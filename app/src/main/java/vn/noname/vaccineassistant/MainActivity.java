package vn.noname.vaccineassistant;

import static vn.noname.vaccineassistant.R.drawable.vaccine_green;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Arrays;

import vn.noname.vaccineassistant.base.BaseActivity;
import vn.noname.vaccineassistant.databinding.ActivityMainBinding;
import vn.noname.vaccineassistant.listener.FirebaseListener;
import vn.noname.vaccineassistant.model.VaccinePlace;

public class MainActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener ,
        GoogleMap.OnInfoWindowClickListener{
    private static final String TAG = "MainActivityFlow";

    private GoogleMap mMap;
    private ActivityMainBinding binding;
    public  static LatLng userLatLong ;
    FusedLocationProviderClient fusedLocationProviderClient;
    private RelativeLayout layoutBottomDescription;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView des_name;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public static ArrayList<VaccinePlace> VaccinePlaces = new ArrayList<>();

    private FirebaseListener<ArrayList<VaccinePlace>> firebaseListener = new FirebaseListener<ArrayList<VaccinePlace>>() {
        @Override
        public void onDataUpdated(ArrayList<VaccinePlace> data) {

            loadMapData(data);

        }

        @Override
        public void onError(String message, int errorCode) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public int currentScreen() {
        return MAP_SCREEN;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initBottomNav();
        layoutBottomDescription = findViewById(R.id.bottom_description);
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomDescription);
        des_name = findViewById(R.id.des_name);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }




       fusedLocationProviderClient = new FusedLocationProviderClient(this);
        /*if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            getLocation();
        }else {
            LatLng location = new LatLng(10.778545, 106.679506);
            mMap.addMarker(new MarkerOptions().position(location).title("Marker in HCM city").icon(BitmapDescriptorFactory.defaultMarker((float) 197.0)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));

        }*/
        checkLocationPermission();
        if(getIntent().getStringExtra("placeaddress") != null){
            addNewMarker(getIntent());
        }


    }

    private void addNewMarker(Intent data) {
        Log.d("Thử lần 1", data.getStringExtra("placeaddress"));
        if(data.getStringExtra("placeaddress") != null){
            Log.d("Thử lần 1", data.getStringExtra("placeaddress"));
            LatLng location = new LatLng(data.getDoubleExtra("placelong",0), data.getDoubleExtra("placelat",0));
            String address = data.getStringExtra("placeaddress");
            String name = data.getStringExtra("placename");
            String id = data.getStringExtra("placeid");
            if(id.equals("Cứu trợ")){
                mMap.addMarker(new MarkerOptions().position(location).title(name).icon(BitmapDescriptorFactory.fromAsset("vaccine_red.png")));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18f));
            }
            if (id.equals("Cung cấp quần áo")){
                mMap.addMarker(new MarkerOptions().position(location).title(name).icon(BitmapDescriptorFactory.fromAsset("clothes.png")));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18f));
            }
            if (id.equals("Cung cấp đồ ăn")){
                mMap.addMarker(new MarkerOptions().position(location).title(name).icon(BitmapDescriptorFactory.fromAsset("food.png")));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18f));
            }
        }
    }


    private void checkLocationPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                getLocation();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                useSampleLocation();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();

            }
        }).check();
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            if (location == null) {
                if (isLocationEnabled(this)) {
                    updateUserLocation();
                } else {
                    alert();
                }
            } else {
                setUserLocationAndMap(location);
            }
        });
    }

    @SuppressWarnings("deprecation")
    public static Boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is a new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
            // This was deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }

    private void updateUserLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)        // 10 seconds, in milliseconds
                .setFastestInterval(100); // 1 second, in milliseconds
        mGoogleApiClient.connect();
    }

    @SuppressLint("MissingPermission")
    private void setUserLocationAndMap(Location location) {
        mMap.setMyLocationEnabled(true);
        setUserLatLongAndMap(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    private void useSampleLocation() {
        setUserLatLongAndMap(new LatLng(10.778545, 106.679506));
    }

    private void setUserLatLongAndMap(LatLng latLong) {
        userLatLong = latLong;
        mMap.addMarker(new MarkerOptions().position(userLatLong).title("Tạo địa điểm mới").draggable(true)).setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLong, 18f));
    }

    private void alert(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Không xác định được vị trí của bạn")
                .setMessage("Vui lòng bật định vị và thử lại")
                .setPositiveButton("Thử Lại", (dialogInterface, i) -> getLocation())
                .setNegativeButton("Bỏ Qua", (dialog, which) -> useSampleLocation())
                .create();
        alertDialog.show();
    }

    private void loadMapData(ArrayList<VaccinePlace> vaccinePlaces) {
        mMap.clear();
        for(int i = 0; i < vaccinePlaces.size();i++){
            VaccinePlaces.add(vaccinePlaces.get(i));
            VaccinePlace place = vaccinePlaces.get(i);
            if(!place.isRequired()){
                Marker marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(place.getLatLong())
                                .title(place.name)
                                .icon(BitmapDescriptorFactory.fromAsset("vaccine_green.png")));
            }

            if(place.isRequired()){
                mMap.addMarker(
                        new MarkerOptions()
                                .position(place.getLatLong())
                                .title(place.name)
                                .icon(BitmapDescriptorFactory.fromAsset("vaccine_red.png")));
            }
        }

        mMap.setOnMarkerClickListener(marker -> {
            String name = marker.getTitle();
            des_name.setText(name);
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
            return false;
        });
        if (userLatLong != null) {
            setUserLatLongAndMap(userLatLong);
        }
    }

    private void setupMap() {
        ArrayList<PlaceModel> placeList = new ArrayList<>(
                Arrays.asList(
                        new PlaceModel("TRK1", 0, new LatLng(10.778265, 106.679647)),
                        new PlaceModel("TRK2", 0, new LatLng(10.778219, 106.679120)),
                        new PlaceModel("TRK3", 1, new LatLng(10.778856, 106.679100)),
                        new PlaceModel("TRK4", 1, new LatLng(10.779204, 106.679725)),
                        new PlaceModel("TRK5", 1, new LatLng(10.778780, 106.680430))

                )
        );
        for(int i = 0; i < placeList.size();i++){
            if(placeList.get(i).getRequire()==0){
                mMap.addMarker(new MarkerOptions().position(placeList.get(i).getLatLong()).title(placeList.get(i).getName()).icon(BitmapDescriptorFactory.fromAsset("vaccine_red.png")));
            }

            if(placeList.get(i).getRequire()==1){
                mMap.addMarker(new MarkerOptions().position(placeList.get(i).getLatLong()).title(placeList.get(i).getName()).icon(BitmapDescriptorFactory.fromAsset("vaccine_red.png")));
            }
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String name = marker.getTitle();
                des_name.setText(name);
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                }

                return false;
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        DataCenter.getInstance().addVaccinePlaceListener(firebaseListener);

        mMap.setOnInfoWindowClickListener(this);
//        setupMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        if (mMap != null) {
            DataCenter.getInstance().addVaccinePlaceListener(firebaseListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        disconnectLocationService();
        DataCenter.getInstance().removeVaccinePlaceListener(firebaseListener);
    }

    private void disconnectLocationService() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            setUserLocationAndMap(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Location services onConnectionFailed");
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONNECTION_FAILURE_RESOLUTION_REQUEST && resultCode == RESULT_OK) {
            getLocation();
        }
        if(requestCode == 100 && resultCode == RESULT_OK){
            addNewMarker(data);
        }
    }

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 101;

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.i(TAG, "onLocationChanged: " + location.toString());
        disconnectLocationService();
        mGoogleApiClient = null;
        setUserLocationAndMap(location);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        if(marker.getTitle().equals("Tạo địa điểm mới")){}
        Intent i = new Intent(MainActivity.this, PlusLocationActivity.class);
        i.putExtra("Lat", marker.getPosition().latitude);
        i.putExtra("Long",marker.getPosition().longitude);
        startActivityForResult(i, 100);


    }


}
