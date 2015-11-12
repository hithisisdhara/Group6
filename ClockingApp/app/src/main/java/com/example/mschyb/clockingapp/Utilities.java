package com.example.mschyb.clockingapp;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class Utilities
{
    public boolean checkCredentials(String username, String password)
    {

        String params = "username=" + username + "&password=" + password;
        Transporter transporter = new Transporter();
        transporter.execute(Config.CHECK_CREDENTIALS_ENDPOINT, "POST", params);

        JsonObject jsonObject = convertStringToJson(readTransporter(transporter));

        if(jsonObject == null)
        {
            Log.e(Config.TAG, "Error when trying to convert string to JSON object");
        }

        return jsonObject.get("authenticated").toString().equals("true");
    }
    public String[] getSchedule(String userID, String sDate, String eDate) {
        String[] arr = new String[2];
        String params = "userId=" + userID + "&startDate=" + sDate + "&endDate=" + eDate;
        Transporter transporter = new Transporter();
        transporter.execute(Config.GET_SCHEDULE_ENDPOINT, "GET", params);

        JsonObject jsonResult = convertStringToJson(readTransporter(transporter));

        if(jsonResult == null)
        {
            Log.e(Config.TAG, "Result check is null");
            arr = null;

        } else {

            //need to check for if there are no query results and return a null array
            //arr = null;
            JsonArray data = jsonResult.getAsJsonArray("schedule");
            for (JsonElement el:data)
            {
                JsonObject obj=(JsonObject)el;
                arr[0]=obj.get("startTime").toString();
                arr[1]=obj.get("endTime").toString();
            }

        }
       // arr[0] = "2015-06-12 08:00:00";
        //arr[1] = "2015-06-12 08:00:00";
        return arr;

    }
    public String[] getHoursWorked(String userID, String sDate, String eDate) {
        String[] arr = new String[2];
        String params = "userId=" + userID + "&startDate=" + sDate + "&endDate=" + eDate;
        Transporter transporter = new Transporter();
        transporter.execute(Config.GET_SCHEDULE_ENDPOINT, "GET", params);

        JsonObject jsonResult = convertStringToJson(readTransporter(transporter));

        if(jsonResult == null)
        {
            Log.e(Config.TAG, "Result check is null");

        } else {

            //need to check for if there are no query results and return a null array
            //arr = null;
            JsonArray data = jsonResult.getAsJsonArray("schedule");
            for (JsonElement el:data)
            {
                JsonObject obj=(JsonObject)el;
                arr[0]=obj.get("startTime").toString();
                arr[1]=obj.get("endTime").toString();
            }

        }
        // arr[0] = "2015-06-12 08:00:00";
        //arr[1] = "2015-06-12 08:00:00";
        return arr;

    }

    public static void clockUser(int isClockingIn)
    {
        String params = "userId=" + Config.getUserId() + "&isClockingIn=" + isClockingIn;
        Transporter transporter = new Transporter();
        transporter.execute(Config.LOG_TIME_ENDPOINT, "POST", params);
    }


    private String readTransporter(Transporter transporter)
    {
        String result = null;
        try
        {
            result = transporter.get();
        }
        catch(InterruptedException | ExecutionException e)
        {
            Log.e(Config.TAG, "Exception when trying to get result from transporter class");
        }
        return result;
    }

    private JsonObject convertStringToJson(String stringToConvert)
    {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonResult = (JsonObject)jsonParser.parse(stringToConvert);

        if(jsonResult == null)
        {
            Log.e(Config.TAG, "Unable to convert string to JsonObject");
            return null;
        }
        return  jsonResult;
    }

}
