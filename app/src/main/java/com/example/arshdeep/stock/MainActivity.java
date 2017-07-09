package com.example.arshdeep.stock;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements onDownloadCompleteListener, onSearchDownloadCompleteListener, onSearchStockDownloadCompleteListener {
    private List< Stock > stocks;
    private RecyclerView recyclerView;
    private StockAdapter stockAdapter;
    MaterialSearchView searchView;
    List <String> stockTagFound;
    List <Stock> stockFound;
    StockAdapter foundAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        searchView = (MaterialSearchView) findViewById(R.id.searchView);

        stockTagFound = new ArrayList<>();
        stockFound = new ArrayList<>();
        stocks = new ArrayList<>();
        prepareStockData();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        stockAdapter = new StockAdapter(stocks);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(stockAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext() , recyclerView , new MainActivity.ClickListener(){
            @Override
            public void onClick(View view, int position) {
                //Stock stock = stocks.get(position);
                //Toast.makeText(MainActivity.this, stock.Name + " is selected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLongClick(View view, int position) {}
        }));

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                //default
                recyclerView.setAdapter(stockAdapter);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){
                    stockTagFound.clear();
                    stockFound.clear();
                    foundAdapter = new StockAdapter(stockFound);
                    searchStock(newText);
                    Log.i("stockFound Size " , " " + stockFound.size());
                    recyclerView.setAdapter(foundAdapter);
//                    for(Stock s : stocks){
//                        if(s.Name.contains(newText)){
//                            stockTagFound.add(s);
//                        }
//
                }else{
                    //default
                    recyclerView.setAdapter(stockAdapter);
                }
                return true;
            }
        });

    }

    //Search tag in stock api
    private void searchStock(String newText) {
        String urlSearchString = "https://stocksearchapi.com/api/?search_text="+ newText +"&api_key=" + Config.APIKEY;
        SearchAsyncTask searchAsyncTask = new SearchAsyncTask();
        searchAsyncTask.execute(urlSearchString);
        searchAsyncTask.setOnSearchDownloadCompleteListener(this);
    }

    //On complete Tag Search put tags in array
    public void onSearchDownloadComplete(ArrayList<String> searchList){
        stockTagFound.clear();
        if(searchList != null) {
            stockTagFound.addAll(searchList);
        }else{
            Toast.makeText(this, "Internet Issue", Toast.LENGTH_SHORT).show();
        }
        searchTagInQuery();
    }

    //search stock using tag/symbol
    private void searchTagInQuery() {
        for(String s : stockTagFound) {
            //Log.i("Tag" ,s);
            char c = '"';
            String urlSearchTagString = "https://query.yahooapis.com/v1/public/yql?q=select+%2A+from+yahoo.finance.quotes+where+symbol+in+%28" + c + s + c + "%29&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback";
            SearchStockAsyncTask searchStockAsyncTask = new SearchStockAsyncTask();
            searchStockAsyncTask.execute(urlSearchTagString);
            searchStockAsyncTask.setSearchStockOnDownloadCompleteListener(this);
        }
    }

    //On Complete Stock Search put Stocks in stockFound array
    public void onSearchStockDownloadComplete(Stock searchList){
        if(searchList != null) {
            stockFound.add(searchList);
        }else{
            Toast.makeText(this, "Internet Issue", Toast.LENGTH_SHORT).show();
        }
        foundAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem item = menu.findItem(R.id.actionSearch);
        searchView.setMenuItem(item);
        return true;
    }

    private void prepareStockData() {
        String urlString = "https://query.yahooapis.com/v1/public/yql?q=select+%2A+from+yahoo.finance.quotes+where+symbol+in+%28%22YHOO%22%2C%22AAPL%22%2C%22GOOG%22%2C%22MSFT%22%29&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback";
        StockAsyncTask stockAsynckTask = new StockAsyncTask();
        stockAsynckTask.execute(urlString);

        stockAsynckTask.setOnDownloadCompleteListener(this);
    }

    public void onDownloadComplete(ArrayList<Stock> stockList){
        stocks.clear();
        if(stockList != null){
            stocks.addAll(stockList);
        }else{
            Toast.makeText(this, "Internet Issue", Toast.LENGTH_SHORT).show();
        }
        stockAdapter.notifyDataSetChanged();
    }

    public interface ClickListener{
        void onClick(View view, int position);
        void onLongClick(View view , int position);
    }
}
