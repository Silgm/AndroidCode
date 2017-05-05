package com.cameraalbumtest.xkf.cameraalbumtest;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;
    private Button takePhoto;
    private Button chooseAlbum;
    private ImageView mImageView;
    private Uri imageUri;
    private static final int CHOOSE_PHOTO = 2;

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
                //调用图库需要申请权限
                //先检查是不是有权限
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //没有权限就提出申请
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    //申请权限的话，结果会调用onRequestPermissionsResult方法，所以重写它
                } else {
                    //有的话就调用这个方法来打开图库
                    openAlbum();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //参数grantResults放的是申请的结果的集合
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openAlbum();
        } else {
            Toast.makeText(this, "你取消了打开图库的权限，所以不能调用", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAlbum() {
        //同样的，隐式打开图库的程序
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        //设置一下打开文件的类型，不然会打开一堆
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //加载并且显示图片
                        Bitmap bitmap = BitmapFactory.decodeStream
                                (getContentResolver().openInputStream(imageUri));
                        mImageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //这里对于android 4.4有着不同的处理
                    if (Build.VERSION.SDK_INT >= 19) {
                        //对于大于android 4.4的版本，选取图库中的版本是返回一个封装过的uri
                        handleImageOnKitKat(data);
                    } else {
                        //对于低于android 4.4的版本，选取图库中的图片是返回一个uri，然后访问
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
        }
    }

    private void handleImageOnKitKat(Intent data) {
        //content://com.android.providers.media.documents/document/image%3A74
        //假设这是图片路径的uri，现在需要解析它
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }
        //它现在可能是content打头的，这样的话就调用getImagePath方法来解析
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        }
        //也有可能是file打头的，直接过去路径就好
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        //调用这个方法来加载显示图片
        displayImage(imagePath);
    }

    private void displayImage(String path) {
        if (path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            mImageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "加载失败", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void handleImageBeforeKitKat(Intent data) {

    }


}
