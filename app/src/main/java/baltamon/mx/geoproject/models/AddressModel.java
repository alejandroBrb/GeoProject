package baltamon.mx.geoproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by Baltazar Rodriguez on 24/03/2018.
 */

public class AddressModel extends RealmObject implements Parcelable {
    private int addressID;
    private String addressKnownName;
    private String addressStreet;
    private String addressCity;
    private String addressState;
    private String addressCountry;
    private String addressPostalCode;
    private double addressLatitude;
    private double addressLongitude;

    protected AddressModel(Parcel in) {
        addressID = in.readInt();
        addressKnownName = in.readString();
        addressStreet = in.readString();
        addressCity = in.readString();
        addressState = in.readString();
        addressCountry = in.readString();
        addressPostalCode = in.readString();
        addressLatitude = in.readDouble();
        addressLongitude = in.readDouble();
    }

    public AddressModel(){}

    public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel in) {
            return new AddressModel(in);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getAddressKnownName() {
        return addressKnownName;
    }

    public void setAddressKnownName(String addressKnownName) {
        this.addressKnownName = addressKnownName;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressState() {
        return addressState;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getAddressPostalCode() {
        return addressPostalCode;
    }

    public void setAddressPostalCode(String addressPostalCode) {
        this.addressPostalCode = addressPostalCode;
    }

    public double getAddressLatitude() {
        return addressLatitude;
    }

    public void setAddressLatitude(double addressLatitude) {
        this.addressLatitude = addressLatitude;
    }

    public double getAddressLongitude() {
        return addressLongitude;
    }

    public void setAddressLongitude(double addressLongitude) {
        this.addressLongitude = addressLongitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(addressID);
        parcel.writeString(addressKnownName);
        parcel.writeString(addressStreet);
        parcel.writeString(addressCity);
        parcel.writeString(addressState);
        parcel.writeString(addressCountry);
        parcel.writeString(addressPostalCode);
        parcel.writeDouble(addressLatitude);
        parcel.writeDouble(addressLongitude);
    }
}
