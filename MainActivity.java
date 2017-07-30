package com.bk.nativejavascriptapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;


/*

    Developed By Nuh Burak Karakaya
    Project was Licanced with GPL
    Available for Personal Usage
    Permission must be obtained for Commercial Usage
    for contact please send an email to burakkarakaya10@gmail.com

*/


public class MainActivity extends AppCompatActivity
{

    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.mWebView);
        mWebView.clearCache(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        mWebView.loadUrl("file:///android_asset/file.html");
    }

    public class JavaScriptInterface
    {
        Context mContext;

        JavaScriptInterface(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public void info(String username,String password)
        {
            addUser(username,password);

        }
    }

    public void addUser(String user,String pass)
    {
        Boolean has = false;
        AppDatabaseHelper helper=new AppDatabaseHelper(getApplicationContext());
        SQLiteDatabase db=helper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM Users where username='"+user+"'", null);
        if(c.moveToFirst()){
            do
            {
                has = true;
                Toast.makeText(getApplicationContext(),"User is already exist!",Toast.LENGTH_SHORT).show();
            } while(c.moveToNext());
        }
        c.close();
        db.close();

        if(!has)
        {
            SQLiteDatabase db2 = helper.getWritableDatabase();
            try
            {
                ContentValues contentValues=new ContentValues();
                contentValues.put("username",user);
                contentValues.put("password",pass);

                Long result=db2.insert("Users",null,contentValues);
                Toast.makeText(getApplicationContext(),"User was Signed UP!",Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"User could not be Signed UP!",Toast.LENGTH_SHORT).show();
            }
            finally
            {
                db2.close();
            }
        }
    }
}
