package com.boha.golfpractice.library.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.boha.golfpractice.library.R;
import com.boha.golfpractice.library.dto.PracticeSessionDTO;
import com.boha.golfpractice.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by aubreyM on 14/12/17.
 */
public class SessionListAdapter extends RecyclerView.Adapter<SessionListAdapter.SessionViewHolder> {

    public interface SessionListener {
        void onSessionClicked(PracticeSessionDTO session);
        void onSessionCloseRequired(PracticeSessionDTO session);
    }

    private SessionListener mListener;
    private List<PracticeSessionDTO> practiceSessionList;
    private Context ctx;

    public SessionListAdapter(List<PracticeSessionDTO> sessionList,
                              Context context, SessionListener listener) {
        this.practiceSessionList = sessionList;
        this.ctx = context;
        this.mListener = listener;
    }


    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_item, parent, false);
        return new SessionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SessionViewHolder holder, final int position) {

        final PracticeSessionDTO p = practiceSessionList.get(position);
        holder.golfCourse.setText(p.getGolfCourseName());
        holder.sessionDate.setText(sdf.format(new Date(p.getSessionDate())));
        holder.numHoles.setText("" + p.getNumberOfHoles());
        holder.numStrokes.setText("" + p.getTotalStrokes());
        holder.numUnder.setText("" + p.getUnderPar());
        holder.par.setText("" + p.getPar());
        holder.numOver.setText("" + p.getOverPar());
        holder.number.setText("" +(position + 1));


        holder.numMistakes.setText(""+p.getTotalMistakes());
        if (p.getClosed() == false) {
            holder.btnClose.setEnabled(true);
            holder.btnClose.setText("Close Session");
            holder.btnClose.setAlpha(1.0f);
            holder.golfCourse.setTextColor(ContextCompat.getColor(ctx,R.color.black));

        } else {
            holder.btnClose.setEnabled(false);
            holder.btnClose.setText("Closed");
            holder.btnClose.setAlpha(0.5f);
            holder.golfCourse.setTextColor(ContextCompat.getColor(ctx,R.color.grey_400));
        }

        holder.golfCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSessionClicked(p);
            }
        });
        holder.sessionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSessionClicked(p);
            }
        });
        holder.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSessionCloseRequired(p);
            }
        });
        Statics.setRobotoFontLight(ctx, holder.golfCourse);

    }

    @Override
    public int getItemCount() {
        return practiceSessionList == null ? 0 : practiceSessionList.size();
    }

    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm", loc);
    static final DecimalFormat df = new DecimalFormat("###,###,###,###");

    public class SessionViewHolder extends RecyclerView.ViewHolder  {
        protected TextView golfCourse, sessionDate, number;
        protected Button btnClose;
        protected TextView numHoles, numStrokes, numUnder, numOver, par, numMistakes;


        public SessionViewHolder(View itemView) {
            super(itemView);

            golfCourse = (TextView) itemView
                    .findViewById(R.id.golfCourse);
            sessionDate = (TextView) itemView
                    .findViewById(R.id.date);
            numHoles = (TextView) itemView
                    .findViewById(R.id.holeCount);
            numStrokes = (TextView) itemView
                    .findViewById(R.id.strokes);

            numUnder = (TextView) itemView
                    .findViewById(R.id.underPar);
            numOver = (TextView) itemView
                    .findViewById(R.id.overpar);
            par = (TextView) itemView
                    .findViewById(R.id.par);
            numMistakes = (TextView) itemView
                    .findViewById(R.id.mistakes);
            number = (TextView) itemView
                    .findViewById(R.id.number);
            btnClose = (Button) itemView
                    .findViewById(R.id.btnClose);




        }

    }

    static final String LOG = SessionListAdapter.class.getSimpleName();
}
