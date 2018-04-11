package baltamon.mx.geoproject.view_holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import baltamon.mx.geoproject.R

/**
 * @author Alejandro Barba on 4/9/18.
 */
class AddressItemViewHolderKt constructor(private val view: View) : RecyclerView.ViewHolder(view) {
    var tvAddressName: TextView = view.findViewById(R.id.tv_address)
    var btnPlace: ImageButton = view.findViewById(R.id.btn_show_place)
    var btnDetail: ImageButton = view.findViewById(R.id.btn_show_detail)
}