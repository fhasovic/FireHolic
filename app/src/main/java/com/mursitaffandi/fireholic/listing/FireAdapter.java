package com.mursitaffandi.fireholic.listing;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mursitaffandi.fireholic.R;
import com.mursitaffandi.fireholic.model.MFire;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mursitaffandi on 1/18/18.
 */

public class FireAdapter extends RecyclerView.Adapter<VHFire> {
    private Context mContext;
    private List<MFire> mFire = new ArrayList<>();

    public FireAdapter(Context context) {
        this.mContext = context;
    }
public void swapFire(List<MFire> fireList){
    this.mFire = fireList;
    notifyDataSetChanged();
}

    public void clearFire() {
        mFire.clear();
        notifyDataSetChanged();
    }
    @Override
    public VHFire onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHFire( LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_fire, parent, false));
    }

    @Override
    public void onBindViewHolder(VHFire holder, int position) {
holder.bind(mFire.get(position));
    }

    @Override
    public int getItemCount() {
        return mFire.size();
    }
}
