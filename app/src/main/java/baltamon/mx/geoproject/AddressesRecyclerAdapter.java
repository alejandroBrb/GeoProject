package baltamon.mx.geoproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Baltazar Rodriguez on 22/03/2018.
 */

public class AddressesRecyclerAdapter extends RecyclerView.Adapter<AddressItemViewHolder> {

    private ArrayList<String> arrayList;

    public AddressesRecyclerAdapter (ArrayList<String> arrayList){
        this.arrayList = arrayList;
    }

    @Override
    public AddressItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_view_item, parent, false);

        return new AddressItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressItemViewHolder holder, int position) {
        holder.tvAddressName.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
