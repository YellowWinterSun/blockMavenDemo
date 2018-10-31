package com.dy.bean;

/**
 * 类的描述
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * Create on 2018/9/26 15:22
 */
public class Car {
    private String carNumber;
    private Integer highestSpeed;
    private String carColor;

    public Car(){}

    /* getter and setter */

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Integer getHighestSpeed() {
        return highestSpeed;
    }

    public void setHighestSpeed(Integer highestSpeed) {
        this.highestSpeed = highestSpeed;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }
}
