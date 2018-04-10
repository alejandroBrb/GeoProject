package baltamon.mx.geoproject.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import baltamon.mx.geoproject.R;

/**
 * Created by Baltazar Rodriguez on 22/03/2018.
 */
@Deprecated
public class AddressItemViewHolder extends RecyclerView.ViewHolder {
    public TextView tvAddressName;
    public ImageButton btnPlace;
    public ImageButton btnDetail;

    public AddressItemViewHolder(View view){
        super(view);
        tvAddressName = view.findViewById(R.id.tv_address);
        btnPlace = view.findViewById(R.id.btn_show_place);
        btnDetail = view.findViewById(R.id.btn_show_detail);
    }
}
