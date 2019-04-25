package com.tocydoludoge.wheelersbayclient.Model;

public class Hire {
    private  String plateId,carName,duration,rate,discount;

    public Hire() {
    }

    public Hire(String plateId, String carName, String duration, String rate, String discount) {
        this.plateId = plateId;
        this.carName = carName;
        this.duration = duration;
        this.rate = rate;
        this.discount = discount;
    }

    public String getPlateId() {
        return plateId;
    }

    public void setPlateId(String plateId) {
        this.plateId = plateId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
