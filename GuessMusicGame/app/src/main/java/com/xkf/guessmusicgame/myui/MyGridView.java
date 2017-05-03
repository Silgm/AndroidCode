package com.xkf.guessmusicgame.myui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.xkf.guessmusicgame.R;
import com.xkf.guessmusicgame.model.IWordButtonClickListener;
import com.xkf.guessmusicgame.model.WordButton;
import com.xkf.guessmusicgame.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MyGridView extends GridView {
    public static final int COUNT_WORDS = 24;
    private Animation mScaleAnimation;
    private List<WordButton> mList = new ArrayList<>();
    private MyGridAdapter mAdapter;
    private Context mContext;
    private IWordButtonClickListener mWordButtonClickListener;

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAdapter = new MyGridAdapter();
        this.setAdapter(mAdapter);
    }

    public void updataData(List<WordButton> list) {
        mList = list;
        this.setAdapter(mAdapter);
    }

    class MyGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup group) {
            final WordButton holder;
            mScaleAnimation = AnimationUtils.loadAnimation(mContext, R.anim.scale);
            if (view == null) {
                view = Util.getView(mContext, R.layout.self_ui_gridview_item);
                holder = mList.get(i);
                holder.mIndex = i;
                if (holder.mViewButton == null) {
                    holder.mViewButton = (Button) view.findViewById(R.id.item_btn);
                    holder.mViewButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mWordButtonClickListener.onWordButtonClick(holder);
                        }
                    });
                    holder.mViewButton.setText(holder.mString);
                }
                view.setTag(holder);
            } else {
                holder = (WordButton) view.getTag();
            }
            mScaleAnimation.setStartOffset(i * 100);
            view.startAnimation(mScaleAnimation);
            return view;
        }
    }

    //监听器注册
    public void registOnWordButtononClick(IWordButtonClickListener listener) {
        mWordButtonClickListener = listener;
    }
}
