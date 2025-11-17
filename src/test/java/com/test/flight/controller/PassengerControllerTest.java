package com.test.flight.controller;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flight.controller.PassengerController;
import com.flight.entity.Address;
import com.flight.entity.Passenger;
import com.flight.entity.Status;
import com.flight.entity.Ticket;
import com.flight.exception.ResourceNotFoundException;
import com.flight.request.PassengerRequest;
import com.flight.service.PassengerService;
import com.flight.service.TicketService;

@ExtendWith(MockitoExtension.class)
class PassengerControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
    void registerPassenger_Success() throws Exception {
        PassengerRequest request = new PassengerRequest();
        request.name = "Alice";
        request.emailId = "alice@example.com";
        request.phoneNum = 919876543210L;
        request.houseNo = 12;
        request.city = "Bengaluru";
        request.state = "Karnataka";
        request.country = "India";

        Passenger saved = new Passenger();
        saved.setPassengerId(10);
        saved.setName(request.name);
        saved.setPhoneNo(request.phoneNum);
        saved.setEmailId(request.emailId);

        Address address = new Address();
        address.setHouseNo(request.houseNo);
        address.setCity(request.city);
        address.setState(request.state);
        address.setCountry(request.country);

        saved.setAddress(address);
        saved.setTicket(new ArrayList<>());

        when(passengerService.add(org.mockito.ArgumentMatchers.any(PassengerRequest.class)))
                .thenReturn(saved);

        mockMvc.perform(post("/api/v1.0/flight/register/passenger")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String body = result.getResponse().getContentAsString();
                    assertTrue(body.contains("Alice"));
                    assertTrue(body.contains("alice@example.com"));
                });
    }

    @Test
    void getTickets_Success() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setPnr("PNR123");
        ticket.setSeatNo("12A");
        ticket.setStatus(Status.Booked);

        List<Ticket> tickets = List.of(ticket);

        when(passengerService.getTickets("alice@example.com")).thenReturn(tickets);

        mockMvc.perform(get("/api/v1.0/flight/booking/history/alice@example.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String body = result.getResponse().getContentAsString();
                    assertTrue(body.contains("PNR123"));
                });
    }

    @Test
    void getTickets_NotFound_Throws() throws Exception {
        when(passengerService.getTickets("missing@example.com"))
                .thenThrow(new ResourceNotFoundException("Passenger not found"));

        jakarta.servlet.ServletException exception = org.junit.jupiter.api.Assertions.assertThrows(
                jakarta.servlet.ServletException.class,
                () -> mockMvc.perform(
                        get("/api/v1.0/flight/booking/history/missing@example.com"))
                        .andReturn()
        );

        Throwable cause = exception.getCause();
        assertTrue(cause instanceof ResourceNotFoundException);
        assertEquals("Passenger not found", cause.getMessage());
    }

    @Test
    void cancelBooking_Success() throws Exception {
        when(ticketService.getDelete("PNR123")).thenReturn("Deleted PNR123");

        mockMvc.perform(delete("/api/v1.0/flight/booking/cancel/PNR123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted PNR123"));
    }

    @Test
    void cancelBooking_NotFound_Throws() throws Exception {
        when(ticketService.getDelete("MISSING"))
                .thenThrow(new ResourceNotFoundException("Ticket not found"));

        jakarta.servlet.ServletException exception = org.junit.jupiter.api.Assertions.assertThrows(
                jakarta.servlet.ServletException.class,
                () -> mockMvc.perform(
                        delete("/api/v1.0/flight/booking/cancel/MISSING"))
                        .andReturn()
        );

        Throwable cause = exception.getCause();
        assertTrue(cause instanceof ResourceNotFoundException);
        assertEquals("Ticket not found", cause.getMessage());
    }
}
