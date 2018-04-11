package baltamon.mx.geoproject.models

import android.os.Parcelable
import io.realm.RealmObject
import kotlinx.android.parcel.Parcelize

/**
 * @author Alejandro Barba on 4/9/18.
 */
@Parcelize
open class AddressModel : RealmObject(), Parcelable {
    var id: Int? = 0
    var knownName: String? = ""
    var street: String? = ""
    var city: String? = ""
    var state: String? = ""
    var country: String? = ""
    var postalCode: String? = ""
    var latitude: Double? = 0.toDouble()
    var longitude: Double? = 0.toDouble()
}