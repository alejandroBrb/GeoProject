package baltamon.mx.geoproject.address_detail

import android.view.View
import baltamon.mx.geoproject.models.AddressModel
import baltamon.mx.geoproject.utilities.Presenter

/**
 * @author Alejandro Barba on 4/9/18.
 */
class AddressDetailFragmentPresenterKt constructor(
        private val mAddressModel: AddressModel,
        private val mView: AddressDetailFragmentKt) : Presenter {

    override fun onCreate() {
        // TODO
    }

    fun onViewCreated(view: View) {
        mView.onShowAddressDetails(mAddressModel, view)
    }

    override fun onPause() {
        // TODO
    }

    override fun onResume() {
        // TODO
    }

    override fun onDestroy() {
        // TODO
    }
}