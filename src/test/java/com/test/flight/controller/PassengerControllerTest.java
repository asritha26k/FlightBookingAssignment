package com.test.flight.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flight.controller.PassengerController;
import com.flight.entity.Status;
import com.flight.entity.Ticket;
import com.flight.service.PassengerService;
import com.flight.service.TicketService;

@ExtendWith(MockitoExtension.class)
class PassengerControllerTest {

	private MockMvc mockMvc;
	private ObjectMapper mapper = new ObjectMapper();

	@Mock
	private PassengerService passengerService;

	@Mock
	private TicketService ticketService;

	@InjectMocks
	private PassengerController passengerController;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(passengerController).build();
	}

	@Test
	void getTickets_Success() throws Exception {
		Ticket ticket = new Ticket();
		ticket.setPnr("PNR123");
		ticket.setSeatNo("12A");
		ticket.setStatus(Status.Booked);

		Mockito.when(passengerService.getTickets("alice@example.com"))
				.thenReturn(ResponseEntity.status(HttpStatus.OK).body(List.of(ticket)));

		mockMvc.perform(get("/api/v1.0/flight/booking/history/alice@example.com").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].pnr").value("PNR123"));
	}

	@Test
	void cancelBooking_Success() throws Exception {
		Mockito.when(ticketService.getDelete("PNR123"))
				.thenReturn(ResponseEntity.status(HttpStatus.OK).build());

		mockMvc.perform(delete("/api/v1.0/flight/booking/cancel/PNR123")).andExpect(status().isOk())
		.andExpect(content().string(""));
	}
}
