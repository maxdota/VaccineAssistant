package vn.noname.vaccineassistant;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.noname.vaccineassistant.model.VaccinePlace;

public class ListAdapter extends ArrayAdapter<VaccinePlace> {
    Activity context;
    int resource;
    private ArrayList<VaccinePlace> objects;
    public ListAdapter(Activity context, int resourse, ArrayList<VaccinePlace>objects){
        super(context, resourse, objects);
        this.context = context;
        this.resource = resourse;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);

        TextView txtName = (TextView) row.findViewById(R.id.tname);
        ImageView btnAddress = (ImageView) row.findViewById(R.id.iaddress);
        TextView txtAddress = (TextView) row.findViewById(R.id.taddress);
        ImageView btnTime = (ImageView) row.findViewById(R.id.itime);
        TextView txtStart = (TextView) row.findViewById(R.id.start);
//        TextView txtClose = (TextView) row.findViewById(R.id.close);
        ImageView btnVaccine = (ImageView) row.findViewById(R.id.ivaccine);
        TextView txtVaccine = (TextView) row.findViewById(R.id.tvaccine);
        ImageView btnAge = (ImageView) row.findViewById(R.id.iage);
        TextView txtAge = (TextView) row.findViewById(R.id.tage);
        ImageView btnRegion = (ImageView) row.findViewById(R.id.iregion);
        TextView txtRegion = (TextView) row.findViewById(R.id.tregion);
        ImageView btnFee = (ImageView) row.findViewById(R.id.ifee);
        TextView txtFee = (TextView) row.findViewById(R.id.tfee);
        /** Set data to row*/
        final VaccinePlace place = this.objects.get(position);
        txtName.setText(place.name);
        txtAddress.setText(place.address);
        txtStart.setText(place.openingTime + "-" + place.closingTime);
//        txtClose.setText(place.closingTime);
        txtVaccine.setText(place.vaccine);
        int ageAbove = place.ageLimitAbove;
        int ageBelow = place.ageLimitBelow;
        if (ageBelow == 0 && ageAbove == 0) {
            txtAge.setText("không giới hạn");
        } else if (ageAbove == 0) {
            txtAge.setText("<" + ageBelow);
        } else if (ageBelow == 0) {
            txtAge.setText(">" + ageAbove);
        } else {
            txtAge.setText(">" + ageAbove + "và <" + ageBelow);
        }
        txtRegion.setText(("unlimited".equals(place.region)) ? "Không giới hạn" : (place.region));
        txtFee.setText((place.fee == 0) ? "Miễn Phí" : (place.fee + "đ"));
        return row;
    }
    /** Show mesage*/
    private void showMessage(Place place) {
        Toast.makeText(this.context,place.toString(),Toast.LENGTH_LONG).show();
    }
}

