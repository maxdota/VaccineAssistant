package vn.noname.vaccineassistant;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import vn.noname.vaccineassistant.listener.FirebaseListener;
import vn.noname.vaccineassistant.model.VaccinePlace;

public class DataCenter {
    private static DataCenter sInstance;
    private ArrayList<FirebaseListener<ArrayList<VaccinePlace>>> vaccinePlacesListeners = new ArrayList<>();
    private ArrayList<VaccinePlace> vaccinePlaces = new ArrayList<>();
    private final Object vaccinePlaceLock = new Object();

    private DatabaseReference placeFirebaseRef;

    public static DataCenter getInstance() {
        if (sInstance == null) {
            sInstance = new DataCenter();
        }
        return sInstance;
    }

    private DataCenter() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        placeFirebaseRef = database.getReference("places");
        placeFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<VaccinePlace>> type =
                        new GenericTypeIndicator<ArrayList<VaccinePlace>>() {};
                ArrayList<VaccinePlace> value = dataSnapshot.getValue(type);
                if (value == null) {
                    Log.d(TAG, "Value is null!");
                    triggerErrorVaccinePlaceListeners("Value is null!", 0);
                } else {
                    Log.d(TAG, "Value is: " + value.size());
                    triggerDataVaccinePlaceListeners(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                triggerErrorVaccinePlaceListeners("Failed to read from Firebase: " + error.getMessage(), 0);
            }
        });
    }

    public void addPlace(VaccinePlace place) {
        synchronized (vaccinePlaceLock) {
            vaccinePlaces.add(place);
            placeFirebaseRef.setValue(vaccinePlaces, (error, ref) -> {
                Log.d(TAG, "Add place completed, error: " + error);
            });
        }
    }

    public void triggerDataVaccinePlaceListeners(ArrayList<VaccinePlace> value) {
        synchronized (vaccinePlaceLock) {
            vaccinePlaces.clear();
            vaccinePlaces.addAll(value);
            for (FirebaseListener<ArrayList<VaccinePlace>> listener : vaccinePlacesListeners) {
                listener.onDataUpdated(vaccinePlaces);
            }
        }
    }

    public void triggerErrorVaccinePlaceListeners(String message, int errorCode) {
        synchronized (vaccinePlaceLock) {
            for (FirebaseListener<ArrayList<VaccinePlace>> listener : vaccinePlacesListeners) {
                listener.onError(message, errorCode);
            }
        }
    }

    public void addVaccinePlaceListener(FirebaseListener<ArrayList<VaccinePlace>> listener) {
        synchronized (vaccinePlaceLock) {
            if (!vaccinePlacesListeners.contains(listener)) {
                vaccinePlacesListeners.add(listener);
                listener.onDataUpdated(vaccinePlaces);
            }
        }
    }

    public void removeVaccinePlaceListener(FirebaseListener<ArrayList<VaccinePlace>> listener) {
        synchronized (vaccinePlaceLock) {
            vaccinePlacesListeners.remove(listener);
        }
    }

    private static final String TAG = "DataCenter";
}
