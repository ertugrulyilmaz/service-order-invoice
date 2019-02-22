package com.eriks.service.order.invoice.repository.impl;

import com.eriks.service.config.AsyncConfig;
import com.eriks.service.order.invoice.domain.entity.Invoice;
import com.eriks.service.order.invoice.repository.OrderInvoiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Repository
public class OrderInvoiceRepositoryImpl implements OrderInvoiceRepository {

	private final static String SQL_INSERT = "INSERT INTO invoices(order_id, invoice_number, status, created_at, updated_at) " +
			" VALUES(:orderId, :invoiceNumber, :status, :createdAt, :updatedAt) RETURNING *";
	private final static String SQL_FIND_BY_ID = "SELECT id, order_id, invoice_number, status, created_at, updated_at FROM orders WHERE id = :invoiceId";
	private final static String SQL_UPDATE_STATUS_BY_ID = "UPDATE invoices SET status = :status, updated_at = :updatedAt WHERE id = :id RETURNING *";
	private final static String SQL_UPDATE_STATUS_BY_ORDER_ID = "UPDATE invoices SET status = :status, updated_at = :updatedAt WHERE order_id = :orderId RETURNING *";

	private final Sql2o sql2o;
	private final Executor executor;

	public OrderInvoiceRepositoryImpl(final Sql2o sql2o, @Qualifier(AsyncConfig.TASK_EXECUTOR_REPOSITORY) final Executor executor) {
		this.sql2o = sql2o;
		this.executor = executor;
	}

	@Override
	public CompletableFuture<Optional<Invoice>> create(final Invoice order) {
		return CompletableFuture.supplyAsync(() -> {
			try (Connection connection = sql2o.open()) {
				return Optional.ofNullable(connection.createQuery(SQL_INSERT)
						.setAutoDeriveColumnNames(true)
						.bind(order)
						.executeAndFetchFirst(Invoice.class));
			}
		}, executor);
	}

	@Override
	public CompletableFuture<Optional<Invoice>> read(long invoiceId) {
		return CompletableFuture.supplyAsync(() -> {
			try (Connection connection = sql2o.open()) {
				return Optional.ofNullable(connection.createQuery(SQL_FIND_BY_ID)
						.setAutoDeriveColumnNames(true)
						.addParameter("invoiceId", invoiceId)
						.executeAndFetchFirst(Invoice.class));
			}
		}, executor);
	}

	@Override
	public CompletableFuture<Optional<Invoice>> update(final Invoice order) {
		return CompletableFuture.supplyAsync(() -> {
			try (Connection connection = sql2o.open()) {
				return Optional.ofNullable(connection.createQuery(SQL_UPDATE_STATUS_BY_ID)
						.setAutoDeriveColumnNames(true)
						.bind(order)
						.executeAndFetchFirst(Invoice.class));
			}
		}, executor);
	}

	@Override
	public CompletableFuture<Optional<Invoice>> cancelByOrderId(final long orderId, final String status, final long updatedAt) {
		return CompletableFuture.supplyAsync(() -> {
			try (Connection connection = sql2o.open()) {
				return Optional.ofNullable(connection.createQuery(SQL_UPDATE_STATUS_BY_ORDER_ID)
						.setAutoDeriveColumnNames(true)
						.addParameter("orderId", orderId)
						.addParameter("status", status)
						.addParameter("updatedAt", updatedAt)
						.executeAndFetchFirst(Invoice.class));
			}
		}, executor);
	}

}
