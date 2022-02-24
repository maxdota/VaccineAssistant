package vn.noname.vaccineassistant;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.noname.vaccineassistant.model.VaccinePlace;

public class PlaceViewHolder extends RecyclerView.ViewHolder {

    private final TextView likeCount;
    private final TextView unlikeCount;
    private final ImageView likeIcon;
    private final ImageView unlikeIcon;
    private final TextView txtName;
    private final ImageView btnAddress;
    private final TextView txtAddress;
    private final TextView txtStart;
    private final TextView txtVaccine;
    private final TextView txtAge;
    private final ImageView btnRegion;
    private final TextView txtRegion;
    private final TextView txtFee;

    private int position;
    private VaccinePlace place;
    private boolean isLikeSelected;
    private boolean isUnlikeSelected;

    public PlaceViewHolder(@NonNull View view, ListAdapter.OnClickAction onClickAction) {
        super(view);

        likeCount = (TextView) view.findViewById(R.id.tvLike);
        unlikeCount = (TextView) view.findViewById(R.id.tvUnlike);
        likeIcon = view.findViewById(R.id.icLike);
        unlikeIcon = view.findViewById(R.id.icUnlike);

        txtName = (TextView) view.findViewById(R.id.tname);
        btnAddress = (ImageView) view.findViewById(R.id.iaddress);
        txtAddress = (TextView) view.findViewById(R.id.taddress);

        txtStart = (TextView) view.findViewById(R.id.start);

        txtVaccine = (TextView) view.findViewById(R.id.tvaccine);

        txtAge = (TextView) view.findViewById(R.id.tage);
        btnRegion = (ImageView) view.findViewById(R.id.iregion);
        txtRegion = (TextView) view.findViewById(R.id.tregion);

        txtFee = (TextView) view.findViewById(R.id.tfee);


        likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUnlikeSelected) {
                    return;
                }
                onClickAction.onLike(place, !isLikeSelected);
            }
        });

        unlikeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLikeSelected) {
                    return;
                }
                onClickAction.onUnlike(place, !isUnlikeSelected);
            }
        });
    }

    public void setData(VaccinePlace place, int position, String deviceId) {
        this.position = position;
        this.place = place;
        txtName.setText(place.name);
        txtAddress.setText(place.address);
        txtStart.setText(place.openingTime + "-" + place.closingTime);
//        txtClose.setText(place.closingTime);
        txtVaccine.setText(place.vaccine);

        likeCount.setText(place.likeCount + "");
        unlikeCount.setText(place.unlikeCount + "");

        isLikeSelected = (place.likeSelectedUsers != null) && place.likeSelectedUsers.contains(deviceId);
        likeIcon.setImageResource(
                isLikeSelected ?
                        R.drawable.ic_like_selected :
                        R.drawable.like
        );

        isUnlikeSelected = (place.unlikeSelectedUsers != null) && place.unlikeSelectedUsers.contains(deviceId);
        unlikeIcon.setImageResource(
                isUnlikeSelected ?
                        R.drawable.ic_unlike_selected :
                        R.drawable.ic_unlike
        );

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
    }
}
