package com.eriks.service.order.invoice.consumer;

import com.eriks.service.order.invoice.service.OrderInvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.kinesis.processor.ShardRecordProcessor;
import software.amazon.kinesis.processor.ShardRecordProcessorFactory;

public class InvoiceRecordProcessorFactory implements ShardRecordProcessorFactory {

	private final OrderInvoiceService orderInvoiceService;
	private final ObjectMapper objectMapper;

	public InvoiceRecordProcessorFactory(final OrderInvoiceService orderInvoiceService, final ObjectMapper objectMapper) {
		this.orderInvoiceService = orderInvoiceService;
		this.objectMapper = objectMapper;
	}

	public ShardRecordProcessor shardRecordProcessor() {
		return new InvoiceRecordProcessor(orderInvoiceService, objectMapper);
	}

}
