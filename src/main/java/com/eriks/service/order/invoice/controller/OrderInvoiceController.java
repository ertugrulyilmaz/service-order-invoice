package com.eriks.service.order.invoice.controller;

import com.eriks.service.order.invoice.domain.dto.InvoiceRequest;
import com.eriks.service.order.invoice.domain.dto.InvoiceResponse;
import com.eriks.service.order.invoice.service.OrderInvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/v1/order-invoice", produces = APPLICATION_JSON_VALUE)
public class OrderInvoiceController {

	private final OrderInvoiceService orderInvoiceService;

	public OrderInvoiceController(final OrderInvoiceService orderInvoiceService) {
		this.orderInvoiceService = orderInvoiceService;
	}

	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	public CompletableFuture<ResponseEntity> create(@RequestParam(name = "orderId") final long orderId) {
		return orderInvoiceService.create(orderId)
				.thenApply(mapMaybeOrderToResponse)
				.exceptionally(t -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}

	@GetMapping(value = "/{orderId}")
	public CompletableFuture<ResponseEntity> read(@PathVariable final Long orderId) {
		return orderInvoiceService.read(orderId)
				.thenApply(mapMaybeOrderToResponse)
				.exceptionally(handleGetFailure.apply(orderId));
	}

	@PutMapping(consumes = APPLICATION_JSON_VALUE)
	public CompletableFuture<ResponseEntity> update(@RequestBody final InvoiceRequest request) {
		return orderInvoiceService.update(request)
				.thenApply(mapMaybeOrderToResponse)
				.exceptionally(t -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}

	@DeleteMapping(value = "/{orderId}")
	public CompletableFuture<ResponseEntity> delete(@PathVariable final Long orderId) {
		return orderInvoiceService.delete(orderId)
				.thenApply(mapMaybeOrderToResponse)
				.exceptionally(handleGetFailure.apply(orderId));
	}

	private static Function<Optional<InvoiceResponse>, ResponseEntity> mapMaybeOrderToResponse = maybeOrder -> maybeOrder
			.<ResponseEntity>map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());

	private static Function<Long, Function<Throwable, ResponseEntity>> handleGetFailure = id -> throwable -> {
		log.error(String.format("Unable to retrieve order for id: %s", id), throwable);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	};

}
