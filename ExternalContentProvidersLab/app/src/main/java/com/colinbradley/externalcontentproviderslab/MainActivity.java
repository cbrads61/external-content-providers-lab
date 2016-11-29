package com.colinbradley.externalcontentproviderslab;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    Adapter mAdapter;

    public static final int CALENDER_LOADER = 0;

    public static final int CALENDER_REQUEST = 1;
    public static String[] PERMISSIONS = {Manifest.permission.READ_CALENDAR,
                                          Manifest.permission.WRITE_CALENDAR};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new Adapter(new ArrayList<Event>());
        recyclerView.setAdapter(mAdapter);

        permissionsCheck(this);

        int permissions = ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_CALENDAR);
        if (permissions == PackageManager.PERMISSION_GRANTED){
            getSupportLoaderManager().initLoader(CALENDER_LOADER, null, this);

        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case CALENDER_LOADER:
                return new CursorLoader(this,
                        CalendarContract.Events.CONTENT_URI,
                        new String[]{CalendarContract.Events._ID,
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DTSTART},
                        null,null,
                        "dtstart ASC");
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.swapData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mAdapter.swapData(null);
    }

    public static void permissionsCheck(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR);
        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,PERMISSIONS, CALENDER_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CALENDER_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getSupportLoaderManager().initLoader(CALENDER_LOADER,null,this);
                }
        }
    }
}
