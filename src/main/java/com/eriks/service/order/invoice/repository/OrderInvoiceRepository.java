package com.eriks.service.order.invoice.repository;

import com.eriks.service.order.invoice.domain.entity.Invoice;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface OrderInvoiceRepository {

  CompletableFuture<Optional<Invoice>> create(final Invoice order);

  CompletableFuture<Optional<Invoice>> read(final long invoiceId);

  CompletableFuture<Optional<Invoice>> update(final Invoice order);

	CompletableFuture<Optional<Invoice>> cancelByOrderId(final long orderId, final String status, final long updatedAt);

}
