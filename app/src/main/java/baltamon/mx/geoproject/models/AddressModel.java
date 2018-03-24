package baltamon.mx.geoproject.models;

import io.realm.MutableRealmInteger;
import io.realm.RealmObject;

/**
 * Created by Baltazar Rodriguez on 24/03/2018.
 */

public class AddressModel extends RealmObject {
    private int addressID;
    private String addressKnownName;
    private String addressStreet;
    private String addressCity;
    private String addressState;
    private String addressCountry;
    private String addressPostalCode;
    private double addressLatitude;
    private double addressLongitude;

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
}
