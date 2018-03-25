package baltamon.mx.geoproject.address_detail;

import android.content.Context;
import android.view.View;

import baltamon.mx.geoproject.Presenter;
import baltamon.mx.geoproject.models.AddressModel;

/**
 * Created by Baltazar Rodriguez on 24/03/2018.
 */

public class AddressDetailFragmentPresenter implements Presenter {

    private AddressModel mAddress;
    private AddressDetailFragmentView mView;

    public AddressDetailFragmentPresenter(AddressModel addressModel, AddressDetailFragment fragment){
        mAddress = addressModel;
        mView = fragment;
    }

    @Override
    public void onCreate() {

    }

    public void onViewCreated(View view){
        mView.onShowAddressDetails(mAddress, view);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }
}
