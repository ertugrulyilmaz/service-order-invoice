package com.eriks.service.order.invoice.service;

import com.eriks.service.order.invoice.domain.dto.InvoiceEvent;
import com.eriks.service.order.invoice.domain.dto.InvoiceRequest;
import com.eriks.service.order.invoice.domain.dto.InvoiceResponse;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface OrderInvoiceService {

  CompletableFuture<Optional<InvoiceResponse>> create(final long orderId);

  CompletableFuture<Optional<InvoiceResponse>> read(final long invoiceId);

  CompletableFuture<Optional<InvoiceResponse>> update(final InvoiceRequest invoiceRequest);

  CompletableFuture<Optional<InvoiceResponse>> delete(final long invoiceId);

	void process(final InvoiceEvent invoiceEvent);

}
