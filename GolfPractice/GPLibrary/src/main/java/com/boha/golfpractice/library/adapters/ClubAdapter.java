package com.boha.golfpractice.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.boha.golfpractice.library.R;
import com.boha.golfpractice.library.dto.ClubDTO;

import java.util.List;

/**
 * Created by aubreymalabie on 3/21/16.
 */
public class ClubAdapter extends ArrayAdapter<ClubDTO> {
    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ClubDTO> mList;
    private Context ctx;

    public interface ClubListener {
        void onSelected(ClubDTO club, int position);
        void onRemoved(ClubDTO club, int position);
    }
    ClubListener listener;

    public ClubAdapter(Context context, int layoutResource, List<ClubDTO> clubList,
             ClubListener listener) {
        super(context, layoutResource, clubList);
        this.mLayoutRes = layoutResource;
        this.listener = listener;
        mList = clubList;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    static class ViewHolderItem {
        RadioButton radioButton;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.radioButton = (RadioButton) convertView
                    .findViewById(R.id.radioBtn);
            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final ClubDTO p = mList.get(position);
        item.radioButton.setText(p.getClubName());
        item.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listener.onSelected(p,position);
                } else {
                    listener.onSelected(p,position);
                }
            }
        });
        return (convertView);
    }
}
