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

public class SearchStockAsyncTask extends AsyncTask<String , Void , Stock> {
    onSearchStockDownloadCompleteListener msListener;

    void setSearchStockOnDownloadCompleteListener(onSearchStockDownloadCompleteListener listener){
        msListener = listener;
    }

    @Override
    protected Stock doInBackground(String... strings) {
        String urlString = strings[0];

        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
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

    private Stock parseStocks(String str) {
        try {
            JSONObject stockStringJsonObject = new JSONObject(str);
            JSONObject stockQueryJsonObject = stockStringJsonObject.getJSONObject("query");
            JSONObject stockResultsJsonObject = stockQueryJsonObject.getJSONObject("results");
            JSONObject stockQuoteJsonObjectFinal = stockResultsJsonObject.getJSONObject("quote");

            String Symbol = stockQuoteJsonObjectFinal.getString("Symbol");
            String Name = stockQuoteJsonObjectFinal.getString("Name");
            String Change = stockQuoteJsonObjectFinal.getString("Change");
            String ChangeinPercent = stockQuoteJsonObjectFinal.getString("ChangeinPercent");

            Stock stk = new Stock(Symbol,Name,Change,ChangeinPercent);
            return stk;
        }catch (JSONException e){

        }
        return null;
    }

    @Override
    protected void onPostExecute(Stock stocks) {
        super.onPostExecute(stocks);
        if(msListener != null){
            msListener.onSearchStockDownloadComplete(stocks);
        }
    }
}

interface onSearchStockDownloadCompleteListener{
    void onSearchStockDownloadComplete(Stock stocks);
}

