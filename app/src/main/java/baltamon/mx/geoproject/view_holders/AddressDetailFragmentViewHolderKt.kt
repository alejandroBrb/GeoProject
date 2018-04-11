package baltamon.mx.geoproject.view_holders

import android.view.View
import android.widget.Button
import android.widget.TextView
import baltamon.mx.geoproject.R

/**
 * @author Alejandro Barba on 4/9/18.
 */
class AddressDetailFragmentViewHolderKt constructor(private val view: View) {
    var tvAddressName: TextView = view.findViewById(R.id.tv_address_name)
    var tvAddressLatitude: TextView = view.findViewById(R.id.tv_latitude)
    var tvAddressLongitude: TextView = view.findViewById(R.id.tv_longitude)
    var btnClose: Button = view.findViewById(R.id.btn_close_detail)
}