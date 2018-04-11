package baltamon.mx.geoproject.main_activity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import baltamon.mx.geoproject.models.AddressModel;
import baltamon.mx.geoproject.utilities.Presenter;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Baltazar Rodriguez on 24/03/2018.
 */
@Deprecated
public class MainActivityPresenter implements Presenter {

    private Context mContext;
    private MainActivityView mView;
    private Realm mRealm;

    @Override
    public void onCreate() {
        Realm.init(mContext);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name("geoprojectdb.realm").build();
        mRealm = Realm.getInstance(realmConfiguration);
        mView = (MainActivityView) mContext;

        getAddressesList();
    }

    public void cleanAddressesList(){
        mRealm.executeTransactionAsync(realm -> realm.deleteAll(),
                () -> mView.onDeleteAddressesSuccess("List is clean"),
                error -> mView.onDeleteAddressesError(error.toString()));
    }

    private void getAddressesList(){
        RealmResults<AddressModel> results = mRealm.where(AddressModel.class).findAll();
        mView.onAddressesList(results);
    }

    public void onSaveAddress(Location location){
        final AddressModel address = getAddress(location);
        address.setId(getAddressID());

        mRealm.executeTransactionAsync(realm ->
                realm.copyToRealm(address),
                () -> mView.onAddedAddressSuccess("Address Saved"),
                error -> mView.onAddAddressError(error.toString()));
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
        address.setLatitude(location.getLatitude());
        address.setLongitude(location.getLongitude());
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (!addresses.isEmpty()){
                address.setStreet(addresses.get(0).getAddressLine(0));
                address.setCity(addresses.get(0).getLocality());
                address.setState(addresses.get(0).getAdminArea());
                address.setCountry(addresses.get(0).getCountryName());
                address.setPostalCode(addresses.get(0).getPostalCode());
                address.setKnownName(addresses.get(0).getFeatureName());
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
