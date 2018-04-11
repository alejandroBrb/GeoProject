package baltamon.mx.geoproject.adapters

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import baltamon.mx.geoproject.R
import baltamon.mx.geoproject.address_detail.AddressDetailFragmentKt
import baltamon.mx.geoproject.main_activity.MainActivityView
import baltamon.mx.geoproject.models.AddressModel
import baltamon.mx.geoproject.view_holders.AddressItemViewHolderKt
import io.realm.RealmResults

/**
 * @author Alejandro Barba on 4/9/18.
 */
class AddressAdapter constructor(
        private val mContext: Context,
        private val mRealmAddress: RealmResults<AddressModel>,
        private val mFragmentManager: FragmentManager) : RecyclerView.Adapter<AddressItemViewHolderKt>() {

    private var mView: MainActivityView = mContext as MainActivityView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressItemViewHolderKt {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_item, parent, false)
        return AddressItemViewHolderKt(view)
    }

    override fun onBindViewHolder(holder: AddressItemViewHolderKt, position: Int) {
        val address = mRealmAddress[position]

        holder.tvAddressName.text = getAddressName(address)

        holder.btnDetail.setOnClickListener {
            val dialog = AddressDetailFragmentKt().newInstance(address!!)
            dialog.show(mFragmentManager, "Detail")
        }

        holder.btnPlace.setOnClickListener { mView.onAddressSelected(address) }
    }

    private fun getAddressName(address: AddressModel?) = address?.street ?: "No address name"

    override fun getItemCount() = mRealmAddress.size

}