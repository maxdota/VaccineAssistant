package vn.noname.vaccineassistant;

import android.os.Bundle;
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
    ArrayList<Place> placeList;
    private ArrayList<VaccinePlace> vaccinePlaces = new ArrayList<>();
    ListAdapter listAdapter;

    private FirebaseListener<ArrayList<VaccinePlace>> firebaseListener = new FirebaseListener<ArrayList<VaccinePlace>>() {
        @Override
        public void onDataUpdated(ArrayList<VaccinePlace> data) {
            listAdapter.clear();
            listAdapter.addAll(data);
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

        addControls();
//        addEvent();
    }

    private void addControls() {
        listView = (ListView) findViewById(R.id.list_view);
        placeList = new ArrayList<>();
        listAdapter = new ListAdapter(ListActivity.this, R.layout.list_item, vaccinePlaces);
        listView.setAdapter(listAdapter);
    }
    private void addEvent() {
        createData();
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

    private void createData() {
        Place place = new Place("Bệnh viện Bạch Mai", "Địa chỉ: 78 Đường Giải Phóng, Phương Đình,  Đống Đa, Hà Nội", "Mở cửa: 7:00", "Đóng cửa: 17:00", "Loại vaccine: Pfizer",
                "Độ tuổi: >13", "Khu vực: Không giới hạn", "Phí: Miễn Phí");
        placeList.add(place);
        place = new Place("Bệnh viện Bạch Mai", "Địa chỉ: 78 Đường Giải Phóng, Phương Đình,  Đống Đa, Hà Nội", "Mở cửa: 7:00", "Đóng cửa: 17:00", "Loại vaccine: Pfizer",
                "Độ tuổi: >13", "Khu vực: Không giới hạn", "Phí: Miễn Phí");
        placeList.add(place);
        place = new Place("Bệnh viện Bạch Mai", "Địa chỉ: 78 Đường Giải Phóng, Phương Đình,  Đống Đa, Hà Nội", "Mở cửa: 7:00", "Đóng cửa: 17:00", "Loại vaccine: Pfizer",
                "Độ tuổi: >13", "Khu vực: Không giới hạn", "Phí: Miễn Phí");
        placeList.add(place);
        place = new Place("Bệnh viện Bạch Mai", "Địa chỉ: 78 Đường Giải Phóng, Phương Đình,  Đống Đa, Hà Nội", "Mở cửa: 7:00", "Đóng cửa: 17:00", "Loại vaccine: Pfizer",
                "Độ tuổi: >13", "Khu vực: Không giới hạn", "Phí: Miễn Phí");
        placeList.add(place);
        place = new Place("Bệnh viện Bạch Mai", "Địa chỉ: 78 Đường Giải Phóng, Phương Đình,  Đống Đa, Hà Nội", "Mở cửa: 7:00", "Đóng cửa: 17:00", "Loại vaccine: Pfizer",
                "Độ tuổi: >13", "Khu vực: Không giới hạn", "Phí: Miễn Phí");
        placeList.add(place);
        place = new Place("Bệnh viện Bạch Mai", "Địa chỉ: 78 Đường Giải Phóng, Phương Đình,  Đống Đa, Hà Nội", "Mở cửa: 7:00", "Đóng cửa: 17:00", "Loại vaccine: Pfizer",
                "Độ tuổi: >13", "Khu vực: Không giới hạn", "Phí: Miễn Phí");
        placeList.add(place);
        place = new Place("Bệnh viện Bạch Mai", "Địa chỉ: 78 Đường Giải Phóng, Phương Đình,  Đống Đa, Hà Nội", "Mở cửa: 7:00", "Đóng cửa: 17:00", "Loại vaccine: Pfizer",
                "Độ tuổi: >13", "Khu vực: Không giới hạn", "Phí: Miễn Phí");
        placeList.add(place);
        place = new Place("Bệnh viện Bạch Mai", "Địa chỉ: 78 Đường Giải Phóng, Phương Đình,  Đống Đa, Hà Nội", "Mở cửa: 7:00", "Đóng cửa: 17:00", "Loại vaccine: Pfizer",
                "Độ tuổi: >13", "Khu vực: Không giới hạn", "Phí: Miễn Phí");
        placeList.add(place);
        listAdapter.notifyDataSetChanged();
    }

}
