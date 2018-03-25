package baltamon.mx.geoproject.address_detail;

import android.view.View;

import baltamon.mx.geoproject.models.AddressModel;

/**
 * Created by Baltazar Rodriguez on 24/03/2018.
 */

public interface AddressDetailFragmentView {
    void onShowAddressDetails(AddressModel address, View view);
}
