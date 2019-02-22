package com.eriks.service.order.invoice.domain.dto;

import lombok.Data;

@Data
public class InvoiceEvent {

	private long orderId;
	private String orderStatus;
	private long date;

}
