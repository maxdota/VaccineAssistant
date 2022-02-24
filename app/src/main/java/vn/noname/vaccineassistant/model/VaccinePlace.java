package vn.noname.vaccineassistant.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

public class VaccinePlace implements Parcelable {
    public static final String PLACE_TYPE_VACCINE = "vaccine";
    public static final String PLACE_TYPE_CLOTHES_SUPPORT = "clothes_support";
    public static final String PLACE_TYPE_FOOD_SUPPORT = "food_support";
    public static final String PLACE_TYPE_HELP_SUPPORT = "help_support";

    @PropertyName("id")
    public String id;
    @PropertyName("name")
    public String name;
    @PropertyName("phone")
    public String phone;
    @PropertyName("image_url")
    public String imageUrl;
    @PropertyName("address")
    public String address;
    @PropertyName("place_type")
    public String placeType;
    @PropertyName("age_limit_above")
    public int ageLimitAbove;
    @PropertyName("age_limit_below")
    public int ageLimitBelow;
    @PropertyName("fee")
    public long fee;
    @PropertyName("day")
    public String day;
    @PropertyName("opening_time")
    public String openingTime;
    @PropertyName("closing_time")
    public String closingTime;
    @PropertyName("region")
    public String region;
    @PropertyName("vaccine")
    public String vaccine;
    @PropertyName("latitude")
    public double latitude;
    @PropertyName("longitude")
    public double longitude;
    @PropertyName("like_selected_users")
    public String likeSelectedUsers;
    @PropertyName("unlike_selected_users")
    public String unlikeSelectedUsers;
    @PropertyName("like_count")
    public int likeCount;
    @PropertyName("unlike_count")
    public int unlikeCount;

    public VaccinePlace() {
    }

    protected VaccinePlace(Parcel in) {
        id = in.readString();
        name = in.readString();
        phone = in.readString();
        imageUrl = in.readString();
        address = in.readString();
        placeType = in.readString();
        ageLimitAbove = in.readInt();
        ageLimitBelow = in.readInt();
        fee = in.readLong();
        day = in.readString();
        openingTime = in.readString();
        closingTime = in.readString();
        region = in.readString();
        vaccine = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        likeSelectedUsers = in.readString();
        unlikeSelectedUsers = in.readString();
        likeCount = in.readInt();
        unlikeCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(imageUrl);
        dest.writeString(address);
        dest.writeString(placeType);
        dest.writeInt(ageLimitAbove);
        dest.writeInt(ageLimitBelow);
        dest.writeLong(fee);
        dest.writeString(day);
        dest.writeString(openingTime);
        dest.writeString(closingTime);
        dest.writeString(region);
        dest.writeString(vaccine);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(likeSelectedUsers);
        dest.writeString(unlikeSelectedUsers);
        dest.writeInt(likeCount);
        dest.writeInt(unlikeCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VaccinePlace> CREATOR = new Creator<VaccinePlace>() {
        @Override
        public VaccinePlace createFromParcel(Parcel in) {
            return new VaccinePlace(in);
        }

        @Override
        public VaccinePlace[] newArray(int size) {
            return new VaccinePlace[size];
        }
    };

    public boolean isRequired() {
        return region != null && !region.equals("unlimited");
    }

    @Override
    public String toString() {
        return "VaccinePlace{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", placeType='" + placeType + '\'' +
                ", ageLimitAbove=" + ageLimitAbove +
                ", ageLimitBelow=" + ageLimitBelow +
                ", fee=" + fee +
                ", day='" + day + '\'' +
                ", openingTime='" + openingTime + '\'' +
                ", closingTime='" + closingTime + '\'' +
                ", region='" + region + '\'' +
                ", vaccine='" + vaccine + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public LatLng getLatLong() {
        return new LatLng(latitude, longitude);
    }
}
