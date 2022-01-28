package vn.noname.vaccineassistant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

public class MainActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMainBinding binding;
    LatLng userLatLong;
    FusedLocationProviderClient fusedLocationProviderClient;
    private LinearLayout layoutBottomDescription;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView des_name;



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


    }

    private void checkLocationPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    getLocation();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                LatLng location = new LatLng(10.778545, 106.679506);
                mMap.addMarker(new MarkerOptions().position(location).title("Marker in HCM city").visible(false)).setTag("hello");
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 20f));

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
                if(location == null){
                    alert();
                }else{
                mMap.setMyLocationEnabled(true);
                userLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(userLatLong).title("Your location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLong, 15f));}

        });

    }
    private void alert(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Thông báo")
                .setMessage("Cần bật định vị để xác định vị trí của bạn. Bật sau khi ấn đồng ý.")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        dialogInterface.dismiss();
                    }
                })
                .create();
        alertDialog.show();
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
                mMap.addMarker(new MarkerOptions().position(placeList.get(i).getLatLong()).title(placeList.get(i).getName()).icon(BitmapDescriptorFactory.defaultMarker((float) 245.0)));
            }

            if(placeList.get(i).getRequire()==1){
                mMap.addMarker(new MarkerOptions().position(placeList.get(i).getLatLong()).title(placeList.get(i).getName()).icon(BitmapDescriptorFactory.defaultMarker((float) 78.0)));
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

}
