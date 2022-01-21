package vn.noname.vaccineassistant;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import vn.noname.vaccineassistant.base.BaseActivity;
import vn.noname.vaccineassistant.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMainBinding binding;

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

        // Add a marker in HCM city and move the camera
        LatLng location = new LatLng(10.778545, 106.679506);
        mMap.addMarker(new MarkerOptions().position(location).title("Marker in HCM city"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));
    }
}
