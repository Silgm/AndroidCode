package com.xkf.guessmusicgame.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xkf.guessmusicgame.R;
import com.xkf.guessmusicgame.data.Const;
import com.xkf.guessmusicgame.model.IAlertDialogButtonListener;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {

    private static AlertDialog mAlterDialog;

    //加载一个布局文件并返回
    public static View getView(Context context, int layoutId) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutId, null);
        return view;
    }


    //显示对话框
    public static void showDialog
    (final Context context, String message, final IAlertDialogButtonListener listener) {
        View dialogView = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Transparent);
        dialogView = getView(context, R.layout.dialog_layout);
        ImageButton yes_button = (ImageButton) dialogView.findViewById(R.id.dialog_yes_button);
        ImageButton cancel_button = (ImageButton) dialogView.findViewById(R.id.dialog_cancel_button);
        TextView dialog_message = (TextView) dialogView.findViewById(R.id.dialog_message);
        dialog_message.setText(message);
        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAlterDialog != null) {
                    mAlterDialog.cancel();
                }
                if (listener != null) {
                    listener.onClick();
                }
                MyPlayer.playTone(context, MyPlayer.SOUND_ENTER);
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAlterDialog != null) {
                    mAlterDialog.cancel();
                }
                MyPlayer.playTone(context, MyPlayer.SOUND_CANCEL);
            }
        });
        builder.setView(dialogView);
        mAlterDialog = builder.create();
        mAlterDialog.show();
    }

    public static void saveData(Context context, int stageIndex, int coins) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(Const.FILE_NAME_SAVE_DATA, Context.MODE_PRIVATE);
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
            dataOutputStream.writeInt(stageIndex);
            dataOutputStream.writeInt(coins);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int[] loadData(Context context) {
        FileInputStream fileInputStream = null;
        int[] data = {-1, Const.TOTAL_COINS};
        try {
            fileInputStream = context.openFileInput(Const.FILE_NAME_SAVE_DATA);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            data[Const.INDEX_LOAD_SAVESTAGE] = dataInputStream.readInt();
            data[Const.INDEX_LOAD_SAVECOINS] = dataInputStream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
}
