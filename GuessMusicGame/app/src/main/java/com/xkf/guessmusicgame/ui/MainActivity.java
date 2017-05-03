package com.xkf.guessmusicgame.ui;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xkf.guessmusicgame.R;
import com.xkf.guessmusicgame.data.Const;
import com.xkf.guessmusicgame.model.IAlertDialogButtonListener;
import com.xkf.guessmusicgame.model.IWordButtonClickListener;
import com.xkf.guessmusicgame.model.Song;
import com.xkf.guessmusicgame.model.WordButton;
import com.xkf.guessmusicgame.myui.MyGridView;
import com.xkf.guessmusicgame.util.MyPlayer;
import com.xkf.guessmusicgame.util.Util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements IWordButtonClickListener {
    //正确答案
    public static final int ANSER_RIGHT = 1;

    //错误答案
    public static final int ANSER_ERROR = -1;

    //不完整答案
    public static final int ANSER_LACK = 0;

    //文字闪烁的次数
    public static final int SPASH_TIMES = 6;

    //作为删除按钮对话框的提示ID
    public static final int ID_DIALOG_DELETE_WORD = 1;

    //作为提示按钮对话框的提示ID
    public static final int ID_DIALOG_TIP_WORD = 2;

    //作为缺钱按钮对话框的提示ID
    public static final int ID_DIALOG_LOCK_COINS = 3;

    //过关时显示的布局页面
    public View mPassView;

    //过关节面的关卡的显示文字
    private TextView mCurrentStagePassView;

    //过关节面的当前歌曲名字的现显示
    private TextView mCurrentStageNamePassView;

    //当前节面的关卡显示
    private TextView mCurrentStageView;

    //唱片相关动画
    private Animation mPanAnim;
    private LinearInterpolator mPanLin;

    //拨杆向里运动时的动画
    private Animation mBarInAnim;
    private LinearInterpolator mBarInLin;

    //拨杆向外运动时的动画
    private Animation mBarOutAnim;
    private LinearInterpolator mBarOutLin;

    //转盘
    private ImageView mViewPan;

    //拨杆
    private ImageView mViewPanBar;

    //播放按钮
    private ImageButton mBtnPlayStart;

    //删除按钮
    private ImageButton mDeleteButton;
    private ImageButton mTipButton;

    //判断是否在运行
    private boolean mIsRunning = false;

    //下面的那一堆文字的集合
    private List<WordButton> mAllWords;

    //上面那几个文字的集合
    private List<WordButton> mBtnSelectWords;

    //文字选择框布局
    private LinearLayout mViewWordsContainer;
    private MyGridView mGridView;

    //当前歌曲
    private Song mCurrentSong;

    //当前关卡
    private int mCurrentStageIndex = -1;

    //当前的金币数量
    private int mCurrentCoins = Const.TOTAL_COINS;

    //金币数量的那个TextView
    private TextView mViewCurrentCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //读取数据
        int[] data = Util.loadData(this);
        mCurrentStageIndex = data[Const.INDEX_LOAD_SAVESTAGE];
        mCurrentCoins = data[Const.INDEX_LOAD_SAVECOINS];

        //初始化金币
        mViewCurrentCoins = (TextView) findViewById(R.id.money_count);
        mViewCurrentCoins.setText("" + mCurrentCoins);

        //初始化删除按钮
        mDeleteButton = (ImageButton) findViewById(R.id.pay_delete_word);
        mTipButton = (ImageButton) findViewById(R.id.pay_tip_answer);

        //初始化当前关卡的显示文本
        mCurrentStageView = (TextView) findViewById(R.id.text_curretn_stage);

        //文字框相关组件
        mViewWordsContainer = (LinearLayout) findViewById(R.id.word_select_container);
        mGridView = (MyGridView) findViewById(R.id.mGridView);
        mGridView.registOnWordButtononClick(this);

        //转盘相关组件
        mViewPan = (ImageView) findViewById(R.id.panView);
        mViewPanBar = (ImageView) findViewById(R.id.barView);

        //初始化动画
        mPanAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        mPanLin = new LinearInterpolator();
        mPanAnim.setInterpolator(mPanLin);
        mPanAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewPanBar.startAnimation(mBarOutAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //拨杆相关组件
        mBarInAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_45);
        mBarInLin = new LinearInterpolator();
        mBarInAnim.setInterpolator(mBarInLin);
        mBarInAnim.setFillAfter(true);
        mBarInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewPan.startAnimation(mPanAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBarOutAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_d_45);
        mBarOutLin = new LinearInterpolator();
        mBarOutAnim.setInterpolator(mBarOutLin);
        mBarOutAnim.setFillAfter(true);
        mBarOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsRunning = false;
                mBtnPlayStart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //中间的播放按钮
        mBtnPlayStart = (ImageButton) findViewById(R.id.btn_play_start);
        mBtnPlayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePlayButton();
            }
        });

        //初始化数据
        initCurrentStageData();

        //添加删除按钮的逻辑
        handleDeleteButton();

        //添加提示按钮的逻辑
        handleTipButton();
    }

    //播放按钮点击事件
    public void handlePlayButton() {
        if (mViewPanBar != null) {
            if (!mIsRunning) {
                mViewPanBar.startAnimation(mBarInAnim);
                mIsRunning = true;
                mBtnPlayStart.setVisibility(View.GONE);
                MyPlayer.playSong(this, mCurrentSong.getSongFileNmae());
            }
        }
    }

    //活动暂停的时候清除动画
    @Override
    protected void onPause() {
        Util.saveData(this, mCurrentStageIndex - 1, mCurrentCoins);
        mViewPan.clearAnimation();
        MyPlayer.stopTheSong();
        super.onPause();
    }

    //初始化歌曲类
    private Song loadStageSongInfo(int stageIndex) {
        Song song = new Song();
        String[] s = Const.SONG_INFO[stageIndex];
        song.setSongFileNmae(s[Const.INDEX_FILE_NAME]);
        song.setSongName(s[Const.INDEX_SONG_NAME]);
        return song;
    }

    //初始化当前的关卡
    public void initCurrentStageData() {
        mCurrentSong = loadStageSongInfo(++mCurrentStageIndex);
        mAllWords = initWord();
        mBtnSelectWords = initWordSelect();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
        mViewWordsContainer.removeAllViews();
        for (int i = 0; i < mBtnSelectWords.size(); i++) {
            mViewWordsContainer.addView(mBtnSelectWords.get(i).mViewButton, layoutParams);
        }
        mCurrentStageView.setText(mCurrentStageIndex + 1 + "");
        mGridView.updataData(mAllWords);
    }

    //往下面的按钮上面填字
    private List<WordButton> initWord() {
        List<WordButton> list = new ArrayList<>();
        //获得所有待选文字
        String[] words = generateWords();
        for (int i = 0; i < MyGridView.COUNT_WORDS; i++) {
            WordButton wordbutton = new WordButton();
            wordbutton.mString = words[i];
            list.add(wordbutton);
        }
        return list;
    }

    //初始化上面那个空的文字框
    private List<WordButton> initWordSelect() {
        List<WordButton> data = new ArrayList<>();
        for (int i = 0; i < mCurrentSong.getNmaeLength(); i++) {
            View view = Util.getView(this, R.layout.self_ui_gridview_item);
            final WordButton holder = new WordButton();
            holder.mViewButton = (Button) view.findViewById(R.id.item_btn);
            holder.mViewButton.setText("");
            holder.mViewButton.setTextColor(Color.WHITE);
            holder.mViewButton.setBackgroundResource(R.drawable.game_wordblank);
            holder.mViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearTheAnswer(holder);
                }
            });
            data.add(holder);
        }
        return data;
    }

    //点击下面的按钮的监听事件
    @Override
    public void onWordButtonClick(WordButton wordButton) {
        setSelectWords(wordButton);
        int status = statusAnswer();
        if (status == ANSER_RIGHT) {
            handlePassEvent();
        } else if (status == ANSER_ERROR) {
            sparkTheWords();
        }
    }

    //生成所有的待选文字
    private String[] generateWords() {
        Random random = new Random();
        String[] words = new String[MyGridView.COUNT_WORDS];
        for (int i = 0; i < mCurrentSong.getNmaeLength(); i++) {
            words[i] = mCurrentSong.getNameChar()[i] + "";
        }

        for (int i = mCurrentSong.getNmaeLength();
             i < MyGridView.COUNT_WORDS; i++) {
            words[i] = getRandomChar() + "";
        }
        for (int i = MyGridView.COUNT_WORDS - 1; i >= 0; i--) {
            int index = random.nextInt(i + 1);

            String buf = words[index];
            words[index] = words[i];
            words[i] = buf;
        }
        return words;
    }

    //随机生成单个文字
    private char getRandomChar() {
        String str = "";
        int low;
        int high;
        Random random = new Random();
        high = (176 + Math.abs(random.nextInt(39)));
        low = (161 + Math.abs(random.nextInt(93)));
        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(high)).byteValue();
        b[1] = (Integer.valueOf(low)).byteValue();
        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.charAt(0);
    }

    //点击了上方的文字框就调用该方法
    private void clearTheAnswer(WordButton button) {
        button.mString = "";
        button.mViewButton.setText("");
        button.mIsVisiable = false;
        setButtonVisiable(mAllWords.get(button.mIndex), View.VISIBLE);
    }

    //当点击下面的按钮时触发的方法
    private void setSelectWords(WordButton wordButton) {
        for (int i = 0; i < mBtnSelectWords.size(); i++) {
            if (mBtnSelectWords.get(i).mString.length() == 0) {
                mBtnSelectWords.get(i).mViewButton.setText(wordButton.mString);
                mBtnSelectWords.get(i).mIsVisiable = true;
                mBtnSelectWords.get(i).mString = wordButton.mString;
                mBtnSelectWords.get(i).mIndex = wordButton.mIndex;
                setButtonVisiable(wordButton, View.GONE);
                break;
            }
        }
    }

    //设置按钮可见
    private void setButtonVisiable(WordButton button, int visiable) {
        button.mViewButton.setVisibility(visiable);
        button.mIsVisiable = (visiable == View.VISIBLE) ? true : false;
    }

    //判断答案的状态
    private int statusAnswer() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mBtnSelectWords.size(); i++) {
            if (mBtnSelectWords.get(i).mString == "") {
                return ANSER_LACK;
            } else {
                sb.append(mBtnSelectWords.get(i).mString);
            }
        }
        return (sb.toString().equals(mCurrentSong.getSongName())) ? ANSER_RIGHT : ANSER_ERROR;
    }

    //文字闪烁的方法
    private void sparkTheWords() {
        TimerTask task = new TimerTask() {
            int times = 0;
            boolean change = true;

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (times++ > SPASH_TIMES) {
                            for (int i = 0; i < mBtnSelectWords.size(); i++) {
                                mBtnSelectWords.get(i).mViewButton.setTextColor
                                        (Color.WHITE);
                            }
                            cancel();
                        }
                        for (int i = 0; i < mBtnSelectWords.size(); i++) {
                            mBtnSelectWords.get(i).mViewButton.setTextColor
                                    ((change) ? Color.RED : Color.WHITE);
                        }
                        change = !change;
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 150);
    }

    //过关界面的显示 处理过关事件
    private void handlePassEvent() {
        mPassView = (ConstraintLayout) findViewById(R.id.pass_view);
        mPassView.setVisibility(View.VISIBLE);
        mViewPan.clearAnimation();
        MyPlayer.stopTheSong();
        MyPlayer.playTone(this, MyPlayer.SOUND_COIN);
        mCurrentStagePassView = (TextView) findViewById(R.id.text_curretn_stage_pass);
        mCurrentStagePassView.setText(1 + mCurrentStageIndex + "");
        mCurrentStageNamePassView = (TextView) findViewById(R.id.pass_name_song);
        mCurrentStageNamePassView.setText(mCurrentSong.getSongName());
        ImageButton btnpass = (ImageButton) findViewById(R.id.pass_next_button);
        btnpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (judgeAppPass()) {
                    AppPassActivity.startActivity(MainActivity.this);
                } else {
                    mPassView.setVisibility(View.GONE);
                    initCurrentStageData();
                }
            }
        });
    }

    //判断是否通关
    private boolean judgeAppPass() {
        return mCurrentStageIndex == Const.SONG_INFO.length - 1;
    }

    //当点击删除按钮时触发此方法
    private void handleDeleteButton() {
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDialo(ID_DIALOG_DELETE_WORD);
            }
        });
    }

    //当点击提示按钮触发此方法
    private void handleTipButton() {
        mTipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDialo(ID_DIALOG_TIP_WORD);
            }
        });
    }

    //增加金币或者减少金币的方法
    private boolean handleCoins(int coins) {
        if (mCurrentCoins + coins >= 0) {
            mCurrentCoins += coins;
            mViewCurrentCoins.setText(mCurrentCoins + "");
            return true;
        }
        return false;
    }

    //返回删除按钮所需要的金币数
    private int getDeleteCoins() {
        return getResources().getInteger(R.integer.pay_delete_word);
    }

    //返回提示按钮所以需要的金币数
    private int getTipCoins() {
        return getResources().getInteger(R.integer.pay_tip_answer);
    }

    //删除下面的一个不是答案的文字
    private void deletedOneWord() {
        if (handleCoins(-getDeleteCoins())) {
            WordButton word = findNotAnswerWord();
            if (word == null) {
                handleCoins(getDeleteCoins());
                Toast.makeText(this, "无法在删除了亲，剩下的就是答案了", Toast.LENGTH_SHORT).show();
            } else {
                setButtonVisiable(word, View.GONE);
            }
        } else {
            showConfirmDialo(ID_DIALOG_LOCK_COINS);
        }
    }

    //找到一个不是答案且可见的文字，将其隐藏
    private WordButton findNotAnswerWord() {
        WordButton word;
        while (true) {
            int cnt = 0;
            for (int i = 0; i < mAllWords.size(); i++) {
                word = mAllWords.get(i);
                if (word.mIsVisiable) {
                    cnt++;
                }
            }
            if (cnt == mCurrentSong.getNmaeLength()) {
                return null;
            }
            int position = (int) (Math.random() * mAllWords.size());
            word = mAllWords.get(position);
            if (word.mIsVisiable && !isAnswerWord(word)) {
                return word;
            }
        }
    }

    //判断一个字是否是答案之一
    private boolean isAnswerWord(WordButton word) {
        boolean result = false;
        for (int i = 0; i < mCurrentSong.getNmaeLength(); i++) {
            if (word.mString.equals("" + mCurrentSong.getNameChar()[i])) {
                result = true;
                break;
            }
        }
        return result;
    }

    //提示按钮的功能
    private void tipAnswer() {
        if (handleCoins(-getTipCoins())) {
            boolean findIt = false;
            for (int i = 0; i < mBtnSelectWords.size(); i++) {
                if (mBtnSelectWords.get(i).mString.equals("")) {
                    onWordButtonClick(findOneAnswerWord(i));
                    findIt = true;
                    break;
                }
            }
            if (!findIt) {
                handleCoins(getTipCoins());
            }
        } else {
            showConfirmDialo(ID_DIALOG_LOCK_COINS);
        }
    }

    //找到一个答案并返回
    private WordButton findOneAnswerWord(int position) {
        WordButton word;
        for (int i = 0; i < mAllWords.size(); i++) {
            word = mAllWords.get(i);
            if (word.mString.equals(mCurrentSong.getNameChar()[position] + "")) {
                return word;
            }
        }
        return null;
    }

    //删除错误答案的事件响应
    private IAlertDialogButtonListener mDeleteButtonListener = new IAlertDialogButtonListener() {
        @Override
        public void onClick() {
            deletedOneWord();
        }
    };

    //提示正确答案的事件响应
    private IAlertDialogButtonListener mTipButtonListener = new IAlertDialogButtonListener() {
        @Override
        public void onClick() {
            tipAnswer();
        }
    };

    //缺钱的事件响应
    private IAlertDialogButtonListener mLockButtonListener = new IAlertDialogButtonListener() {
        @Override
        public void onClick() {
            handleCoins(1000);
        }
    };

    //点击分享按钮时触发
    public void notShare(View view) {
        Toast.makeText(this, "就不让你分享！！！！！", Toast.LENGTH_SHORT).show();
    }

    //按照指令显示对话框
    private void showConfirmDialo(int id) {
        switch (id) {
            case ID_DIALOG_DELETE_WORD:
                Util.showDialog(this, "确认花掉" + getDeleteCoins() + "金币删除一个错误答案吗?",
                        mDeleteButtonListener);
                break;
            case ID_DIALOG_TIP_WORD:
                Util.showDialog(this, "确认花掉" + getTipCoins() + "金币提示一个正确答案吗？",
                        mTipButtonListener);
                break;
            case ID_DIALOG_LOCK_COINS:
                Util.showDialog(this, "金币不足，去商店补充？", mLockButtonListener);
        }
    }
}
