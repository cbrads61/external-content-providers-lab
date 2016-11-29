package com.colinbradley.externalcontentproviderslab;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by colinbradley on 11/28/16.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.CalenderHolder> {

    private List<Event> mCalenderList;
    Calendar mCalender;

    public Adapter(List<Event> calenderList){
        mCalenderList = calenderList;
        mCalender = Calendar.getInstance();
    }

    @Override
    public CalenderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CalenderHolder(inflater.inflate(R.layout.event_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final CalenderHolder holder, int position) {
        holder.mName.setText(mCalenderList.get(position).getmName());

        String date = mCalenderList.get(position).getmDate();

        holder.mDate.setText(date);

        holder.mName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                long id = mCalenderList.get(holder.getAdapterPosition()).getmID();
                Uri uriWithID = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI,id);

                ContentResolver contentResolver = view.getContext().getContentResolver();
                contentResolver.delete(uriWithID, null, null);

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCalenderList.size();
    }

    public void swapData (Cursor cursor){
        mCalenderList.clear();

        if (cursor != null && cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                String name = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE));

                long dateInMillis = cursor.getInt(cursor.getColumnIndex(CalendarContract.Events.DTSTART));

                mCalender.setTimeInMillis(dateInMillis);
                int year = mCalender.get(Calendar.YEAR);
                int month = mCalender.get(Calendar.MONTH);
                int day = mCalender.get(Calendar.DAY_OF_MONTH);
                String date = month + "/" + day + "/" + year;

                long id = cursor.getLong(cursor.getColumnIndex(CalendarContract.Events._ID));


                mCalenderList.add(new Event(name,date,id));
                cursor.moveToNext();
            }
        }
        notifyDataSetChanged();
    }

    public class CalenderHolder extends RecyclerView.ViewHolder{

        TextView mName, mDate;

        public CalenderHolder(View itemView) {
            super(itemView);

            mName = (TextView)itemView.findViewById(R.id.name);
            mDate = (TextView)itemView.findViewById(R.id.date);
        }
    }

}
