package com.xpple.plant.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xpple.plant.R;
import com.xpple.plant.view.BaseFragment;

public class ClassesFragment extends BaseFragment implements OnClickListener,
        OnTouchListener {
    //public class ClassesFragment extends BaseFragment {
    private View parentView;

    private ImageView key1, key2, key3, key4;
    private ImageView type1, type2, type3, type4, type5, type6, type7, type8,
            type9, type10, type11, type12;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.layout_class, container, false);
        // Inflate the layout for this fragment
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        key1 = (ImageView) parentView.findViewById(R.id.key1);
        key2 = (ImageView) parentView.findViewById(R.id.key2);
        key3 = (ImageView) parentView.findViewById(R.id.key3);
        key4 = (ImageView) parentView.findViewById(R.id.key4);
        type1 = (ImageView) parentView.findViewById(R.id.ImageView01);
        type2 = (ImageView) parentView.findViewById(R.id.ImageView02);
        type3 = (ImageView) parentView.findViewById(R.id.ImageView03);
        type4 = (ImageView) parentView.findViewById(R.id.ImageView04);
        type5 = (ImageView) parentView.findViewById(R.id.ImageView05);
        type6 = (ImageView) parentView.findViewById(R.id.ImageView06);
        type7 = (ImageView) parentView.findViewById(R.id.ImageView07);
        type8 = (ImageView) parentView.findViewById(R.id.ImageView08);
        type9 = (ImageView) parentView.findViewById(R.id.ImageView09);
        type10 = (ImageView) parentView.findViewById(R.id.ImageView10);
        type11 = (ImageView) parentView.findViewById(R.id.ImageView11);
        type12 = (ImageView) parentView.findViewById(R.id.ImageView12);

        key1.setOnClickListener(this);
        key2.setOnClickListener(this);
        key3.setOnClickListener(this);
        key4.setOnClickListener(this);
        type1.setOnClickListener(this);
        type2.setOnClickListener(this);
        type3.setOnClickListener(this);
        type4.setOnClickListener(this);
        type5.setOnClickListener(this);
        type6.setOnClickListener(this);
        type7.setOnClickListener(this);
        type8.setOnClickListener(this);
        type9.setOnClickListener(this);
        type10.setOnClickListener(this);
        type11.setOnClickListener(this);
        type12.setOnClickListener(this);
        key1.setOnTouchListener(this);
        key2.setOnTouchListener(this);
        key3.setOnTouchListener(this);
        key4.setOnTouchListener(this);
        type1.setOnTouchListener(this);
        type2.setOnTouchListener(this);
        type3.setOnTouchListener(this);
        type4.setOnTouchListener(this);
        type5.setOnTouchListener(this);
        type6.setOnTouchListener(this);
        type7.setOnTouchListener(this);
        type8.setOnTouchListener(this);
        type9.setOnTouchListener(this);
        type10.setOnTouchListener(this);
        type11.setOnTouchListener(this);
        type12.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO 自动生成的方法存根
        playHeartbeatAnimation(v);

        switch (v.getId()) {
            case R.id.key1:

                break;
            case R.id.key2:

                break;
            case R.id.key3:

                break;
            case R.id.key4:

                break;
            case R.id.ImageView01:

                break;
            case R.id.ImageView02:

                break;
            case R.id.ImageView03:

                break;
            case R.id.ImageView04:

                break;
            case R.id.ImageView05:

                break;
            case R.id.ImageView06:

                break;
            case R.id.ImageView07:

                break;
            case R.id.ImageView08:

                break;
            case R.id.ImageView09:

                break;
            case R.id.ImageView10:

                break;
            case R.id.ImageView11:

                break;
            case R.id.ImageView12:

                break;
            default:
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO 自动生成的方法存根
        int Action = event.getAction();
        if (Action == MotionEvent.ACTION_DOWN) {
            playHeartbeatAnimation(v);
        }
        return false;
    }
}
