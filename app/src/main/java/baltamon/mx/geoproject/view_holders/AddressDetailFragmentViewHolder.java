package baltamon.mx.geoproject.view_holders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import baltamon.mx.geoproject.R;

/**
 * Created by Baltazar Rodriguez on 24/03/2018.
 */
@Deprecated
public class AddressDetailFragmentViewHolder {
    public TextView tvAddressName;
    public TextView tvAddressLatitude;
    public TextView tvAddressLongitude;
    public Button btnClose;

    public AddressDetailFragmentViewHolder(View view) {
        tvAddressName = view.findViewById(R.id.tv_address_name);
        tvAddressLatitude = view.findViewById(R.id.tv_latitude);
        tvAddressLongitude = view.findViewById(R.id.tv_longitude);
        btnClose = view.findViewById(R.id.btn_close_detail);
    }
}
