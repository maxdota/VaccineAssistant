package vn.noname.vaccineassistant.model;

import com.google.firebase.database.PropertyName;

public class VaccinePlace {
    @PropertyName("id")
    public String id;
    @PropertyName("name")
    public String name;
    @PropertyName("address")
    public String address;
    @PropertyName("age_limit_above")
    public int ageLimitAbove;
    @PropertyName("age_limit_below")
    public int ageLimitBelow;
    @PropertyName("fee")
    public long fee;
    @PropertyName("opening_time")
    public String openingTime;
    @PropertyName("closing_time")
    public String closingTime;
    @PropertyName("region")
    public String region;
    @PropertyName("vaccine")
    public String vaccine;

    @Override
    public String toString() {
        return "VaccinePlace{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", ageLimitAbove=" + ageLimitAbove +
                ", ageLimitBelow=" + ageLimitBelow +
                ", fee=" + fee +
                ", openingTime='" + openingTime + '\'' +
                ", closingTime='" + closingTime + '\'' +
                ", region='" + region + '\'' +
                ", vaccine='" + vaccine + '\'' +
                '}';
    }
}
