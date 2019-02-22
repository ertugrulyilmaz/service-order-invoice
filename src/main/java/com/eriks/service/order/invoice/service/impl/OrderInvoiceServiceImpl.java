package com.eriks.service.order.invoice.service.impl;

import com.eriks.service.order.invoice.domain.dto.InvoiceEvent;
import com.eriks.service.order.invoice.domain.dto.InvoiceRequest;
import com.eriks.service.order.invoice.domain.dto.InvoiceResponse;
import com.eriks.service.order.invoice.domain.entity.Invoice;
import com.eriks.service.order.invoice.repository.OrderInvoiceRepository;
import com.eriks.service.order.invoice.service.OrderInvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Slf4j
@Service
public class OrderInvoiceServiceImpl implements OrderInvoiceService {

	private final OrderInvoiceRepository orderInvoiceRepository;

	public OrderInvoiceServiceImpl(final OrderInvoiceRepository orderInvoiceRepository) {
		this.orderInvoiceRepository = orderInvoiceRepository;
	}

	@Override
	public CompletableFuture<Optional<InvoiceResponse>> create(final long orderId) {
		final Invoice invoice = new Invoice();
		invoice.setOrderId(orderId);
		invoice.setInvoiceNumber(UUID.randomUUID().toString());
		invoice.setStatus("CREATED");
		invoice.setCreatedAt(new Date().getTime());

		return orderInvoiceRepository.create(invoice)
				.thenApply(mapMaybeOrderToResponse)
				.exceptionally(handleFailure);
	}

	@Override
	public CompletableFuture<Optional<InvoiceResponse>> read(final long invoiceId) {
		return orderInvoiceRepository.read(invoiceId)
				.thenApply(mapMaybeOrderToResponse)
				.exceptionally(handleFailure);
	}

	@Override
	public CompletableFuture<Optional<InvoiceResponse>> update(final InvoiceRequest invoiceRequest) {
		final Invoice order = new Invoice();
		order.setId(invoiceRequest.getId());
		order.setStatus(invoiceRequest.getStatus());
		order.setUpdatedAt(new Date().getTime());

		return orderInvoiceRepository.update(order)
				.thenApply(mapMaybeOrderToResponse)
				.exceptionally(handleFailure);
	}

	@Override
	public CompletableFuture<Optional<InvoiceResponse>> delete(final long invoiceId) {
		final Invoice order = new Invoice();
		order.setId(invoiceId);
		order.setStatus("DELETED");
		order.setUpdatedAt(new Date().getTime());

		return orderInvoiceRepository.update(order)
				.thenApply(mapMaybeOrderToResponse)
				.exceptionally(handleFailure);
	}

	public void cancel(final long orderId) {
		orderInvoiceRepository.cancelByOrderId(orderId, "CANCELLED", new Date().getTime())
				.thenApply(mapMaybeOrderToResponse)
				.exceptionally(handleFailure);
	}

	@Override
	public void process(final InvoiceEvent invoiceEvent) {
		log.info("InvoiceEvent: {}", invoiceEvent);

		switch (invoiceEvent.getOrderStatus()) {
			case "PAYMENT_CONFIRMED":
					create(invoiceEvent.getOrderId());
				break;
			case "CANCELLED":
				cancel(invoiceEvent.getOrderId());
				break;
		}
	}

	private static Function<Optional<Invoice>, Optional<InvoiceResponse>> mapMaybeOrderToResponse = maybeOrder ->
			maybeOrder.map(entity -> {
				final InvoiceResponse orderResponse = new InvoiceResponse();
				orderResponse.setOrderId(entity.getId());
				orderResponse.setOrderStatus(entity.getStatus());
				orderResponse.setCreatedAt(entity.getCreatedAt());

				return orderResponse;
			});

	private static Function<Throwable, Optional<InvoiceResponse>> handleFailure = throwable -> {
		log.error("{}", throwable);

		return Optional.empty();
	};

}
