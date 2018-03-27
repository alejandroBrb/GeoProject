package baltamon.mx.geoproject.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import baltamon.mx.geoproject.address_detail.AddressDetailFragment;
import baltamon.mx.geoproject.main_activity.MainActivityView;
import baltamon.mx.geoproject.models.AddressModel;
import baltamon.mx.geoproject.view_holders.AddressItemViewHolder;
import baltamon.mx.geoproject.R;
import io.realm.RealmResults;

/**
 * Created by Baltazar Rodriguez on 22/03/2018.
 */

public class AddressesRecyclerAdapter extends RecyclerView.Adapter<AddressItemViewHolder> {

    private RealmResults<AddressModel> mRealmResults;
    private FragmentManager mFragmentManager;
    private MainActivityView mView;

    public AddressesRecyclerAdapter (Context context, RealmResults<AddressModel> realmResults,
                                     FragmentManager manager){
        mRealmResults = realmResults;
        mFragmentManager = manager;
        mView = (MainActivityView) context;
    }

    @Override
    public AddressItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_view_item, parent, false);

        return new AddressItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressItemViewHolder holder, final int position) {
        holder.tvAddressName.setText(mRealmResults.get(position).getAddressStreet());
        holder.btnDetail.setOnClickListener(view -> {
            AddressDetailFragment dialog = new AddressDetailFragment().
                    newInstance(mRealmResults.get(position));
            dialog.show(mFragmentManager, "Detail");
        });
        holder.btnPlace.setOnClickListener(view ->
                mView.onAddressSelected(mRealmResults.get(position)));
    }

    @Override
    public int getItemCount() {
        return mRealmResults.size();
    }
}
