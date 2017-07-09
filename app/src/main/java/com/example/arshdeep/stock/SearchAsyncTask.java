package com.example.arshdeep.stock;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Arshdeep on 7/9/2017.
 */

public class SearchAsyncTask extends AsyncTask<String , Void , ArrayList<String>>{
    onSearchDownloadCompleteListener mListener;

    void setOnSearchDownloadCompleteListener(onSearchDownloadCompleteListener listener){
        mListener = listener;
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        String urlString = strings[0];

        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();

            Scanner s = new Scanner(inputStream);
            String str = "";
            while(s.hasNext()){
                str += s.nextLine();
            }
            return parseStocks(str);
        }catch (MalformedURLException e){

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> parseStocks(String str) {
        try {
            JSONArray searchJsonArray = new JSONArray(str);
            ArrayList<String> searchList = new ArrayList<>();

            for(int i=0;i<searchJsonArray.length();i++){
                JSONObject searchObject = (JSONObject) searchJsonArray.getJSONObject(i);
                String tag = searchObject.getString("company_symbol");
                searchList.add(tag);
            }

            return searchList;
        }catch (JSONException e){

        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<String> stocks) {
        super.onPostExecute(stocks);
        if(mListener != null){
            mListener.onSearchDownloadComplete(stocks);
        }

    }
}

interface onSearchDownloadCompleteListener{
    void onSearchDownloadComplete(ArrayList <String> stocks);
}
