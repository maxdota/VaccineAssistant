package vn.noname.vaccineassistant;

import android.app.Activity;
import android.text.TextUtils;
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
    private String deviceId;
    private OnClickAction onClickAction;

    Activity context;
    int resource;
    private ArrayList<VaccinePlace> objects;
    public ListAdapter(Activity context, int resourse, ArrayList<VaccinePlace>objects, OnClickAction onClickAction){
        super(context, resourse, objects);
        this.context = context;
        this.resource = resourse;
        this.objects = objects;
        this.onClickAction = onClickAction;
        deviceId = DataCenter.getInstance().getDeviceId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);

        TextView likeCount = (TextView) row.findViewById(R.id.tvLike);
        TextView unlikeCount = (TextView) row.findViewById(R.id.tvUnlike);
        ImageView likeIcon = row.findViewById(R.id.icLike);
        ImageView unlikeIcon = row.findViewById(R.id.icUnlike);

        TextView txtName = (TextView) row.findViewById(R.id.tname);
        ImageView btnAddress = (ImageView) row.findViewById(R.id.iaddress);
        TextView txtAddress = (TextView) row.findViewById(R.id.taddress);

        TextView txtStart = (TextView) row.findViewById(R.id.start);
//        TextView txtClose = (TextView) row.findViewById(R.id.close);

        TextView txtVaccine = (TextView) row.findViewById(R.id.tvaccine);

        TextView txtAge = (TextView) row.findViewById(R.id.tage);
        ImageView btnRegion = (ImageView) row.findViewById(R.id.iregion);
        TextView txtRegion = (TextView) row.findViewById(R.id.tregion);

        TextView txtFee = (TextView) row.findViewById(R.id.tfee);
        /** Set data to row*/
        final VaccinePlace place = this.objects.get(position);
        txtName.setText(place.name);
        txtAddress.setText(place.address);
        txtStart.setText(place.openingTime + "-" + place.closingTime);
//        txtClose.setText(place.closingTime);
        txtVaccine.setText(place.vaccine);

        likeCount.setText(place.likeCount + "");
        unlikeCount.setText(place.unlikeCount + "");

        boolean isLikeSelected = (place.likeSelectedUsers != null) && place.likeSelectedUsers.contains(deviceId);
        likeIcon.setImageResource(
                isLikeSelected ?
                        R.drawable.ic_like_selected :
                        R.drawable.like
                );

        boolean isUnlikeSelected = (place.unlikeSelectedUsers != null) && place.unlikeSelectedUsers.contains(deviceId);
        unlikeIcon.setImageResource(
                isUnlikeSelected ?
                        R.drawable.ic_unlike_selected :
                        R.drawable.ic_unlike
        );

        likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAction.onLike(place, !isLikeSelected);
            }
        });

        unlikeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAction.onUnlike(place, !isUnlikeSelected);
            }
        });

        if(TextUtils.isEmpty(place.vaccine)){
            txtVaccine.setVisibility(View.GONE);
        }
        if(TextUtils.isEmpty(place.openingTime)){
            txtStart.setVisibility(View.GONE);
        }
        if(TextUtils.isEmpty(place.region)){
            btnRegion.setVisibility(View.GONE);
            txtRegion.setVisibility(View.GONE);
        }

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

    public interface OnClickAction {
        void onLike(VaccinePlace place, boolean isSelected);
        void onUnlike(VaccinePlace place, boolean isSelected);
    }
}

