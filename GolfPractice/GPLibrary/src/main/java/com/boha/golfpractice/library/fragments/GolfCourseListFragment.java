package com.boha.golfpractice.library.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boha.golfpractice.library.R;
import com.boha.golfpractice.library.adapters.GolfCourseListAdapter;
import com.boha.golfpractice.library.dto.GolfCourseDTO;
import com.boha.golfpractice.library.dto.ResponseDTO;
import com.boha.golfpractice.library.util.MonLog;

import java.util.ArrayList;
import java.util.List;


public class GolfCourseListFragment extends Fragment implements PageFragment {

    public static GolfCourseListFragment newInstance(List<GolfCourseDTO> list) {
        GolfCourseListFragment fragment = new GolfCourseListFragment();
        Bundle args = new Bundle();
        ResponseDTO w = new ResponseDTO();
        w.setGolfCourseList(list);
        args.putSerializable("response", w);
        fragment.setArguments(args);
        return fragment;
    }

    List<GolfCourseDTO> golfCourseList = new ArrayList<>();
    RecyclerView mRecyclerView;
    TextView txtCount;
    View view;
    GolfCourseListAdapter adapter;
    FloatingActionButton fab;
    static final String LOG = GolfCourseListFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ResponseDTO w = (ResponseDTO) getArguments().getSerializable("response");
            golfCourseList = w.getGolfCourseList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_golfcourse_list, container, false);
        setFields();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        MonLog.d(getActivity(),LOG,"+++++++++++++ onResume ++++");
        setList();
    }
    private void setList() {
        if (golfCourseList == null || golfCourseList.isEmpty()) {
            return;
        }
        MonLog.d(getActivity(),LOG,"########## setList: " + golfCourseList.size());
        txtCount.setText("" + golfCourseList.size());
        adapter = new GolfCourseListAdapter(golfCourseList, getActivity(), new GolfCourseListAdapter.GolfCourseListener() {
            @Override
            public void onCourseClicked(GolfCourseDTO course) {
                if (mListener != null) {
                    mListener.onGolfCourseClicked(course);
                }
            }
        });

        mRecyclerView.setAdapter(adapter);

    }
    private void setFields() {
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler);
        txtCount = (TextView)view.findViewById(R.id.count);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);


    }

    public void setGolfCourseList(List<GolfCourseDTO> golfCourseList) {
        this.golfCourseList = golfCourseList;
        if (mRecyclerView != null) {
            setList();
        }
    }

     GolfCourseListListener mListener;

    public void setListener(GolfCourseListListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GolfCourseListListener) {
            mListener = (GolfCourseListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement GolfCourseListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public String getPageTitle() {
        return "Golf Courses";
    }

    public interface GolfCourseListListener {
        void onGolfCourseClicked(GolfCourseDTO course);
    }
}
