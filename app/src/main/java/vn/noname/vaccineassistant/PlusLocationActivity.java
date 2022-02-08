package vn.noname.vaccineassistant;

import static vn.noname.vaccineassistant.model.VaccinePlace.PLACE_TYPE_CLOTHES_SUPPORT;
import static vn.noname.vaccineassistant.model.VaccinePlace.PLACE_TYPE_FOOD_SUPPORT;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import vn.noname.vaccineassistant.base.BaseActivity;
import vn.noname.vaccineassistant.model.VaccinePlace;

public class PlusLocationActivity extends BaseActivity {

    String[] items = {"Cứu trợ","Cung cấp quần áo", "Cung cấp đồ ăn"};
    String item;
    int itemIndex;
    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView date_picker;
    ArrayAdapter<String> adapterItems;
    private EditText nameLocation,addressLocation, openLocation, payLocation, stfLocation, closeLocation, age_above, age_below;
    Button save_id;
    VaccinePlace place= new VaccinePlace();


    @Override
    public int currentScreen() {
        return PLUS_SCREEN;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_location);
        initBottomNav();
        autoCompleteTextView = findViewById(R.id.auto_complete1);

        nameLocation = findViewById(R.id.nameLocation);
        addressLocation = findViewById(R.id.addressLocation);
        openLocation = findViewById(R.id.openLocation);
        closeLocation = findViewById(R.id.closeLocation);
        payLocation = findViewById(R.id.payLocation);
        stfLocation = findViewById(R.id.stfLocation);
        age_above = findViewById(R.id.age_limit_above);
        age_below = findViewById(R.id.age_limit_below);
        save_id = findViewById(R.id.save_location);

        payLocation.setInputType(InputType.TYPE_CLASS_NUMBER);
        age_below.setInputType(InputType.TYPE_CLASS_NUMBER);
        age_above.setInputType(InputType.TYPE_CLASS_NUMBER);
        autoCompleteTextView.setInputType(InputType.TYPE_NULL);
        openLocation.setInputType(InputType.TYPE_NULL);
        closeLocation.setInputType(InputType.TYPE_NULL);

        double lat = getIntent().getDoubleExtra("Lat",0);
        double longtitude = getIntent().getDoubleExtra("Long", 0);


        openLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minus = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(PlusLocationActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                                calendar.set(0,0,0, i, i1);
                                openLocation.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        },
                        hour,
                        minus,
                        true);
                timePickerDialog.show();

            }
        });

        closeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minus = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(PlusLocationActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                                calendar.set(0,0,0, i, i1);
                                closeLocation.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        }, hour, minus, true);
                timePickerDialog.show();

            }
        });

        save_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                place.longitude = longtitude;
                place.latitude = lat;
                place.name = nameLocation.getText().toString();
                place.address = addressLocation.getText().toString();
                place.openingTime = openLocation.getText().toString();
                place.closingTime = closeLocation.getText().toString();

                try {
                    String feeString = payLocation.getText().toString();
                    place.fee = feeString.isEmpty() ? 0 : Long.parseLong(feeString);

                    String ageAboveString = age_above.getText().toString();
                    place.ageLimitAbove = ageAboveString.isEmpty() ? 0 : Integer.parseInt(ageAboveString);

                    String ageBelowString = age_below.getText().toString();
                    place.ageLimitBelow = ageBelowString.isEmpty() ? 0 : Integer.parseInt(ageBelowString);
                } catch (Exception ex) {
                    Toast.makeText(PlusLocationActivity.this, "Vui lòng điền Tiền phí, Độ tuổi là dạng số (hoặc để trống)", Toast.LENGTH_LONG).show();
                    return;
                }

                if (place.ageLimitAbove > place.ageLimitBelow) {
                    Toast.makeText(PlusLocationActivity.this, "Tuổi trên phải nhỏ hơn tuổi dưới", Toast.LENGTH_LONG).show();
                    return;
                }

                place.region = stfLocation.getText().toString();
                place.id = place.name.toLowerCase(Locale.ROOT) + System.currentTimeMillis();

                // default placeType = "" means vaccine place
                if (itemIndex == 1) {
                    place.placeType = PLACE_TYPE_CLOTHES_SUPPORT;
                }
                if (itemIndex == 2) {
                    place.placeType = PLACE_TYPE_FOOD_SUPPORT;
                }

                if( place.name == null || place.address == null|| place.openingTime == null|| place.closingTime == null
                        || place.region == null || place.region == null){
                    Toast.makeText(PlusLocationActivity.this, "Cần điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
                }else {
                    Intent i = new Intent(PlusLocationActivity.this, MainActivity.class);
                    i.putExtra("place", place);
                    i.putExtra("placelong", place.longitude);
                    i.putExtra("placelat", place.latitude);
                    i.putExtra("placeaddress", place.address);
                    i.putExtra("placename", place.name);
                    i.putExtra("placeid", item);
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_vaccine, items);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                itemIndex = position;
            }
        });






    }




}
