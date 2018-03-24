package baltamon.mx.geoproject.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import baltamon.mx.geoproject.models.AddressModel;
import baltamon.mx.geoproject.view_holders.AddressItemViewHolder;
import baltamon.mx.geoproject.R;
import io.realm.RealmResults;

/**
 * Created by Baltazar Rodriguez on 22/03/2018.
 */

public class AddressesRecyclerAdapter extends RecyclerView.Adapter<AddressItemViewHolder> {

    private RealmResults<AddressModel> realmResults;

    public AddressesRecyclerAdapter (RealmResults<AddressModel> realmResults){
        this.realmResults = realmResults;
    }

    @Override
    public AddressItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_view_item, parent, false);

        return new AddressItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressItemViewHolder holder, int position) {
        holder.tvAddressName.setText(realmResults.get(position).getAddressStreet());
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }
}
