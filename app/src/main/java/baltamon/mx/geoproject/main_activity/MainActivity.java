package baltamon.mx.geoproject.main_activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import baltamon.mx.geoproject.R;
import baltamon.mx.geoproject.adapters.AddressesRecyclerAdapter;
import baltamon.mx.geoproject.models.AddressModel;
import baltamon.mx.geoproject.utilities.SwipeButton;
import baltamon.mx.geoproject.utilities.SwipeButtonCustomItems;
import io.realm.RealmResults;

@Deprecated
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        MainActivityView {

    private MainActivityPresenter mPresenter;

    private AddressesRecyclerAdapter mAdapter;

    private SlidingUpPanelLayout mSlidePanel;

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private static final float CAMERA_ZOOM = 13;
    private static final int LOCATION_PERMISSION = 100;

    private FusedLocationProviderClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainActivityPresenter(this);
        mPresenter.onCreate();

        setupToolbar();
        setupConnection();
        mSlidePanel = findViewById(R.id.slideup_panel);
        setupSwipeButton();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        checkForLocationPermissions();
        if (mLocationPermissionGranted) {
            updateLocationUI();
        }
        mGoogleMap.setOnMapClickListener(latLng -> mGoogleMap.clear());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tb_empty_list:
                mPresenter.cleanAddressesList();
                mGoogleMap.clear();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initializing swipe button
     */
    public void setupSwipeButton() {
        SwipeButton swipeButton = findViewById(R.id.my_swipe_button);
        SwipeButtonCustomItems swipeButtonSettings = new SwipeButtonCustomItems() {
            @Override
            public void onSwipeConfirm() {
                onSaveLastKnownLocation();
            }
        };
        swipeButtonSettings
                .setButtonPressText("Saving location")
                .setGradientColor1(0xFF888888)
                .setGradientColor2(0xFF666666)
                .setGradientColor2Width(60)
                .setGradientColor3(0xFF333333)
                .setPostConfirmationColor(0xFF888888)
                .setActionConfirmDistanceFraction(0.7)
                .setActionConfirmText("Swipe to save");
        swipeButton.setSwipeButtonCustomItems(swipeButtonSettings);
    }

    /**
     * Initialize the Toolbar settings
     */
    public void setupToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null)
            mActionBar.setTitle("Geo Project");
    }

    /**
     * Initialize the GoogleClient Connection
     */
    private void setupConnection() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Check if the app has permissions to get the last know devise location
     */
    public void checkForLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mLocationPermissionGranted = true;
        else
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    mLocationPermissionGranted = true;
                break;
        }
        updateLocationUI();
    }

    /**
     * Enable the option to show the current location of the device in the map
     */
    private void updateLocationUI() {
        if (mGoogleMap == null)
            return;
        try {
            if (mLocationPermissionGranted) {
                if (mLocationClient == null)
                    mLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                getDeviceLocation();
            } else {
                mGoogleMap.setMyLocationEnabled(false);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Get device known last location in mLastKnownLocation
     */
    private void getDeviceLocation() {
        try {
            mLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    mLastKnownLocation = location;
                    updateCameraPosition(new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()));
                } else {
                    Log.d("Location Error", "Current location is null. Using defaults.");
                    showToast("Location not available");
                    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Update in the map the camera position
     */
    private void updateCameraPosition(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition(latLng, CAMERA_ZOOM, 0, 0);
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * It add in the database the last know location
     */
    public void onSaveLastKnownLocation() {
        if (mLocationPermissionGranted) getDeviceLocation();
        if (mLastKnownLocation != null)
            mPresenter.onSaveAddress(mLastKnownLocation);
        else
            showToast("No location detected");
    }

    @Override
    public void onAddressesList(RealmResults<AddressModel> realmResults) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AddressesRecyclerAdapter(this, realmResults, getSupportFragmentManager());

        if (realmResults.isEmpty())
            showToast("No addresses in the database");

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onAddedAddressSuccess(String message) {
        mAdapter.notifyDataSetChanged();
        showToast(message);
    }

    @Override
    public void onAddAddressError(String message) {
        showToast(message);
    }

    @Override
    public void onDeleteAddressesSuccess(String message) {
        mAdapter.notifyDataSetChanged();
        showToast(message);
    }

    @Override
    public void onDeleteAddressesError(String message) {
        showToast(message);
    }

    @Override
    public void onAddressSelected(AddressModel address) {
        hideSlideUpPanel();
        mGoogleMap.clear();
        LatLng markerPosition = new LatLng(address.getLatitude(),
                address.getLongitude());
        mGoogleMap.addMarker(new MarkerOptions().position(markerPosition)).
                setTitle(address.getStreet());
        updateCameraPosition(markerPosition);
    }

    @Override
    public void onConnectionSuspended(int i) {
        showToast("Connection was suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showToast("Google Maps Failed");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mSlidePanel.isOverlayed())
            hideSlideUpPanel();
        else
            super.onBackPressed();
    }

    private void hideSlideUpPanel() {
        mSlidePanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }
}