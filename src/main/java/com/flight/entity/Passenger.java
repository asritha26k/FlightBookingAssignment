package com.flight.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Table("passenger")
public class Passenger {

    @Id
    @Column("passenger_id")
    private Integer passengerId;

    @NotBlank
    private String name;

    @NotNull
    @Positive
    @Column("phone_no")
    private Long phoneNo;

    @NotBlank
    @Email
    @Column("email_id")
    private String emailId;

    // Getters & Setters
    public Integer getPassengerId() { return passengerId; }
    public void setPassengerId(Integer passengerId) { this.passengerId = passengerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getPhoneNo() { return phoneNo; }
    public void setPhoneNo(Long phoneNo) { this.phoneNo = phoneNo; }

    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }
}
