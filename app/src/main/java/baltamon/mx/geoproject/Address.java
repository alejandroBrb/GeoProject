package baltamon.mx.geoproject;

import io.realm.MutableRealmInteger;
import io.realm.RealmObject;

/**
 * Created by Baltazar Rodriguez on 23/03/2018.
 */

public class Address extends RealmObject {
    private final MutableRealmInteger addressID = MutableRealmInteger.valueOf(0);
    private String addressStreet;
    private String addressNumber;
    private String addressSuburb;
    private String addressCity;
    private String addressState;
    private String addressCP;
    private double addressLatitude;
    private double addressLongitude;

    public MutableRealmInteger getAddressID() {
        return addressID;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        this.addressNumber = addressNumber;
    }

    public String getAddressSuburb() {
        return addressSuburb;
    }

    public void setAddressSuburb(String addressSuburb) {
        this.addressSuburb = addressSuburb;
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

    public String getAddressCP() {
        return addressCP;
    }

    public void setAddressCP(String addressCP) {
        this.addressCP = addressCP;
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
