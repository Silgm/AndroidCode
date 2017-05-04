package com.cameraalbumtest.xkf.cameraalbumtest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;
    private Button takePhoto;
    private Button chooseAlbum;
    private ImageView mImageView;
    private Uri imageUri;
    private static final int CHHOSE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        takePhoto = (Button) findViewById(R.id.take_photo);
        mImageView = (ImageView) findViewById(R.id.picture);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建一个文件来保存等会拍下来的照片
                File file = new File(getExternalCacheDir(), "output_image.jpg");
                if (file.exists()) {
                    file.delete();
                }
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //获取这个文件的uri
                if (Build.VERSION.SDK_INT >= 24) {
                    //这里使用到了FileProvider，所以还要在清单文件中注册它
                    imageUri = FileProvider.getUriForFile(MainActivity.this,
                            "com.cameraalbumtest.xkf.cameraalbumtest.fileprovider", file);

                } else {
                    imageUri = Uri.fromFile(file);
                }
                //Toast.makeText(MainActivity.this, imageUri.toString(), Toast.LENGTH_LONG).show();
                //intent 的隐式调用，系统会找到响应这个uri的程序并且执行它
                //这里他就会找到相机，然后调用相机
                //然后使用startActivityForResult方法，这样收到结果会执行onActivityResult方法
                //然后就可以在这个方法中处理拍到的照片了
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                //这一步必须要有
                //intent作了一个处理，把imageUri作为内容放了进来，这样在onActivityResult中就可以读出相对应的文件了
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                //这里还需要一个int值来作为结果处理时候的标识码
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });
        chooseAlbum = (Button) findViewById(R.id.choose_from_album);
        chooseAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream
                                (getContentResolver().openInputStream(imageUri));
                        mImageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
        }
    }

}
