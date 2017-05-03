package com.firstcodeofandroid.xkf.firstcodeofandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;


public class MainActivity extends AppCompatActivity {
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        tabHost = (TabHost) findViewById(android.R.id.tabhost);
//        tabHost.setup();
//        TabSpec page1 = tabHost.newTabSpec("tab1")
//                .setIndicator("叫兽")
//                .setContent(R.id.tab1);
//        tabHost.addTab(page1);
//
//        TabSpec page2 = tabHost.newTabSpec("tab2")
//                .setIndicator("老湿")
//                .setContent(R.id.tab2);
//        tabHost.addTab(page2);
//
//        TabSpec page3 = tabHost.newTabSpec("tab3")
//                .setIndicator("哪吒")
//                .setContent(R.id.tab3);
//        tabHost.addTab(page3);
    }

//    public void createDatabase(View view) {
//        Connector.getDatabase();
//    }
//
//    public void addData(View view) {
//        Book book = new Book();
//        book.setName("In Code We Trust");
//        book.setAuthor("XKF");
//        book.setPages(520);
//        book.setPress("Code");
//        book.setPrice(18);
//        book.save();
//    }
//
//    public void upDate(View view) {
//        Book book = new Book();
//        book.setPages(600);
//        book.updateAll();
//    }
//
//    public void deleteData(View view) {
//        DataSupport.deleteAll(Book.class, "pages = ?", "520");
//    }
//
//    public void queryData(View view) {
//        List<Book> books = DataSupport.findAll(Book.class);
//        Book book = books.get(0);
//        Log.e("dalongmao", book.getAuthor());
//        Log.e("dalongmao", book.getName());
//        Log.e("dalongmao", book.getPress());
//        Log.e("dalongmao", "" + book.getPages());
//        Log.e("dalongmao", book.getPrice() + "");
//    }
}
