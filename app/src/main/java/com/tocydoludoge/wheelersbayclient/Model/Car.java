package com.tocydoludoge.wheelersbayclient.Model;

public class Car {



    private  String name,image,description,rate,discount,menuId,carId,availabilityFlag;


    public Car() {

    }

    public Car(String name, String image, String description, String rate, String discount, String menuId, String carId, String availabilityFlag) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.rate = rate;
        this.discount = discount;
        this.menuId = menuId;
        this.carId = carId;
        this.availabilityFlag = availabilityFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getAvailabilityFlag() {
        return availabilityFlag;
    }

    public void setAvailabilityFlag(String availabilityFlag) {
        this.availabilityFlag = availabilityFlag;
    }
}
