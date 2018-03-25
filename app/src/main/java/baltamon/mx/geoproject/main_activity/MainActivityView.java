package baltamon.mx.geoproject.main_activity;


import java.util.ArrayList;

import baltamon.mx.geoproject.models.AddressModel;
import io.realm.RealmResults;

/**
 * Created by Baltazar Rodriguez on 24/03/2018.
 */

public interface MainActivityView {
    void onAddedAddressSuccess(String message);
    void onAddAddressError(String message);
    void onAddressesList(RealmResults<AddressModel> realmResults);
    void onDeleteAddressesSuccess(String message);
    void onDeleteAddressesError(String message);
    void onAddressSelected(AddressModel address);
}
