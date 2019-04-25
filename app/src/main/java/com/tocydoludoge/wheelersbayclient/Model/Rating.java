package com.tocydoludoge.wheelersbayclient.Model;

public class Rating {
    private String userPhone,plateId,rateValue,comment;

    public Rating() {
    }

    public Rating(String userPhone, String plateId, String rateValue, String comment) {
        this.userPhone = userPhone;
        this.plateId = plateId;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getPlateId() {
        return plateId;
    }

    public void setPlateId(String plateId) {
        this.plateId = plateId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
