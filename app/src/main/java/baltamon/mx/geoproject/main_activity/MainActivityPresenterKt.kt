package baltamon.mx.geoproject.main_activity

import android.content.Context
import android.location.Geocoder
import android.location.Location
import baltamon.mx.geoproject.models.AddressModel
import baltamon.mx.geoproject.utilities.Presenter
import io.realm.Realm
import io.realm.RealmConfiguration
import java.io.IOException
import java.util.*

/**
 * @author Alejandro Barba on 4/9/18.
 */
class MainActivityPresenterKt constructor(private val mContext: Context) : Presenter {

    private var mView: MainActivityView? = null
    private var mRealm: Realm? = null

    override fun onCreate() {
        Realm.init(mContext)

        val realmConfiguration = RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .name("geoprojectdb.realm")
                .build()

        mRealm = Realm.getInstance(realmConfiguration)

        mView = mContext as MainActivityView

        getAddressesList()
    }

    fun cleanAddressesList() {
        mRealm?.executeTransactionAsync({ it.deleteAll() },
                { mView?.onDeleteAddressesSuccess("List is clean") }) { error ->  mView?.onDeleteAddressesError(error.toString()) }
    }

    private fun getAddressesList() {
        val adresses = mRealm?.where(AddressModel::class.java)?.findAll()
        mView?.onAddressesList(adresses)
    }

    fun onSaveAddress(location: Location?) {
        val address = getAddress(location)
        address.id = getAddressID()

        mRealm?.executeTransactionAsync({ it.copyToRealm(address) },
                { mView?.onAddedAddressSuccess("Address Saved") }) { error -> mView!!.onAddAddressError(error.toString()) }
    }

    private fun getAddressID(): Int {
        val adresses = mRealm?.where(AddressModel::class.java)?.findAll()
        return if (adresses?.isEmpty() == true)
            1
        else
            adresses?.max("addressID")?.toInt()?.plus(1) ?: 1
    }

    private fun getAddress(location: Location?): AddressModel {
        val address = AddressModel()
        address.latitude = location?.latitude
        address.longitude = location?.longitude
        val geocoder = Geocoder(mContext, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(
                    location?.latitude ?: 0.toDouble(),
                    location?.longitude ?: 0.toDouble(), 1)
            if (!addresses.isEmpty()) {
                address.street = addresses[0].getAddressLine(0)
                address.city = addresses[0].locality
                address.state = addresses[0].adminArea
                address.country = addresses[0].countryName
                address.postalCode = addresses[0].postalCode
                address.knownName = addresses[0].featureName
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return address
    }

    override fun onPause() {
        //TODO Nothing here
    }

    override fun onResume() {
        //TODO Nothing here
    }

    override fun onDestroy() {
        mRealm?.close()
    }
}