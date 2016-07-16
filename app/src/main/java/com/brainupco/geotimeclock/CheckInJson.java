package com.brainupco.geotimeclock;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jsantos on 30/may/2016.
 */
public class CheckInJson {

    /**
     * Log Tag.
     */
    private final String LOG_TAG = this.getClass().getName();

    // Parse json data
    public String status;
    public String idTask;
    public String taskDescription;
    public String address;
    public String distance;
    public String heading;
    public String workduration;

    // These are the names of the JSON objects that need to be extracted.
    private final String OCI_STATUS = "Status";
    private final String OCI_ID_TASK = "IdTask";
    private final String OCI_TASK_DESC = "TaskDescription";
    private final String OCI_ADDRESS = "Address";
    private final String OCI_DISTANCE = "Distance";
    private final String OCI_HEADING = "Heading";
    private final String OCI_WORK_DURATION = "WorkDuration";


    public CheckInJson(String jsonStr){

        try{
            // Parse Json String
            JSONObject resultJson = new JSONObject(jsonStr);

            // Parse Objects
            status = resultJson.getString(OCI_STATUS);
            idTask = resultJson.getString(OCI_ID_TASK);
            taskDescription = resultJson.getString(OCI_TASK_DESC);
            address = resultJson.getString(OCI_ADDRESS);
            distance = resultJson.getString(OCI_DISTANCE);
            heading = resultJson.getString(OCI_HEADING);
            workduration = resultJson.getString(OCI_WORK_DURATION);

        }
        catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }
}
