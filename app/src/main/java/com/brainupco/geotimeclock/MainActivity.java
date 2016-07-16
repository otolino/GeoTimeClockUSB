package com.brainupco.geotimeclock;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    /**
     * Log Tag.
     */
    private final String LOG_TAG = this.getClass().getName();

    private ProgressDialog dialogPD;
    private String mIMEI;
    private String mAssetStatus;
    private String mProcessType;
    private boolean mCheckDistance;
    Date mclickTime;
    String mclickTimeStr;
    String mclickTimeUTCStr;

    // Layout Elements
    private ImageView status_icon_image;
    private TextView status_icon_text;
    private LinearLayout dataZone;
    private TextView starting_time;
    private Chronometer elapsed_time;
    private TextView task;
    private ImageButton ib_checkin;
    private TextView lbl_ib_checkin;
    private ImageButton ib_checkout;
    private TextView lbl_ib_checkout;

    // Date and Time Handling
    String patternTime = "HH:mm:ss";
    SimpleDateFormat sdfTime = new SimpleDateFormat(patternTime);
    String patternFull = "yyyy-MM-dd HH:mm:ss z";
    SimpleDateFormat sdfFull = new SimpleDateFormat(patternFull);
    String patternUTC = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat sdfUTC = new SimpleDateFormat(patternUTC);

    private Float disableAlpha = 0.5f;
    private Float enableAlpha = 1.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Action Bar Icon
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.ic_launcher);

        // Get Layout Elements
        status_icon_image = (ImageView) findViewById(R.id.status_icon_image);
        status_icon_text = (TextView) findViewById(R.id.status_icon_text);
        dataZone = (LinearLayout) findViewById(R.id.dataZone);
        starting_time = (TextView) findViewById(R.id.txt_starting_time);
        elapsed_time = (Chronometer) findViewById(R.id.chm_elapsed_time);
        task = (TextView) findViewById(R.id.txt_task);
        ib_checkin = (ImageButton) findViewById(R.id.ib_checkin);
        lbl_ib_checkin = (TextView) findViewById(R.id.lbl_ib_checkin);
        ib_checkout = (ImageButton) findViewById(R.id.ib_checkout);
        lbl_ib_checkout = (TextView) findViewById(R.id.lbl_ib_checkout);

        // Ensure Applying Preferences Default (Only first time)
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

        // Get IMEI
        // Check if IMEI is in preferences (testphone: 868442014378892)
        mIMEI = Utility.getSavedAssetIMEI(this);
        if (mIMEI == null || mIMEI == "") {
            // Try to read Device IMEI
            mIMEI = Utility.getDeviceIMEI(this);

            // Save Value
            Utility.setAssetIMEI(this, mIMEI);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Show Status
        displayStatus();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Show Settings
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayStatus() {
        // Variables
        Date starting_time_date;

        // Get Preferences Data
        mIMEI = Utility.getSavedAssetIMEI(this);
        mAssetStatus = Utility.getSavedAssetStatus(this);
        status_icon_text.setText(mAssetStatus);

        // Adjust Layout Based on Status
        if (mAssetStatus.equalsIgnoreCase(getString(R.string.status_available))) {
            // Available

            // Stop Chronometer
            elapsed_time.stop();

            // Clean Form
            status_icon_image.setImageResource(R.drawable.available);
            starting_time.setText("");
            elapsed_time.setText("00:00");
            task.setText("");
            dataZone.setVisibility(View.INVISIBLE);

            // Adjust Buttons
            lbl_ib_checkin.setEnabled(true);
            ib_checkin.setAlpha(enableAlpha);
            ib_checkin.setEnabled(true);
            lbl_ib_checkout.setEnabled(false);
            ib_checkout.setAlpha(disableAlpha);
            ib_checkout.setEnabled(false);

        } else {
            // Working

            // Set Chronometer
            try {
                starting_time_date = sdfFull.parse(Utility.getSavedAssetStartingTime(this));
                Date now = new Date();
                long elapsedTime = now.getTime() - starting_time_date.getTime(); //startTime is whatever time you want to start the chronometer from. you might have stored it somwehere

                elapsed_time.setText("00:00");
                Long timeInMilli = 0L; //starting_time_date.getTime();
                elapsed_time.setBase(SystemClock.elapsedRealtime() - elapsedTime);
                elapsed_time.start();

                // Show Starting Hour
                starting_time.setText(sdfTime.format(starting_time_date));


            } catch (ParseException e) {
                // Exception handling goes here
                return;
            }

            // Set Form
            status_icon_image.setImageResource(R.drawable.working);
            task.setText(Utility.getSavedAssetTask(this));
            dataZone.setVisibility(View.VISIBLE);

            // Adjust Buttons
            lbl_ib_checkin.setEnabled(false);
            ib_checkin.setAlpha(disableAlpha);
            ib_checkin.setEnabled(false);
            lbl_ib_checkout.setEnabled(true);
            ib_checkout.setAlpha(enableAlpha);
            ib_checkout.setEnabled(true);

        }
    }

    // Set Check In Button Action
    public void checkInClick(View view) {
        // Indicate To Process CheckIn
        mProcessType = getString(R.string.type_checkIn);

        // Indicate To Check Distance
        mCheckDistance = true;

        // Process Click
        processClick();

    }

    // Set Check Out Button Action
    public void checkOutClick(View view) {
        // Indicate To Process CheckOut
        mProcessType = getString(R.string.type_checkOut);

        // Indicate To Check Distance
        mCheckDistance = true;

        // Process Click
        processClick();
    }

    public void processClick(){

        String title;

        // Get Starting Time (To Ensure time sincronization in site)
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        mclickTime = now.getTime();
        mclickTimeStr = sdfFull.format(mclickTime);
        sdfUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        mclickTimeUTCStr = sdfUTC.format(mclickTime);

        // Select title for dialog
        if (mProcessType.equalsIgnoreCase(getString(R.string.type_checkIn))){
            title = getString(R.string.msg_checkin_process_title);
        }
        else
        {
            title = getString(R.string.msg_checkout_process_title);
        }

        // Show Wait Message
        dialogPD = Utility.createPD(this, title, getString(R.string.msg_wait));
        dialogPD.show();

        // Try to Get Activity, based on Location
        MyLocation processLocation = new MyLocation();
        processLocation.init(this, myLocationResult);

    }

    public MyLocation.LocationResult myLocationResult = new MyLocation.LocationResult() {

        final String LOG_TAG = this.getClass().getName();

        @Override
        public void gotLocation(Context context, final Location location) {
            // Variables
            String message = "";
            String assetLatitude = "";
            String assetLongitude = "";

            // do something
            Log.d(LOG_TAG, "gotLocation");

            if (location != null) {
                // Process Click
                new AsyncTaskRunner().execute(
                        Double.toString(location.getLatitude()),
                        Double.toString(location.getLongitude())
                );

            } else {
                // No Location Found Close Dialog and Show Proper Message
                // Close wait message
                dialogPD.dismiss();

                String title;

                // Select title for dialog
                if (mProcessType.equalsIgnoreCase(getString(R.string.type_checkIn))){
                    title = getString(R.string.msg_checkin_result_title);
                }
                else
                {
                    title = getString(R.string.msg_checkout_result_title);
                }

                // Show No GPS Message
                Utility.showDialog(context, title, getString(R.string.msg_no_gps));

            }

        }

    };



    /**
     * @author Prabu
     *         Private class which runs the long operation. ( Sleeping for some time )
     */
    private class AsyncTaskRunner extends AsyncTask<String, String, String[]> {

        /**
         * Log Tag.
         */
        private final String LOG_TAG = this.getClass().getName();

        private String[] response = new String[3];

        @Override
        protected String[] doInBackground(String... params) {

            // Variables
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Get Parameters and Saved Values
            String assetLatitude = params[0];
            String assetLongitude = params[1];
            String idTask = Utility.getSavedAssetIdTask(MainActivity.this);

            response[0] = mProcessType;
            response[1] = getString(R.string.connection_result_error);


            try {

                // Construct the URL Based on Process Type
                //final String Mobile_Location_BASE_URL = "http://web-mobilelocation.azurewebsites.net/Home/UMPE?";
                final String Mobile_Location_BASE_URL = "https://geotimeclock.azurewebsites.net/api";
                final String IMEI_PARAM = "imei";
                final String START_DATE_PARAM = "sd";
                final String FINISH_DATE_PARAM = "fd";
                final String LAT_PARAM = "lat";
                final String LON_PARAM = "lon";
                final String ID_TASK_PARAM = "idt";
                final String CHECK_DISTANCE = "chkdt";

                // Build URL Based on Process Type
                Uri builtUri;
                if (mProcessType.equalsIgnoreCase(getString(R.string.type_checkIn))){
                    // Check In
                    builtUri = Uri.parse(Mobile_Location_BASE_URL).buildUpon()
                            .appendPath("checkin")
                            .appendQueryParameter(IMEI_PARAM, mIMEI)
                            .appendQueryParameter(START_DATE_PARAM, mclickTimeUTCStr)
                            .appendQueryParameter(LAT_PARAM, assetLatitude)
                            .appendQueryParameter(LON_PARAM, assetLongitude)
                            .build();

                }
                else
                {
                    // Check Out
                    builtUri = Uri.parse(Mobile_Location_BASE_URL).buildUpon()
                            .appendPath("checkout")
                            .appendQueryParameter(IMEI_PARAM, mIMEI)
                            .appendQueryParameter(FINISH_DATE_PARAM, mclickTimeUTCStr)
                            .appendQueryParameter(LAT_PARAM, assetLatitude)
                            .appendQueryParameter(LON_PARAM, assetLongitude)
                            .appendQueryParameter(ID_TASK_PARAM, idTask)
                            .appendQueryParameter(CHECK_DISTANCE, Boolean.toString(mCheckDistance))
                            .build();
                }

                URL url = new URL(builtUri.toString());

                // Create Request
                Log.v(LOG_TAG, "Sending Status");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream != null) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() != 0) {
                        response[1] = getString(R.string.connection_result_ok);
                        response[2] = buffer.toString();
                    }
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error->IOException: ", e);
            } catch (SecurityException e) {
                Log.e(LOG_TAG, "Error->SecurityException: ", e);
            } catch (IllegalArgumentException e) {
                Log.e(LOG_TAG, "Error->IllegalArgumentException: ", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return response;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String[] result) {

            // Close wait message
            dialogPD.dismiss();

            String dialogTitle;
            // Select title for dialogs
            if (mProcessType.equalsIgnoreCase(getString(R.string.type_checkIn))){
                dialogTitle = getString(R.string.msg_checkin_result_title);
            }
            else
            {
                dialogTitle = getString(R.string.msg_checkout_result_title);
            }

            // json result status:
            // 0 - ok
            // 1 - No Task
            // 2 - Not Yet There

            // Check if Was Processed OK
            if (result[1].equalsIgnoreCase(getString(R.string.connection_result_ok))){

                // Check Process Type
                if (mProcessType.equalsIgnoreCase(getString(R.string.type_checkIn))) {

                    // Parse json data for check in
                    CheckInJson jsonResult = new CheckInJson(result[2]);

                    // Process Results
                    if (jsonResult.status.equalsIgnoreCase("0")){
                        // Set Status to Working
                        Utility.setAssetStatus(MainActivity.this, getString(R.string.status_working));

                        // Save Data in Preferences
                        Utility.setAssetStartingTime(MainActivity.this, mclickTimeStr);
                        Utility.setAssetIdTask(MainActivity.this, jsonResult.idTask);
                        Utility.setAssetTask(MainActivity.this, jsonResult.taskDescription);
                        Utility.setAssetAddress(MainActivity.this, jsonResult.address);
                        Utility.setAssetWorkDuration(MainActivity.this, jsonResult.workduration);

                        // Show Result Message
                        Utility.showDialog(MainActivity.this, dialogTitle, getString(R.string.msg_checkin_ok));

                        // Update UI
                        displayStatus();

                    }
                    else if (jsonResult.status.equalsIgnoreCase("1")) {
                        // No Task
                        Utility.showDialog(MainActivity.this, dialogTitle, getString(R.string.msg_checkin_no_task));
                    }
                    else if (jsonResult.status.equalsIgnoreCase("2")) {
                        // Not Yet
                        Utility.showDialog(MainActivity.this, dialogTitle, String.format(getString(R.string.msg_checkin_not_yet),jsonResult.distance));
                    }
                    else{
                        // Unknown Error
                        Utility.showDialog(MainActivity.this, dialogTitle, getString(R.string.msg_critical_error));
                    }
                }
                else
                {
                    // Parse json data for check out
                    CheckOutJson jsonResult = new CheckOutJson(result[2]);

                    // Process Results
                    if (jsonResult.status.equalsIgnoreCase("0")) {
                        // Show Result Message
                        Utility.showDialog(MainActivity.this, getString(R.string.msg_checkout_result_title), getString(R.string.msg_checkout_ok));

                        // Set Status to Available
                        Utility.setAssetStatus(MainActivity.this, getString(R.string.status_available));
                        displayStatus();
                    }
                    else if (jsonResult.status.equalsIgnoreCase("1")) {
                        // No Task (There was a problem with the task id
                        Utility.showDialog(MainActivity.this, dialogTitle, getString(R.string.msg_checkout_no_task));

                        // Reset Interface
                        // Set Status to Available
                        Utility.setAssetStatus(MainActivity.this, getString(R.string.status_available));
                        displayStatus();
                    }
                    else if (jsonResult.status.equalsIgnoreCase("2")) {
                        // Not Yet, we have to ask the user if he wants to record as an incident.
                        Utility.showDialog(MainActivity.this, dialogTitle, String.format(getString(R.string.msg_checkout_not_yet),jsonResult.distance));
                    }
                    else{
                        // Unknown Error
                        Utility.showDialog(MainActivity.this, dialogTitle, getString(R.string.msg_critical_error));

                        // Reset Interface
                        // Set Status to Available
                        Utility.setAssetStatus(MainActivity.this, getString(R.string.status_available));
                        displayStatus();
                    }
                }
            }
            else
            {
                // Show No Network Message
                Utility.showDialog(MainActivity.this, dialogTitle, getString(R.string.msg_no_network));
            }

        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(String... text) {
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
    }

}

