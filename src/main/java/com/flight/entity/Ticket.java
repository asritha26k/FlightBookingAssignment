package com.flight.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Table("ticket")
public class Ticket {

    @Id
    @Column("ticket_id")
    private Integer ticketId;

    @NotBlank
    private String pnr;

    @NotBlank
    @Column("seat_no")
    private String seatNo;

    @NotNull
    private Status status;

    @Column("flight_id")
    private Integer flightId;

    @Column("passenger_id")
    private Integer passengerId;

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public Integer getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Integer passengerId) {
        this.passengerId = passengerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Ticket))
            return false;
        Ticket ticket = (Ticket) o;
        return pnr != null && pnr.equals(ticket.getPnr());
    }

    @Override
    public int hashCode() {
        return pnr != null ? pnr.hashCode() : 0;
    }
}
