package baltamon.mx.geoproject.main_activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import baltamon.mx.geoproject.R
import baltamon.mx.geoproject.adapters.AddressAdapter
import baltamon.mx.geoproject.models.AddressModel
import baltamon.mx.geoproject.utilities.SwipeButton
import baltamon.mx.geoproject.utilities.SwipeButtonCustomItems
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.realm.RealmResults

/**
 * @author Alejandro Barba on 4/9/18.
 */
class MainActivityKt : AppCompatActivity(),
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        MainActivityView {

    private var mPresenter: MainActivityPresenterKt? = null
    private var mAdapter: AddressAdapter? = null
    private var mSlidePanel: SlidingUpPanelLayout? = null
    private var mGoogleMap: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationPermissionGranted: Boolean = false
    private var mLastKnownLocation: Location? = null
    private var mLocationClient: FusedLocationProviderClient? = null

    private val CAMERA_ZOOM = 13f
    private val LOCATION_PERMISSION = 100
    private val DEFAULT_LAT = 20.6737777
    private val DEFAULT_LNG = -103.4054534

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPresenter = MainActivityPresenterKt(this)
        mPresenter?.onCreate()

        setupToolbar()
        setupConnection()
        mSlidePanel = findViewById(R.id.slideup_panel)
        setupSwipeButton()
    }

    override fun onConnected(bundle: Bundle?) {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        checkForLocationPermissions()

        if (mLocationPermissionGranted) {
            updateLocationUI()
        }

        mGoogleMap?.setOnMapClickListener { mGoogleMap?.clear() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_empty_list -> {
                mPresenter?.cleanAddressesList()
                mGoogleMap?.clear()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Initializing swipe button
     */
    private fun setupSwipeButton() {
        val swipeButton = findViewById<SwipeButton>(R.id.my_swipe_button)
        val swipeButtonSettings = object : SwipeButtonCustomItems() {
            override fun onSwipeConfirm() {
                onSaveLastKnownLocation()
            }
        }
        swipeButtonSettings
                .setButtonPressText("Saving location")
                .setGradientColor1(-0x777778)
                .setGradientColor2(-0x99999a)
                .setGradientColor2Width(60)
                .setGradientColor3(-0xcccccd)
                .setPostConfirmationColor(-0x777778)
                .setActionConfirmDistanceFraction(0.7).actionConfirmText = "Swipe to save"
        swipeButton.setSwipeButtonCustomItems(swipeButtonSettings)
    }

    /**
     * Initialize the Toolbar settings
     */
    private fun setupToolbar() {
        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        mToolbar?.let {
            setSupportActionBar(it)
            title = "Geo Project"
        }
    }

    /**
     * Initialize the GoogleClient Connection
     */
    private fun setupConnection() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build()
        mGoogleApiClient?.connect()
    }

    /**
     * Check if the app has permissions to get the last know devise location
     */
    private fun checkForLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mLocationPermissionGranted = true
        else
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        mLocationPermissionGranted = false
        when (requestCode) {
            LOCATION_PERMISSION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                mLocationPermissionGranted = true
        }
        updateLocationUI()
    }

    /**
     * Enable the option to show the current location of the device in the map
     */
    private fun updateLocationUI() {
        try {
            if (mLocationPermissionGranted) {
                if (mLocationClient == null) {
                    mLocationClient = LocationServices.getFusedLocationProviderClient(this)
                }
                mGoogleMap?.let {
                    it.isMyLocationEnabled = true
                    it.uiSettings.isMyLocationButtonEnabled = true
                }
                getDeviceLocation()
            } else {
                mGoogleMap?.let {
                    it.isMyLocationEnabled = true
                    it.uiSettings.isMyLocationButtonEnabled = false
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
    }

    /**
     * Get device known last location in mLastKnownLocation
     */
    private fun getDeviceLocation() {
        try {
            mLocationClient?.lastLocation?.addOnSuccessListener(this) {
                if (it != null) {
                    mLastKnownLocation = it
                    updateCameraPosition(LatLng(it.latitude, it.longitude))
                } else {
                    Log.d("Location Error", "Current location is null. Using defaults.")
                    showToast("Location not available")
                    mGoogleMap?.uiSettings?.isMyLocationButtonEnabled = false
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }

    }

    /**
     * Update in the map the camera position
     */
    private fun updateCameraPosition(latLng: LatLng) {
        val cameraPosition = CameraPosition(latLng, CAMERA_ZOOM, 0f, 0f)
        mGoogleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    /**
     * It add in the database the last know location
     */
    fun onSaveLastKnownLocation() {
        if (mLocationPermissionGranted) {
            getDeviceLocation()
        }
        if (mLastKnownLocation != null)
            mPresenter?.onSaveAddress(mLastKnownLocation)
        else
            showToast("No location detected")
    }

    override fun onAddressesList(realmResults: RealmResults<AddressModel>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = AddressAdapter(this, realmResults, supportFragmentManager)

        if (realmResults.isEmpty())
            showToast("No addresses in the database")

        recyclerView.adapter = mAdapter
    }

    override fun onAddedAddressSuccess(message: String) {
        mAdapter?.notifyDataSetChanged()
        showToast(message)
    }

    override fun onAddAddressError(message: String) {
        showToast(message)
    }

    override fun onDeleteAddressesSuccess(message: String) {
        mAdapter?.notifyDataSetChanged()
        showToast(message)
    }

    override fun onDeleteAddressesError(message: String) {
        showToast(message)
    }

    override fun onAddressSelected(address: AddressModel) {
        hideSlideUpPanel()
        mGoogleMap?.clear()
        val markerPosition = LatLng(address.latitude ?: DEFAULT_LAT, address.longitude ?: DEFAULT_LNG)
        mGoogleMap?.addMarker(MarkerOptions().position(markerPosition))?.title = address.street
        updateCameraPosition(markerPosition)
    }

    override fun onConnectionSuspended(i: Int) {
        showToast("Connection was suspended")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        showToast("Google Maps Failed")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        mPresenter?.onDestroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (mSlidePanel?.isOverlayed == true)
            hideSlideUpPanel()
        else
            super.onBackPressed()
    }

    private fun hideSlideUpPanel() {
        mSlidePanel?.let{
            it.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
    }
}