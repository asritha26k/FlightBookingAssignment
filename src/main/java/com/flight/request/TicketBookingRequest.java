package com.flight.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class TicketBookingRequest {

	@Positive
	public int passenger_id;

	@NotBlank
	public String seatNo;
}
