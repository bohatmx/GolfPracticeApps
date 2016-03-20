package com.boha.golfpractice.library.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.boha.golfpractice.library.R;
import com.boha.golfpractice.library.activities.MonApp;
import com.boha.golfpractice.library.dto.ClubDTO;
import com.boha.golfpractice.library.dto.ClubUsedDTO;
import com.boha.golfpractice.library.dto.ResponseDTO;
import com.boha.golfpractice.library.dto.ShotShapeDTO;
import com.boha.golfpractice.library.util.SnappyGeneral;
import com.boha.golfpractice.library.util.Util;

import java.util.List;

/**
 * Created by aubreymalabie on 3/19/16.
 */
public class ClubsAndShapesDialog extends DialogFragment {
    View view;
    RadioGroup radioGroupClubs;
    RadioGroup radioGroupShapes;
    Context ctx;
    Button btnDone, btnCancel;
    ClubsAndShapesListener listener;
    MonApp app;

    public interface ClubsAndShapesListener {
        void onSelected(ClubUsedDTO clubUsed);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
        view = inflater.inflate(R.layout.club_used_dialog, container, false);
        ctx = inflater.getContext();
        setFields();
        return view;
    }
    private void setFields() {
        radioGroupClubs = (RadioGroup)view.findViewById(R.id.radioGroupClubs);
        radioGroupShapes = (RadioGroup)view.findViewById(R.id.radioGroupShapes);
        btnDone = (Button) view.findViewById(R.id.btnDone);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSelections();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        SnappyGeneral.getLookups(app, new SnappyGeneral.DBReadListener() {
            @Override
            public void onDataRead(ResponseDTO response) {
                clubList = response.getClubList();
                shotShapeList = response.getShotShapeList();
                setRadioButtons();
            }

            @Override
            public void onError(String message) {

            }
        });
    }
    private void processSelections() {
        if (selectedClubName == null) {
            Util.showErrorToast(ctx,"Please select Club");
            return;
        }
        if (selectedShotShape == null) {
            Util.showErrorToast(ctx,"Please select Shot shape");
            return;
        }
        ClubDTO club = null;
        for (ClubDTO c: clubList) {
            if (selectedClubName.equalsIgnoreCase(c.getClubName())) {
                club = c;
                break;
            }
        }
        ShotShapeDTO shape = null;
        for (ShotShapeDTO c: shotShapeList) {
            if (selectedShotShape.equalsIgnoreCase(c.getShape())) {
                shape = c;
                break;
            }
        }
        ClubUsedDTO cu = new ClubUsedDTO();
        cu.setClub(club);
        cu.setShotShape(shape);

        listener.onSelected(cu);
        dismiss();

    }

    String selectedClubName, selectedShotShape;
    List<ClubDTO> clubList;
    List<ShotShapeDTO> shotShapeList;

    private void setRadioButtons() {
        for (ClubDTO c: clubList) {
            final RadioButton rb = new RadioButton(ctx);
            rb.setText(c.getClubName());
            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedClubName = rb.getText().toString();
                    }
                }
            });
            radioGroupClubs.addView(rb);
        }
        for (ShotShapeDTO c: shotShapeList) {
            final RadioButton rb = new RadioButton(ctx);
            rb.setText(c.getShape());
            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedShotShape = rb.getText().toString();
                    }
                }
            });
            radioGroupShapes.addView(rb);
        }
    }
    public void setListener(ClubsAndShapesListener listener) {
        this.listener = listener;
    }

    public void setApp(MonApp app) {
        this.app = app;
    }
}
