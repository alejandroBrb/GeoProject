package baltamon.mx.geoproject.address_detail

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import baltamon.mx.geoproject.R
import baltamon.mx.geoproject.models.AddressModel
import baltamon.mx.geoproject.view_holders.AddressDetailFragmentViewHolderKt

/**
 * @author Alejandro Barba on 4/9/18.
 */
class AddressDetailFragmentKt : DialogFragment(), AddressDetailFragmentView {

    private var mPresenter: AddressDetailFragmentPresenterKt? = null

    private val OBJECT_KEY = "ADDRESS_OBJECT"

    fun newInstance(address: AddressModel): AddressDetailFragmentKt {
        val detailFragment = AddressDetailFragmentKt()
        val bundle = Bundle()
        bundle.putParcelable(OBJECT_KEY, address)
        detailFragment.arguments = bundle
        return detailFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPresenter = AddressDetailFragmentPresenterKt(arguments.getParcelable(OBJECT_KEY), this)
        mPresenter?.onCreate()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.dialog_fragment_address_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter?.onViewCreated(view)
    }

    override fun onShowAddressDetails(address: AddressModel, view: View) {
        val holder = AddressDetailFragmentViewHolderKt(view)

        holder.tvAddressName.text = address.street
        holder.tvAddressLatitude.text = "Lat. ${address.latitude}"
        holder.tvAddressLongitude.text = "Lng. ${address.longitude}"

        holder.btnClose.setOnClickListener { dismiss() }
    }
}