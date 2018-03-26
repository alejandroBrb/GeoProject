package baltamon.mx.geoproject.address_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import baltamon.mx.geoproject.R;
import baltamon.mx.geoproject.models.AddressModel;
import baltamon.mx.geoproject.view_holders.AddressDetailFragmentViewHolder;

/**
 * Created by Baltazar Rodriguez on 24/03/2018.
 */

public class AddressDetailFragment extends DialogFragment implements AddressDetailFragmentView{

    private AddressDetailFragmentPresenter mPresenter;

    private final static String OBJECT_KEY = "ADDRESS_OBJECT";

    public AddressDetailFragment newInstance(AddressModel address){
        AddressDetailFragment detailFragment = new AddressDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(OBJECT_KEY, address);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new AddressDetailFragmentPresenter(
                getArguments().getParcelable(OBJECT_KEY), this);
        mPresenter.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_address_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.onViewCreated(view);
    }

    @Override
    public void onShowAddressDetails(AddressModel address, View view) {
        AddressDetailFragmentViewHolder holder = new AddressDetailFragmentViewHolder(view);
        holder.tvAddressName.setText(address.getAddressStreet());
        holder.tvAddressLatitude.setText("Lat. " + String.valueOf(address.getAddressLatitude()));
        holder.tvAddressLongitude.setText("Lng. " + String.valueOf(address.getAddressLongitude()));
        holder.btnClose.setOnClickListener(view1 -> dismiss());
    }
}
