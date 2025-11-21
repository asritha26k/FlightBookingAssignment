package com.flight.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("address")
public class Address {

    @Id
    @Column("address_id")
    private Integer addressId;

    @Column("house_no")
    private Integer houseNo;

    private String city;
    private String state;
    private String country;

    @Column("pass_id")
    private Integer passengerId; // FK to passenger

    // Getters & Setters
    public Integer getAddressId() { return addressId; }
    public void setAddressId(Integer addressId) { this.addressId = addressId; }

    public Integer getHouseNo() { return houseNo; }
    public void setHouseNo(Integer houseNo) { this.houseNo = houseNo; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Integer getPassengerId() { return passengerId; }
    public void setPassengerId(Integer passengerId) { this.passengerId = passengerId; }
}
