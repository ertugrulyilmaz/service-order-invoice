package com.eriks.service.order.invoice.controller;

import com.eriks.service.order.invoice.service.InvoiceConsumer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class ConsumerController implements CommandLineRunner {

	private final InvoiceConsumer invoiceConsumer;

	public ConsumerController(final InvoiceConsumer invoiceConsumer) {
		this.invoiceConsumer = invoiceConsumer;
	}

	@Override
	public void run(String... args) {
		invoiceConsumer.start();
	}

	@PreDestroy()
	public void destroy() {
		invoiceConsumer.shutdown();
	}

}
