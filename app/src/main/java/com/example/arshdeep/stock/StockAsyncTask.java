package com.example.arshdeep.stock;

import android.os.AsyncTask;
import android.text.LoginFilter;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.SortedMap;

/**
 * Created by Arshdeep on 7/9/2017.
 */

public class StockAsyncTask extends AsyncTask<String , Void , ArrayList<Stock>>{
    onDownloadCompleteListener mListener;

    void setOnDownloadCompleteListener(onDownloadCompleteListener listener){
        mListener = listener;
    }


    @Override
    protected ArrayList<Stock> doInBackground(String... strings) {
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

    private ArrayList<Stock> parseStocks(String str) {
        try {
            JSONObject stockStringJsonObject = new JSONObject(str);
            JSONObject stockQueryJsonObject = stockStringJsonObject.getJSONObject("query");
            JSONObject stockResultsJsonObject = stockQueryJsonObject.getJSONObject("results");
            JSONArray stockQuoteJsonArray = stockResultsJsonObject.getJSONArray("quote");
            ArrayList <Stock> stocks = new ArrayList<>();

            for(int i=0;i<stockQuoteJsonArray.length();i++){
                JSONObject stockJsonObject = (JSONObject) stockQuoteJsonArray.get(i);
                String Symbol = stockJsonObject.getString("Symbol");
                String Name = stockJsonObject.getString("Name");
                String Change = stockJsonObject.getString("Change");
                String ChangeinPercent = stockJsonObject.getString("ChangeinPercent");

                Stock stk = new Stock(Symbol,Name,Change,ChangeinPercent);
                stocks.add(stk);
            }
            return stocks;
        }catch (JSONException e){

        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Stock> stocks) {
        super.onPostExecute(stocks);
        if(mListener != null){
            mListener.onDownloadComplete(stocks);
        }
    }
}
interface onDownloadCompleteListener{
    void onDownloadComplete(ArrayList <Stock> stocks);
}

