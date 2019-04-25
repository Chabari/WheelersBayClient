package com.tocydoludoge.wheelersbayclient.Model;

import com.tocydoludoge.wheelersbayclient.Model.Hire;

import java.util.List;

public class Request {
    private String phone;
    private String name;
    private String address;
    private String total;
    private String comment;
    private String status;
    private List<Hire> cars;
    private boolean partial = false;

    public Request() {
    }

    public Request(String phone, String name, String address, String total, String comment, String status, List<Hire> cars) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.comment = comment;
        this.status = status;
        this.cars = cars;
        this.partial = partial;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Hire> getCars() {
        return cars;
    }

    public void setCars(List<Hire> cars) {
        this.cars = cars;
    }

    public boolean isPartial() {
        return partial;
    }

    public void setPartial(boolean partial) {
        this.partial = partial;
    }
}