package com.example.mini2;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class ApiRequest {
    public interface ApiCallback {
        void onSuccess(JSONObject response);
        void onError(VolleyError error);
    }

    public static void makeApiCall(Context context, String inputText, final ApiCallback callback) {
        String API_URL = "https://api.oneai.com/api/v0/pipeline";
        String API_KEY = "2b41c4bc-40bb-412b-a9b2-f0fbf0064ec2";
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("input", inputText);
            jsonBody.put("input_type", "article");
            jsonBody.put("output_type", "json");
            jsonBody.put("multilingual", new JSONObject().put("enabled", true));

            JSONArray stepsArray = new JSONArray();
            JSONObject stepObject = new JSONObject();
            stepObject.put("skill", "summarize");
            stepsArray.put(stepObject);

            jsonBody.put("steps", stepsArray);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API_URL, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            callback.onSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            callback.onError(error);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("api-key", API_KEY);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}