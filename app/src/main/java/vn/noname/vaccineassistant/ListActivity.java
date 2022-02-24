package vn.noname.vaccineassistant;

import static vn.noname.vaccineassistant.model.VaccinePlace.PLACE_TYPE_CLOTHES_SUPPORT;
import static vn.noname.vaccineassistant.model.VaccinePlace.PLACE_TYPE_FOOD_SUPPORT;
import static vn.noname.vaccineassistant.model.VaccinePlace.PLACE_TYPE_HELP_SUPPORT;
import static vn.noname.vaccineassistant.model.VaccinePlace.PLACE_TYPE_VACCINE;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import vn.noname.vaccineassistant.base.BaseActivity;
import vn.noname.vaccineassistant.listener.FirebaseListener;
import vn.noname.vaccineassistant.model.VaccinePlace;

public class ListActivity extends BaseActivity {
    @Override
    public int currentScreen() {
        return LIST_SCREEN;
    }
    ListView listView;
    private ArrayList<VaccinePlace> placeDst = new ArrayList<>();
    private ArrayList<VaccinePlace> Placetype = new ArrayList<>();
    private  ArrayList<VaccinePlace> vaccinePlaces = new ArrayList<>();
    ListAdapter listAdapter;
    private ArrayList<Float> distance=new ArrayList<>();
    private Button all_list, vaccine_list, food_list, clothes_list, help_list;

    private FirebaseListener<ArrayList<VaccinePlace>> firebaseListener = new FirebaseListener<ArrayList<VaccinePlace>>() {
        @Override
        public void onDataUpdated(ArrayList<VaccinePlace> data) {

            listAdapter.clear();
            if( getIntent().getStringExtra("Vaccine") != null ){
               for(int i = 0; i < data.size();i++){
                    VaccinePlace place = data.get(i);
                    if(getIntent().getStringExtra("Vaccine").equals(place.vaccine)){
                        if(MainActivity.userLatLong != null){
                            float[] result = new float[1];
                            Location.distanceBetween(MainActivity.userLatLong.latitude, MainActivity.userLatLong.longitude, place.latitude, place.longitude, result);
                            distance.add(result[0]);
                            placeDst.add(data.get(i));
                        }else {

                            float[] result = new float[1];
                            Location.distanceBetween(10.778545, 106.679506, place.latitude, place.longitude, result);
                            distance.add(result[0]);

                            placeDst.add(data.get(i));
                        }
                    }
                }
                placeDst = placeDistance( distance);
                listAdapter.addAll(placeDst);
            }
            else {
                for(int i = 0; i < data.size();i++){
                    VaccinePlace place = data.get(i);
                    if(MainActivity.userLatLong != null){
                            float[] result = new float[1];
                            Location.distanceBetween(MainActivity.userLatLong.latitude, MainActivity.userLatLong.longitude, place.latitude, place.longitude, result);
                            distance.add(result[0]);
                            placeDst.add(data.get(i));
                    }else {

                            float[] result = new float[1];
                            Location.distanceBetween(10.778545, 106.679506, place.latitude, place.longitude, result);
                            distance.add(result[0]);

                            placeDst.add(data.get(i));
                        }

                }
                placeDst = placeDistance( distance);
                listAdapter.addAll(placeDst);
            }
        }

        @Override
        public void onError(String message, int errorCode) {
            Toast.makeText(ListActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initBottomNav();
        listView = findViewById(R.id.list_view);
        all_list = findViewById(R.id.all_list);
        vaccine_list= findViewById(R.id.vaccine_list);
        food_list = findViewById(R.id.food_list);
        clothes_list = findViewById(R.id.clothes_list);
        help_list = findViewById(R.id.help_list);

        addControls();
        clothes_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(placeDst.size()>0){
                    addPlace(PLACE_TYPE_CLOTHES_SUPPORT);
                }
                updateFilterUi(view);
            }
        }
        );

        all_list.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if(placeDst.size()>0){
                                                    listAdapter.clear();
                                                    listAdapter.addAll(placeDst);
                                                    addControls();
                                                }
                                                updateFilterUi(view);
                                            }
                                        }
        );
        vaccine_list.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             if(placeDst.size()>0){
                                                 addPlace(PLACE_TYPE_VACCINE);
                                             }
                                             updateFilterUi(view);
                                         }
                                     }
        );
        food_list.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if(placeDst.size()>0){
                                                    addPlace(PLACE_TYPE_FOOD_SUPPORT);
                                                }
                                                updateFilterUi(view);
                                            }
                                        }
        );
        help_list.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if(placeDst.size()>0){
                                                    addPlace(PLACE_TYPE_HELP_SUPPORT);
                                                }
                                                updateFilterUi(view);
                                            }
                                        }
        );
    }

    private void updateFilterUi(View view) {
        resetFilterUiToUnselected();
        setFilterUiToSelected(view);
    }

    private void setFilterUiToSelected(View view) {
        view.setBackgroundResource(R.drawable.selected_each_item);
    }

    private void resetFilterUiToUnselected() {
        all_list.setBackgroundResource(R.drawable.each_item);
        vaccine_list.setBackgroundResource(R.drawable.each_item);
        food_list.setBackgroundResource(R.drawable.each_item);
        clothes_list.setBackgroundResource(R.drawable.each_item);
        help_list.setBackgroundResource(R.drawable.each_item);
    }

    private void addControls() {
        listAdapter = new ListAdapter(ListActivity.this, R.layout.list_item, vaccinePlaces);
        listView.setAdapter(listAdapter);
    }


    private void addPlace(String type) {
        listAdapter.clear();
        Placetype.clear();
        for(int i=0; i< placeDst.size(); i++){
            if(!TextUtils.isEmpty(placeDst.get(i).placeType)){
            if(((placeDst.get(i)).placeType).equals(type)){
                Placetype.add(placeDst.get(i));
            }

        }}
        listAdapter.addAll(Placetype);
        listAdapter = new ListAdapter(ListActivity.this, R.layout.list_item, vaccinePlaces);
        listView.setAdapter(listAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataCenter.getInstance().addVaccinePlaceListener(firebaseListener);
    }

    @Override
    protected void onPause() {
        DataCenter.getInstance().removeVaccinePlaceListener(firebaseListener);
        super.onPause();
    }

    private ArrayList<VaccinePlace> placeDistance( ArrayList<Float> distance){
        VaccinePlace trk;
        Float trk1;
        for (int i=0; i<distance.size(); i++){
            for (int j=i+1; j<distance.size(); j++){
                if(distance.get(i) > distance.get(j)){
                    trk1 = distance.get(i);
                    distance.set(i, distance.get(j));
                    distance.set(j,trk1);
                    trk= placeDst.get(i);
                    placeDst.set(i, placeDst.get(j));
                    placeDst.set(j, trk);
                }
            }
        }
        return placeDst;
    }


}
