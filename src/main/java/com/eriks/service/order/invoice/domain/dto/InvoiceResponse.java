package com.eriks.service.order.invoice.domain.dto;

import lombok.Data;

@Data
public class InvoiceResponse {

	private long id;
	private long orderId;
	private String invoiceNumber;
	private String orderStatus;
	private long createdAt;

}
