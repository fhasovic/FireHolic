package com.mursitaffandi.fireholic.listing;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mursitaffandi.fireholic.R;
import com.mursitaffandi.fireholic.model.MFire;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mursitaffandi on 1/18/18.
 */

class VHFire extends RecyclerView.ViewHolder {
@BindView(R.id.tv_item_head)
    TextView tv_title;
@BindView(R.id.tv_item_child)
    TextView tv_child;

    public VHFire(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final MFire item) {
        tv_title.setText(item.getStr_title());
        tv_child.setText(item.getStr_child());

    }
}
