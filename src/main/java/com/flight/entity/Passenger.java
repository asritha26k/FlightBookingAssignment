package com.flight.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class Passenger {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int passengerId;

	@NotBlank
	private String name;

	@NotNull
	@Positive
	private Long phoneNo;

	@NotBlank
	@Email
	private String emailId;

	@OneToOne(mappedBy = "passenger", cascade = CascadeType.ALL)
	@JsonBackReference
	private Address address;

	@OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL)
	@JsonBackReference
	private List<Ticket> tickets;

	public int getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(int passengerId) {
		this.passengerId = passengerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(Long phoneNum) {
		this.phoneNo = phoneNum;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<Ticket> getTicket() {
		return tickets;
	}

	public void setTicket(List<Ticket> ticket) {
		this.tickets = tickets;
	}
}
