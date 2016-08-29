package com.owlab.calendarprovidertest;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Arrays;
import java.util.Calendar;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int RC_READ_WRITE_CALENDAR_4_CALENDARS = 13;
    private static final int RC_READ_WRITE_CALENDAR_4_EVENTS = 14;
    private static final int RC_READ_WRITE_CALENDAR_4_INSTANCES = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCalendars();
        getEvents();
        getInstances();
    }

    @AfterPermissionGranted(RC_READ_WRITE_CALENDAR_4_CALENDARS)
    private void getCalendars() {
        Log.d(TAG, "getCalendars() called");
        // Projection array. Creating indices for this array instead of doing
        // dynamic lookups improves performance...
        final String[] CALENDARS_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.Calendars.OWNER_ACCOUNT,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.NAME,
                CalendarContract.Calendars.VISIBLE
        };

        // The indices for the projection array above.
        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        final int PROJECTION_ACCOUNT_TYPE_INDEX = 2;
        final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
        final int PROJECTION_DISPLAY_NAME_INDEX = 4;
        final int PROJECTION_NAME_INDEX = 5;
        final int PROJECTION_VISIBLE_INDEX = 5;

        Cursor cursor = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        //String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ? ) AND ("
        //        + CalendarContract.Calendars.ACCOUNT_TYPE + " = ? ) AND ("
        //        + CalendarContract.Calendars.OWNER_ACCOUNT + " = ? ))";
        //String[] selectionArgs = new String[]{"zeandoo@hotmail.com", "com.android.exchange", "zeandoo@hotmail.com"};
        String selection = "(" + CalendarContract.Calendars.ACCOUNT_NAME + " = ? ) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ? ) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ? )";

        // For google calendar
        //String[] selectionArgs = new String[]{"nemo.owlab@gmail.com", "com.google", "nemo.owlab@gmail.com"};
        // For exchange calendar
        String[] selectionArgs = new String[]{"zenadoo@hotmail.com", "com.android.exchange", "zenadoo@hotmail.com"};

        //if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
        //    // TODO: Consider calling
        //    //    ActivityCompat#requestPermissions
        //    // here to request the missing permissions, and then overriding
        //    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //    //                                          int[] grantResults)
        //    // to handle the case where the user grants the permission. See the documentation
        //    // for ActivityCompat#requestPermissions for more details.
        //    return;
        //}

        //WRITE_CALENDAR not granted!
        String[] permissions = {Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};
        //String[] permissions = {Manifest.permission.WRITE_CALENDAR};
        //String[] permissions = {Manifest.permission.READ_CALENDAR};
        if(EasyPermissions.hasPermissions(this, permissions)) {
            Log.d(TAG, "Permissions granted");
            cursor = cr.query(uri, CALENDARS_PROJECTION, selection, selectionArgs, null);
            //cursor = cr.query(uri, EVENT_PROJECTION, null, null, null);
            Log.d(TAG, "Cursor count: " + cursor.getCount());
            while(cursor.moveToNext()) {
                long callID = 0;
                String displayName = null;
                String name = null;
                String accountName = null;
                String accountType = null;
                String ownerName = null;
                String visible = null;

                //
                callID = cursor.getLong(PROJECTION_ID_INDEX);
                accountName = cursor.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                accountType = cursor.getString(PROJECTION_ACCOUNT_TYPE_INDEX);
                ownerName = cursor.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
                displayName = cursor.getString(PROJECTION_DISPLAY_NAME_INDEX);
                name = cursor.getString(PROJECTION_NAME_INDEX);
                visible = cursor.getString(PROJECTION_VISIBLE_INDEX);

                //
                Log.d(TAG, "queryTest: " + callID + ", " + accountName + ", " + accountType + ", " + ownerName + ", " + displayName + ", " + name + ", " + visible);
            }
            cursor.close();
        } else {
            Log.d(TAG, "Permissions not granted");
            EasyPermissions.requestPermissions(this, "We need these permissions", RC_READ_WRITE_CALENDAR_4_CALENDARS, permissions);
        }

    }

    // This approach - using Intent will not be used in my calendar
    private void getCalendarOfDate() {
        long startMillis = System.currentTimeMillis();

        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, startMillis);
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
        startActivity(intent);
        //The Calendar app will appear, but this is not of my intention!
    }

    @AfterPermissionGranted(RC_READ_WRITE_CALENDAR_4_EVENTS)
    private void getEvents() {
        Log.d(TAG, "getEvents() called");
        // Projection array. Creating indices for this array instead of doing
        // dynamic lookups improves performance...
        final String[] EVENTS_PROJECTION = new String[]{
                CalendarContract.Events.CALENDAR_ID,
                CalendarContract.Events._ID,
                CalendarContract.Events.ORGANIZER,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.EVENT_LOCATION,
                CalendarContract.Events.DESCRIPTION,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.Events.EVENT_TIMEZONE,
                CalendarContract.Events.EVENT_END_TIMEZONE,
                CalendarContract.Events.DURATION,
                CalendarContract.Events.ALL_DAY,
                CalendarContract.Events.RRULE,
                CalendarContract.Events.RDATE,
                CalendarContract.Events.AVAILABILITY,
                CalendarContract.Events.GUESTS_CAN_MODIFY,
                CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS,
                CalendarContract.Events.GUESTS_CAN_SEE_GUESTS
        };

        // The indices for the projection array above.
        final int PROJECTION_CALENDAR_ID_INDEX =                0;
        final int PROJECTION_ID_INDEX =                         1;
        final int PROJECTION_ORGANIZER_INDEX =                  2;
        final int PROJECTION_TITLE_INDEX =                      3;
        final int PROJECTION_EVENT_LOCATION_INDEX =             4;
        final int PROJECTION_DESCRIPTION_INDEX =                5;
        final int PROJECTION_DTSTART_INDEX =                    6;
        final int PROJECTION_DTEND_INDEX =                      7;
        final int PROJECTION_EVENT_TIMEZONE_INDEX =             8;
        final int PROJECTION_EVENT_END_TIMEZONE_INDEX =         9;
        final int PROJECTION_DURATION_INDEX =                   10;
        final int PROJECTION_ALL_DAY_INDEX =                    11;
        final int PROJECTION_RRULE_INDEX =                      12;
        final int PROJECTION_RDATE_INDEX =                      13;
        final int PROJECTION_AVAILABILITY_INDEX =               14;
        final int PROJECTION_GUESTS_CAN_MODIFY_INDEX =          15;
        final int PROJECTION_GUESTS_CAN_INVITE_OTHERS_INDEX =   16;
        final int PROJECTION_GUESTS_CAN_SEE_GUESTS_INDEX =      17;

        Cursor cursor = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;

        //String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ? ) AND ("
        //        + CalendarContract.Calendars.ACCOUNT_TYPE + " = ? ) AND ("
        //        + CalendarContract.Calendars.OWNER_ACCOUNT + " = ? ))";
        //String[] selectionArgs = new String[]{"zeandoo@hotmail.com", "com.android.exchange", "zeandoo@hotmail.com"};
        String selection = "(" + CalendarContract.Calendars.ACCOUNT_NAME + " = ? ) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ? ) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ? )";

        // For google calendar
        //String[] selectionArgs = new String[]{"nemo.owlab@gmail.com", "com.google", "nemo.owlab@gmail.com"};
        // For exchange calendar
        String[] selectionArgs = new String[]{"zenadoo@hotmail.com", "com.android.exchange", "zenadoo@hotmail.com"};

        //if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
        //    // TODO: Consider calling
        //    //    ActivityCompat#requestPermissions
        //    // here to request the missing permissions, and then overriding
        //    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //    //                                          int[] grantResults)
        //    // to handle the case where the user grants the permission. See the documentation
        //    // for ActivityCompat#requestPermissions for more details.
        //    return;
        //}

        //WRITE_CALENDAR not granted!
        String[] permissions = {Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};
        //String[] permissions = {Manifest.permission.WRITE_CALENDAR};
        //String[] permissions = {Manifest.permission.READ_CALENDAR};
        if(EasyPermissions.hasPermissions(this, permissions)) {
            Log.d(TAG, "Permissions granted");
            //cursor = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
            cursor = cr.query(uri, EVENTS_PROJECTION, null, null, null);
            Log.d(TAG, "Cursor count: " + cursor.getCount());
            while(cursor.moveToNext()) {
                //Log.d(TAG, cursor.toString());
                String calendarId = cursor.getString(PROJECTION_CALENDAR_ID_INDEX);
                String eventId = cursor.getString(PROJECTION_ID_INDEX);
                String organizer = cursor.getString(PROJECTION_ORGANIZER_INDEX);
                String title = cursor.getString(PROJECTION_TITLE_INDEX);
                String eventLocation = cursor.getString(PROJECTION_EVENT_LOCATION_INDEX);
                String description = cursor.getString(PROJECTION_DESCRIPTION_INDEX);
                String dtstart = cursor.getString(PROJECTION_DTSTART_INDEX);
                String dtend = cursor.getString(PROJECTION_EVENT_TIMEZONE_INDEX);
                String eventEndTimezone = cursor.getString(PROJECTION_EVENT_END_TIMEZONE_INDEX);
                String duration = cursor.getString(PROJECTION_DURATION_INDEX);
                String allDay = cursor.getString(PROJECTION_ALL_DAY_INDEX);
                String rrule = cursor.getString(PROJECTION_RRULE_INDEX);
                String rdate = cursor.getString(PROJECTION_RDATE_INDEX);
                String availability = cursor.getString(PROJECTION_AVAILABILITY_INDEX);
                String guestsCanModify = cursor.getString(PROJECTION_GUESTS_CAN_MODIFY_INDEX);
                String guestsCanInviteOthers = cursor.getString(PROJECTION_GUESTS_CAN_INVITE_OTHERS_INDEX);
                String guestsCanSeeGuests = cursor.getString(PROJECTION_GUESTS_CAN_SEE_GUESTS_INDEX);

                Log.d(TAG, "Event = (" + calendarId
                        + ", " + eventId
                        + ", " + organizer
                        + ", " + title
                        + ", " + eventLocation
                        + ", " + description
                        + ", " + dtstart
                        + ", " + dtend
                        + ", " + eventEndTimezone
                        + ", " + duration
                        + ", " + allDay
                        + ", " + rrule
                        + ", " + rdate
                        + ", " + availability
                        + ", " + guestsCanModify
                        + ", " + guestsCanInviteOthers
                        + ", " + guestsCanSeeGuests
                        + ")"
                );

            }
            cursor.close();
        } else {
            Log.d(TAG, "Permissions not granted");
            EasyPermissions.requestPermissions(this, "We need these permissions", RC_READ_WRITE_CALENDAR_4_EVENTS, permissions);
        }

    }

    @AfterPermissionGranted(RC_READ_WRITE_CALENDAR_4_INSTANCES)
    private void getInstances() {
        Log.d(TAG, "getInstances() called");
        // Projection array. Creating indices for this array instead of doing
        // dynamic lookups improves performance...
        final String[] INSTANCES_PROJECTION = new String[]{
                CalendarContract.Instances.EVENT_ID,
                CalendarContract.Instances.TITLE,
                CalendarContract.Instances.BEGIN,
                CalendarContract.Instances.START_DAY,
                CalendarContract.Instances.START_MINUTE,
                CalendarContract.Instances.END,
                CalendarContract.Instances.END_DAY,
                CalendarContract.Instances.END_MINUTE
        };

        // The indices for the projection array above.
        final int PROJECTION_EVENT_ID_INDEX =                0;
        final int PROJECTION_TITLE_INDEX =                         1;
        final int PROJECTION_BEGIN_INDEX =                  2;
        final int PROJECTION_START_DAY_INDEX =                      3;
        final int PROJECTION_START_MINUTE_INDEX =             4;
        final int PROJECTION_END_INDEX =                5;
        final int PROJECTION_END_DAY_INDEX =                    6;
        final int PROJECTION_END_DAY_MINUTE_INDEX =                      7;

        ContentResolver cr = getContentResolver();
        Uri.Builder uriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2016, 1, 1, 0, 0);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2016, 12, 31, 24, 59);
        ContentUris.appendId(uriBuilder, beginTime.getTimeInMillis());
        ContentUris.appendId(uriBuilder, endTime.getTimeInMillis());
        Uri uri = uriBuilder.build();

        //String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ? ) AND ("
        //        + CalendarContract.Calendars.ACCOUNT_TYPE + " = ? ) AND ("
        //        + CalendarContract.Calendars.OWNER_ACCOUNT + " = ? ))";
        //String[] selectionArgs = new String[]{"zeandoo@hotmail.com", "com.android.exchange", "zeandoo@hotmail.com"};
        String selection = "(" + CalendarContract.Calendars.ACCOUNT_NAME + " = ? ) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ? ) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ? )";

        // For google calendar
        //String[] selectionArgs = new String[]{"nemo.owlab@gmail.com", "com.google", "nemo.owlab@gmail.com"};
        // For exchange calendar
        String[] selectionArgs = new String[]{"zenadoo@hotmail.com", "com.android.exchange", "zenadoo@hotmail.com"};

        //if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
        //    // TODO: Consider calling
        //    //    ActivityCompat#requestPermissions
        //    // here to request the missing permissions, and then overriding
        //    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //    //                                          int[] grantResults)
        //    // to handle the case where the user grants the permission. See the documentation
        //    // for ActivityCompat#requestPermissions for more details.
        //    return;
        //}

        //WRITE_CALENDAR not granted!
        String[] permissions = {Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};
        //String[] permissions = {Manifest.permission.WRITE_CALENDAR};
        //String[] permissions = {Manifest.permission.READ_CALENDAR};
        if(EasyPermissions.hasPermissions(this, permissions)) {
            Log.d(TAG, "Permissions granted");
            //cursor = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
            Cursor cursor = cr.query(uri, INSTANCES_PROJECTION, null, null, null);
            Log.d(TAG, "Cursor count: " + cursor.getCount());
            while(cursor.moveToNext()) {
                //Log.d(TAG, cursor.toString());
                String eventId = cursor.getString(PROJECTION_EVENT_ID_INDEX);
                String title = cursor.getString(PROJECTION_TITLE_INDEX);
                String begin = cursor.getString(PROJECTION_BEGIN_INDEX);
                String startDay = cursor.getString(PROJECTION_START_DAY_INDEX);
                String startMinute = cursor.getString(PROJECTION_START_MINUTE_INDEX);
                String end = cursor.getString(PROJECTION_END_INDEX);
                String endDay = cursor.getString(PROJECTION_END_DAY_INDEX);
                String endMinute = cursor.getString(PROJECTION_END_DAY_MINUTE_INDEX);

                Log.d(TAG, "Event = (" + eventId
                        + ", " + title
                        + ", " + begin
                        + ", " + startDay
                        + ", " + startMinute
                        + ", " + end
                        + ", " + endDay
                        + ", " + endMinute
                        + ")"
                );

            }
            cursor.close();
        } else {
            Log.d(TAG, "Permissions not granted");
            EasyPermissions.requestPermissions(this, "We need these permissions", RC_READ_WRITE_CALENDAR_4_INSTANCES, permissions);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "Permissions request results received: requestCode = " + requestCode + ", permissions = " + Arrays.toString(permissions) + ", results = " + Arrays.toString(grantResults));
        //Log.d(TAG, "0 " + (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED));
        //Log.d(TAG, "1 " + (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED));

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
