package com.boha.golfpractice.library.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boha.golfpractice.library.R;

/**
 * Created by aubreymalabie on 3/17/16.
 */
public class Util {
    public static void showErrorToast(Context ctx, String caption) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.toast_monitor_generic, null);
        TextView txt = (TextView) view.findViewById(R.id.MONTOAST_text);
        TextView ind = (TextView) view.findViewById(R.id.MONTOAST_indicator);
        ind.setText("E");
        Statics.setRobotoFontLight(ctx, txt);
        ind.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xred_oval_small));
        txt.setTextColor(ContextCompat.getColor(ctx, R.color.absa_red));
        txt.setText(caption);
        Toast customtoast = new Toast(ctx);

        customtoast.setView(view);
        customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        customtoast.setDuration(Toast.LENGTH_LONG);
        customtoast.show();
    }

    public static void showToast(Context ctx, String caption) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.toast_monitor_generic, null);
        TextView txt = (TextView) view.findViewById(R.id.MONTOAST_text);
        Statics.setRobotoFontLight(ctx, txt);
        TextView ind = (TextView) view.findViewById(R.id.MONTOAST_indicator);
        ind.setText("M");
        ind.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xblue_oval_small));
        txt.setTextColor(ContextCompat.getColor(ctx, R.color.blue));
        txt.setText(caption);

        Toast customtoast = new Toast(ctx);

        customtoast.setView(view);
        customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        customtoast.setDuration(Toast.LENGTH_SHORT);
        customtoast.show();
    }
    public static ImageView setCustomActionBar(Context ctx,
                                               ActionBar actionBar, String text, String subText, Drawable image) {
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater)
                ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar_logo, null);
        TextView txt = (TextView) v.findViewById(R.id.ACTION_BAR_text);
        TextView sub = (TextView) v.findViewById(R.id.ACTION_BAR_subText);
        ImageView logo = (ImageView) v.findViewById(R.id.ACTION_BAR_logo);
        txt.setText(text);
        sub.setText(subText);
        //
        logo.setImageDrawable(image);
        actionBar.setCustomView(v);
        actionBar.setTitle("");
        return logo;
    }

}
