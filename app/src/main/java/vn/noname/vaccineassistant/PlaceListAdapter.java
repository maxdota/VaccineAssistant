package vn.noname.vaccineassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.noname.vaccineassistant.model.VaccinePlace;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceViewHolder> {

    private ArrayList<VaccinePlace> places = new ArrayList<>();
    private LayoutInflater mInflater;
    private ListAdapter.OnClickAction onClickAction;
    private String deviceId = DataCenter.getInstance().getDeviceId();

    public PlaceListAdapter(Context context, ListAdapter.OnClickAction onClickAction) {
        mInflater = LayoutInflater.from(context);
        this.onClickAction = onClickAction;
    }

    public void updateData(ArrayList<VaccinePlace> places) {
        this.places.clear();
        this.places.addAll(places);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new PlaceViewHolder(view, onClickAction);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        VaccinePlace place = places.get(position);
        holder.setData(place, position, deviceId);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }
}
