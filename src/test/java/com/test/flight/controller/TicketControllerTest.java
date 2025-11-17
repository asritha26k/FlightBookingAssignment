package com.test.flight.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flight.controller.TicketController;
import com.flight.entity.Status;
import com.flight.entity.Ticket;
import com.flight.exception.ResourceNotFoundException;
import com.flight.request.TicketBookingRequest;
import com.flight.service.TicketService;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	private TicketService ticketService;

	@InjectMocks
	private TicketController ticketController;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
	}

	@Test
	void bookTicket_Success() throws Exception {
		TicketBookingRequest req = new TicketBookingRequest();
		req.passenger_id = 1;
		req.seatNo = "A1";

		when(ticketService.bookTicketService(anyInt(), org.mockito.ArgumentMatchers.any(TicketBookingRequest.class)))
				.thenReturn("PNR12345");

		mockMvc.perform(post("/api/v1.0/flight/booking/100").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))).andExpect(status().isOk())
				.andExpect(content().string("PNR12345"));
	}

	@Test
	void bookTicket_ResourceNotFound() throws Exception {
		TicketBookingRequest req = new TicketBookingRequest();
		req.passenger_id = 1;
		req.seatNo = "A1";

		when(ticketService.bookTicketService(anyInt(), org.mockito.ArgumentMatchers.any(TicketBookingRequest.class)))
				.thenThrow(new ResourceNotFoundException("Passenger with id 1 not found"));

		jakarta.servlet.ServletException thrown = assertThrows(jakarta.servlet.ServletException.class,
				() -> mockMvc.perform(post("/api/v1.0/flight/booking/100").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))).andReturn());

		Throwable cause = thrown.getCause();
		assertTrue(cause instanceof ResourceNotFoundException);
		assertEquals("Passenger with id 1 not found", cause.getMessage());
	}

	@Test
	void getTicket_Success() throws Exception {
		Ticket ticket = new Ticket();
		ticket.setTicketId(7);
		ticket.setPnr("PNR12345");
		ticket.setSeatNo("12A");
		ticket.setStatus(Status.Booked);

		when(ticketService.getServiceDetails("PNR12345")).thenReturn(ticket);

		mockMvc.perform(get("/api/v1.0/flight/ticket/PNR12345").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(result -> {
					String json = result.getResponse().getContentAsString();
					assertTrue(json.contains("PNR12345"));
					assertTrue(json.contains("12A"));
					assertTrue(json.contains("Booked"));
				});
	}

	@Test
	void getTicket_NotFoundReturnsNullBody() throws Exception {
		when(ticketService.getServiceDetails("MISSING")).thenReturn(null);

		mockMvc.perform(get("/api/v1.0/flight/ticket/MISSING").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string(""));
	}
}
