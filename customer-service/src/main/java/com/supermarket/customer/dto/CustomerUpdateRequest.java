package com.supermarket.customer.dto;

public class CustomerUpdateRequest {
    private String email;
    private String address;
    private String phone;

    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
}
