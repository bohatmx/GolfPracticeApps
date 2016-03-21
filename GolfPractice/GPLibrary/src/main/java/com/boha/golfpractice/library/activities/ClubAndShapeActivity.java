package com.boha.golfpractice.library.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.boha.golfpractice.library.R;
import com.boha.golfpractice.library.adapters.ClubAdapter;
import com.boha.golfpractice.library.dto.ClubDTO;
import com.boha.golfpractice.library.dto.ResponseDTO;
import com.boha.golfpractice.library.dto.ShotShapeDTO;
import com.boha.golfpractice.library.util.MonLog;
import com.boha.golfpractice.library.util.SnappyGeneral;
import com.boha.golfpractice.library.util.Util;

import java.util.Collections;
import java.util.List;

public class ClubAndShapeActivity extends AppCompatActivity {

    RadioGroup shapeRadioGroup;
    RadioGroup clubRadioGroup;
    MonApp app;

    List<ClubDTO> clubList;
    List<ShotShapeDTO> shotShapeList;

    ClubDTO selectedClub;
    ShotShapeDTO selectedShape;
    Context ctx;
    ClubAdapter adapter;
    Button btnDone;
    LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_selection);
        ctx = getApplicationContext();
        inflater = getLayoutInflater();
        initialize();
        getData();

    }
    private void initialize() {
        shapeRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        clubRadioGroup = (RadioGroup) findViewById(R.id.clubRadioGroup);
        btnDone = (Button)findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonLog.w(ctx,"ClubAndShapeActivity","onClick, selectedClub: " + selectedClub
                + " selectedShape: " + selectedShape);
                if (selectedClub != null && selectedShape != null) {
                    onBackPressed();
                } else {
                    Util.showErrorToast(ctx,"Please complete club and shape selection");
                }
            }
        });

    }

    private void setClubs() {

        for (ClubDTO c: clubList) {
            final RadioButton rb = (RadioButton) inflater.inflate(R.layout.radio,null);
            rb.setText(c.getClubName());
            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedClub = getClub(rb.getText().toString());
                        MonLog.w(ctx,"ClubAndShapeActivity","Club selected: " + selectedClub.getClubName());
                    }
                }
            });
            clubRadioGroup.addView(rb);
        }

    }
    private ClubDTO getClub(String name) {
        for (ClubDTO c: clubList) {
            if (c.getClubName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }
    private void setShapes() {
        for (final ShotShapeDTO ss: shotShapeList) {
            final RadioButton rb = (RadioButton) inflater.inflate(R.layout.radio,null);
            rb.setText(ss.getShape());
            rb.setTextColor(ContextCompat.getColor(ctx,R.color.blue_500));
            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedShape = ss;
                        MonLog.w(ctx,"ClubAndShapeActivity","Shape selected: " + selectedShape.getShape());
                    }
                }
            });
            shapeRadioGroup.addView(rb);
        }
    }
    private void getData() {
        SnappyGeneral.getLookups((MonApp)getApplication(), new SnappyGeneral.DBReadListener() {
            @Override
            public void onDataRead(ResponseDTO response) {
                clubList = response.getClubList();
                shotShapeList = response.getShotShapeList();
                setShapes();
                Collections.sort(clubList);
                setClubs();
            }

            @Override
            public void onError(String message) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        if (selectedClub != null) {
            Intent m = new Intent();
            m.putExtra("club", selectedClub);
            m.putExtra("shotShape",selectedShape);
            setResult(RESULT_OK,m);
        }
        finish();

    }
}
