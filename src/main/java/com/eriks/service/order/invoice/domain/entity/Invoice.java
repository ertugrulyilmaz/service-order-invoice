package com.eriks.service.order.invoice.domain.entity;

import lombok.Data;

@Data
public class Invoice {

	private long id;
	private long orderId;
	private String invoiceNumber;
	private String status;
	private long createdAt;
	private long updatedAt;

}
