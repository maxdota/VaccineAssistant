package vn.noname.vaccineassistant;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLatLong;

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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
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
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                userLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(userLatLong).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLong, 15f));


            }
        };
        // asking permission
        askLocationPermission();
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
                mMap.addMarker(new MarkerOptions().position(placeList.get(i).getLatLong()).title(placeList.get(i).getName()));
            }

            if(placeList.get(i).getRequire()==1){
                mMap.addMarker(new MarkerOptions().position(placeList.get(i).getLatLong()).title(placeList.get(i).getName()).icon(BitmapDescriptorFactory.defaultMarker((float) 78.0)));
            }
        }

    }

    private void askLocationPermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                     return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                // get user last location to set marker
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                userLatLong = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(userLatLong).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker((float) 197.0)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLong, 15f));



            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                LatLng location = new LatLng(10.778545, 106.679506);
                mMap.addMarker(new MarkerOptions().position(location).title("Marker in HCM city").icon(BitmapDescriptorFactory.defaultMarker((float) 197.0)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();

            }
        }).check();
    }
}
