package com.example.letsgogooglemap.Maps.library;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
public class JSONParser {
    /*
        01. Static Fields
        02. Constructor
        03. Making HTTP Requests
        04. Try Parse the String To a JSON Object
        05. Return JSON String
     */
    // 01. Static Fields
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // 02. Constructor
    public JSONParser(){

    }

    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params){
        // 03. Making HTTP Requests
        try{
            // Default HTTP CLient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }catch (ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        // buffered Reader
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line+"\n");
            }
            is.close();
            json = sb.toString();
            Log.e("JSON", json);
        }catch(Exception e){
            Log.e("Buffer Error", "Error Converting Result "+ e.toString());
        }

        // 04. Try Parse the String To a JSON Object
        try{
            jObj = new JSONObject(json);
        }catch(JSONException e){
            Log.e("JSON Parser", "Error Parsing Data "+e.toString());
        }
        // 05. Return JSON String
        return jObj;

    }
}
