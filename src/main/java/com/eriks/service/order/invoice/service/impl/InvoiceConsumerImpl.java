package com.eriks.service.order.invoice.service.impl;

import com.eriks.service.config.model.KinesisProperties;
import com.eriks.service.order.invoice.consumer.InvoiceRecordProcessorFactory;
import com.eriks.service.order.invoice.service.InvoiceConsumer;
import com.eriks.service.order.invoice.service.OrderInvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.kinesis.common.ConfigsBuilder;
import software.amazon.kinesis.common.KinesisClientUtil;
import software.amazon.kinesis.coordinator.Scheduler;
import software.amazon.kinesis.metrics.MetricsLevel;
import software.amazon.kinesis.retrieval.polling.PollingConfig;

import java.util.UUID;
import java.util.concurrent.*;

@Slf4j
@Component
public class InvoiceConsumerImpl implements InvoiceConsumer {

	private final Region region;
	private final String streamName;
	private final KinesisAsyncClient kinesisClient;
	private final ScheduledExecutorService producerExecutor;
	private final OrderInvoiceService orderInvoiceService;
	private final ObjectMapper objectMapper;

	private Scheduler scheduler;

	public InvoiceConsumerImpl(final KinesisProperties kinesisProperties, final OrderInvoiceService orderInvoiceService, final ObjectMapper objectMapper) {
		this.region = kinesisProperties.getRegion();
		this.streamName = kinesisProperties.getStreamName();
		this.kinesisClient = KinesisClientUtil.createKinesisAsyncClient(KinesisAsyncClient.builder().region(kinesisProperties.getRegion()));
		this.orderInvoiceService = orderInvoiceService;
		this.objectMapper = objectMapper;
		this.producerExecutor = Executors.newSingleThreadScheduledExecutor();
	}

	public void start() {
		final String workerId = UUID.randomUUID().toString();
		final DynamoDbAsyncClient dynamoClient = DynamoDbAsyncClient.builder().region(region).build();
		final CloudWatchAsyncClient cloudWatchClient = CloudWatchAsyncClient.builder().region(region).build();
		final ConfigsBuilder configsBuilder = new ConfigsBuilder(streamName, streamName, kinesisClient, dynamoClient, cloudWatchClient, workerId, new InvoiceRecordProcessorFactory(orderInvoiceService, objectMapper));

		scheduler = new Scheduler(
				configsBuilder.checkpointConfig(),
				configsBuilder.coordinatorConfig(),
				configsBuilder.leaseManagementConfig(),
				configsBuilder.lifecycleConfig(),
				configsBuilder.metricsConfig().metricsLevel(MetricsLevel.NONE),
				configsBuilder.processorConfig(),
				configsBuilder.retrievalConfig().retrievalSpecificConfig(new PollingConfig(streamName, kinesisClient))
		);

		final Thread schedulerThread = new Thread(scheduler);
		schedulerThread.setDaemon(true);
		schedulerThread.start();
	}

	public void shutdown() {
		producerExecutor.shutdownNow();

		final Future<Boolean> gracefulShutdownFuture = scheduler.startGracefulShutdown();
		log.info("Waiting up to 20 seconds for shutdown to complete.");

		try {
			gracefulShutdownFuture.get(20, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.info("Interrupted while waiting for graceful shutdown. Continuing.");
		} catch (ExecutionException e) {
			log.error("Exception while executing graceful shutdown.", e);
		} catch (TimeoutException e) {
			log.error("Timeout while waiting for shutdown.  Scheduler may not have exited.");
		}

		log.info("Completed, shutting down now.");
	}

}
