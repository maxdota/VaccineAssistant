package vn.noname.vaccineassistant;

import java.util.ArrayList;
import java.util.Arrays;

public class Place {
    String name, address, start, close, vaccine, age, region, fee;
    public Place(){
    }
    public Place(String name, String address, String start, String close,
                 String vaccine, String age, String region, String fee){
        this.name = name;
        this.address = address;
        this.start = start;
        this.close = close;
        this.vaccine = vaccine;
        this.age = age;
        this.region  = region;
        this.fee = fee;
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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
    @Override
    public String toString(){
        return "Place{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", start='" + start + '\'' +
                ", close='" + close + '\'' +
                ", vaccine='" + vaccine + '\'' +
                ", age='" + age + '\'' +
                ", region='" + region + '\'' +
                ", fee='" + fee + '\'' +
                '}';
    }
}
