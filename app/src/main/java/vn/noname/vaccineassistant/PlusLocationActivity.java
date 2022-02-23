package vn.noname.vaccineassistant;

import static vn.noname.vaccineassistant.model.VaccinePlace.PLACE_TYPE_CLOTHES_SUPPORT;
import static vn.noname.vaccineassistant.model.VaccinePlace.PLACE_TYPE_FOOD_SUPPORT;
import static vn.noname.vaccineassistant.model.VaccinePlace.PLACE_TYPE_SUPPORT;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import vn.noname.vaccineassistant.base.BaseActivity;
import vn.noname.vaccineassistant.model.VaccinePlace;

public class PlusLocationActivity extends BaseActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "PlusLocationActivity";

    String[] items = {"Cứu trợ","Cung cấp quần áo", "Cung cấp đồ ăn", "Vaccine"};
    String item;
    int itemIndex;
    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView date_picker;
    ArrayAdapter<String> adapterItems;
    private EditText nameLocation,addressLocation, openLocation, payLocation, stfLocation, closeLocation, age_above, age_below;
    Button save_id;
    VaccinePlace place= new VaccinePlace();

    View viewLoading;
    TextView imageEmpty;
    ImageView imageContent;
    StorageReference imageFileRef;

    boolean isUploadingImage = false;

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

        imageEmpty = findViewById(R.id.imageEmpty);
        imageContent = findViewById(R.id.imageContent);
        viewLoading = findViewById(R.id.viewLoading);

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

        imageEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

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
                if (itemIndex == 0) {
                    place.placeType = PLACE_TYPE_SUPPORT;
                }
                if (itemIndex == 1) {
                    place.placeType = PLACE_TYPE_CLOTHES_SUPPORT;
                }
                if (itemIndex == 2) {
                    place.placeType = PLACE_TYPE_FOOD_SUPPORT;
                }

                if( TextUtils.isEmpty(place.name) || TextUtils.isEmpty(place.address)|| TextUtils.isEmpty(place.openingTime)|| TextUtils.isEmpty(place.closingTime)
                        || TextUtils.isEmpty(place.region)){
                    Toast.makeText(PlusLocationActivity.this, "Cần điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
                }else {
                    createNewPlace();
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

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

                Toast.makeText(this, "Lỗi ko tạo được file", Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "vn.noname.vaccineassistant.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic();
            imageEmpty.setVisibility(View.GONE);
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private Bitmap rotate(Bitmap bitmap) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(currentPhotoPath);
        } catch (IOException e) {
            return bitmap;
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }

    private void setPic() {
        setUploadingImage(true);
        ImageView imageView = imageContent;

        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        Bitmap rotatedBitmap = rotate(bitmap);
        imageView.setImageBitmap(rotatedBitmap);

        uploadImage(rotatedBitmap);
    }

    private void uploadImage(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference().child("place_images");
        imageFileRef = storageRef.child(System.currentTimeMillis() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageFileRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                setUploadingImage(false);
                Toast.makeText(PlusLocationActivity.this, "Upload hình thất bại: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Upload image failure: " + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                Log.e(TAG, "Upload image success: " + taskSnapshot.getMetadata());

                Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();

                downloadUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String generatedFilePath = downloadUri.getResult().toString();
                        Log.e(TAG, "Stored path is " + generatedFilePath);

                        place.imageUrl = generatedFilePath;
                        setUploadingImage(false);
                        createNewPlace();
                    }
                });
            }
        });
    }

    private void setUploadingImage(boolean isUploading) {
        isUploadingImage = isUploading;
        if (!isUploadingImage) {
            viewLoading.setVisibility(View.GONE);
        }
    }

    private void createNewPlace() {
        if (isUploadingImage) {
            viewLoading.setVisibility(View.VISIBLE);
            return;
        }
        if (place.id == null) {
            return;
        }

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
