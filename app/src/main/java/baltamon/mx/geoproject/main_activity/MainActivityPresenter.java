package baltamon.mx.geoproject.main_activity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import baltamon.mx.geoproject.Presenter;
import baltamon.mx.geoproject.models.AddressModel;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Baltazar Rodriguez on 24/03/2018.
 */

public class MainActivityPresenter implements Presenter {

    private Context mContext;
    private MainActivityView mView;
    private Realm mRealm;

    @Override
    public void onCreate() {
        Realm.init(mContext);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("geoprojectdb.realm").build();
        mRealm = Realm.getInstance(realmConfiguration);
        mView = (MainActivityView) mContext;

        getAddressesList();
    }

    public void cleanAddressesList(){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                mView.onDeleteAddressesSuccess("List is clean");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                mView.onDeleteAddressesError(error.toString());
            }
        });
    }

    private void getAddressesList(){
        RealmResults<AddressModel> results = mRealm.where(AddressModel.class).findAll();
        mView.onAddressesList(results);
    }

    public void onSaveAddress(Location location){
        final AddressModel address = getAddress(location);
        address.setAddressID(getAddressID());

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AddressModel addressModel = realm.copyToRealm(address);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                mView.onAddedAddressSuccess("Address Saved");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                mView.onAddAddressError(error.toString());
            }
        });
    }

    private int getAddressID(){
        RealmResults<AddressModel> results = mRealm.where(AddressModel.class).findAll();
        if (results.isEmpty())
            return 1;
        else
            return results.max("addressID").intValue() + 1;
    }

    private AddressModel getAddress(Location location){
        AddressModel address = new AddressModel();
        address.setAddressLatitude(location.getLatitude());
        address.setAddressLongitude(location.getLongitude());
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (!addresses.isEmpty()){
                address.setAddressStreet(addresses.get(0).getAddressLine(0));
                address.setAddressCity(addresses.get(0).getLocality());
                address.setAddressState(addresses.get(0).getAdminArea());
                address.setAddressCountry(addresses.get(0).getCountryName());
                address.setAddressPostalCode(addresses.get(0).getPostalCode());
                address.setAddressKnownName(addresses.get(0).getFeatureName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        mRealm.close();
    }

    MainActivityPresenter(Context context){
        mContext = context;
    }
}
