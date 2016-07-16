package com.brainupco.geotimeclock;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jsantos on 15/jul/2016.
 */
public class CheckOutJson {
    /**
     * Log Tag.
     */
    private final String LOG_TAG = this.getClass().getName();

    // Parse json data
    public String status;
    public String distance;

    // These are the names of the JSON objects that need to be extracted.
    private final String OCI_STATUS = "Status";
    private final String OCI_DISTANCE = "Distance";


    public CheckOutJson(String jsonStr){

        try{
            // Parse Json String
            JSONObject resultJson = new JSONObject(jsonStr);

            // Parse Objects
            status = resultJson.getString(OCI_STATUS);
            distance = resultJson.getString(OCI_DISTANCE);

        }
        catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }

}
