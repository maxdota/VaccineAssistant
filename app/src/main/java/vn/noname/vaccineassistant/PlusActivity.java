package vn.noname.vaccineassistant;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

import vn.noname.vaccineassistant.base.BaseActivity;
import vn.noname.vaccineassistant.model.VaccinePlace;

public class PlusActivity extends BaseActivity {

    String[] items = {"Abdala","AstraZeneca", "Moderna","Pfizer","Vero Cell"};
    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView date_picker;
    ArrayAdapter<String> adapterItems;
     String item;
    int date;
    int month ;
    int year ;
    public static int checkCount=0;
    Button save_id;
    private ArrayList<VaccinePlace> vaccinePlaces;
    ArrayList<Pair<Float, Integer>> distanceList;




    @Override
    public int currentScreen() {
        return PLUS_SCREEN;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);
        initBottomNav();
        
        vaccinePlaces = MainActivity.VaccinePlaces;
        save_id = findViewById(R.id.save_id);
        autoCompleteTextView = findViewById(R.id.auto_complete);
        date_picker = findViewById(R.id.recentDate);
        autoCompleteTextView.setInputType(InputType.TYPE_NULL);
        date_picker.setInputType(InputType.TYPE_NULL);

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });


        adapterItems = new ArrayAdapter<String>(this, R.layout.list_vaccine, items);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();

            }
        });

        save_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //distanceList = checkDistance(vaccinePlaces);
                if(TextUtils.isEmpty(item)|| TextUtils.isEmpty(date_picker.getText())){
                    Toast.makeText(PlusActivity.this, "Cần điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    checkCount++;
                    Intent intent = new Intent(PlusActivity.this, ListActivity.class);
                    intent.putExtra("Vaccine", item);
                    /*intent.putExtra("checkCount", 5);*/
                    startActivity(intent);
                    
                    // overridePendingTransition to remove animation between 2 activities
                    overridePendingTransition(0, 0);

                }
            }


        });
    }

    private void setDate() {
        final Calendar calendar = Calendar.getInstance();
        int date1 = calendar.get(Calendar.DATE);
        int month1 = calendar.get(Calendar.MONTH);
        int year1 = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                date = i2;
                month = i1;
                year = i;
                calendar.set(i,i1,i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                date_picker.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year1, month1,date1);
        datePickerDialog.show();
    }





}
