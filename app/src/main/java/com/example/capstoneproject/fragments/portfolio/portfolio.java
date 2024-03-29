package com.example.capstoneproject.fragments.portfolio;

import static android.icu.lang.UCharacter.toUpperCase;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.capstoneproject.R;
import com.example.capstoneproject.fragments.chartsgraphs.pieChartOnChartValueSelectedListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import okhttp3.Headers;


public class portfolio extends Fragment {


    //Graphs - Ariq
    TextView chartTitleTextView2;
    LineChart mChart;
    ArrayList<Entry> x;
    ArrayList<String> y;
    // - end

    //used for the popup menu
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private EditText popup_stockname, popup_stockamount;
    Button popup_savebutton, popup_cancelbutton;
    Vector<Integer> stockamounts = new Vector<Integer>();
    Vector<String> stocknames = new Vector<String>();
    TextView testbalancesee,balanceee;
    databaseforportfoliograph portfolioDB;
    myportfoliodatabase myDB;
    databaseforsecondchartportfolio mysecondDB;
    databaseforbalance balanceDB;
    databasefortotalvalue valueDB;
    ArrayList<String> book_id, book_title, book_author, book_pages;
    FloatingActionButton gotofragment2; //possibly going to be useless
    databaseforachievements myAchievementDB;
    private Activity activity;
    //for alarm
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    Button testbutton;
    Button testbutton2;
    //for recyclerview
    private ArrayList<portfoliostock> stocksnames;
    portfoliostockrecycleradapter portfoliostockadapter;
    private RecyclerView recyclerview;



    public portfolio() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {

        super.onResume();
        balanceee.setText(String.valueOf(returnbalance()));
        //tryredraw();
        updatestock();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        gotofragment2 = view.findViewById(R.id.addstockcryptobutton);
        testbalancesee = view.findViewById(R.id.testbalanceview);
        myDB = new myportfoliodatabase(getActivity());
        portfolioDB = new databaseforportfoliograph(getActivity());
        balanceDB = new databaseforbalance(getActivity());
        valueDB = new databasefortotalvalue(getActivity());
        myAchievementDB = new databaseforachievements(getActivity());

       // uncomment for actual release of the app ig
        //updatestock();
        mysecondDB = new databaseforsecondchartportfolio(getActivity());
        checkifaddtobalancedb();
        book_id = new ArrayList<>();
        book_author = new ArrayList<>();
        book_title = new ArrayList<>();
        book_pages = new ArrayList<>();
        testbutton = view.findViewById(R.id.refreshbutton);
        testbutton2 = view.findViewById(R.id.teestbutton);
        storeDatainArrays();
        recyclerview = view.findViewById(R.id.recycleviewstocks);
        portfoliostockadapter = new portfoliostockrecycleradapter(getActivity(),getActivity(), book_id, book_title, book_author, book_pages);
        recyclerview.setAdapter(portfoliostockadapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        activity = getActivity();
        balanceee = view.findViewById(R.id.balanceview); //for viewing balance
        balanceee.setText(String.valueOf(returnbalance()));

        gotofragment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewDialog();

            }
        });
        testbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //updatestock();
                Intent intent = new Intent(getActivity(), achievementactivity.class);
                activity.startActivityForResult(intent, 1);
        }
        });

        testbutton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
 /*
                mysecondDB.addentry("12133","10/23/2021");
                mysecondDB.addentry("12133","10/24/2021");
                mysecondDB.addentry("14213","10/25/2021");
                mysecondDB.addentry("11231","10/26/2021");
                mysecondDB.addentry("1713","10/27/2021");
                mysecondDB.addentry("11425","10/28/2021");
                mysecondDB.addentry("7130","10/29/2021");
                mysecondDB.addentry("7130","10/30/2021");
                mysecondDB.addentry("7130","10/30/2021");
                mysecondDB.addentry("0","11/01/2021");
*/
    startsetbalnce();
    }
        });

        return view;
    }


    // Graph code - Ariq
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //line chart - start

        chartTitleTextView2 = view.findViewById(R.id.chartTitleTextView2);
        mChart = (LineChart)view.findViewById(R.id.lineChart);

        mChart.setDrawGridBackground(false);
        mChart.setDescription("");
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        mChart.getXAxis().setTextSize(15f);
        mChart.getAxisLeft().setTextSize(15f);

        XAxis xl = mChart.getXAxis();
        xl.setAvoidFirstLastClipping(true);


        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);


        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> balance = new ArrayList<>();
        ArrayList<String> date_entry = new ArrayList<>();

        List<Integer> colors = new ArrayList<>();
        int greeen  = Color.rgb(110,190,102);
        int reed = Color.rgb(211,87,44);

        //chartTitleTextView2.setText();
        storeDataInArraysPortfolio(id, balance, date_entry);


        x = new ArrayList<Entry>();
        y = new ArrayList<String>();

        for(int i = 0; i < balance.size(); i++) {
            float xVal = Float.valueOf(balance.get(i));
            x.add(new Entry(xVal, i));
            y.add(date_entry.get(i));
        }




        LineDataSet balanceset = new LineDataSet(x, "Portfolio Balance Graph");

        balanceset.setLineWidth(1.5f);
        balanceset.setCircleRadius(4f);
        LineData data = new LineData(y, balanceset);
        mChart.setData(data);
        mChart.invalidate();






        /*
        portfolioChart.animateY(0);
        LineData data = new LineData(portfolioDateTimes, portfolioSet);
        //portfolioSet .setColors(colors);
        Legend legend = portfolioChart.getLegend();
        legend.setEnabled(false); // hide legend

        portfolioChart.setDescription("Portfolio Balance Chart");
        portfolioChart.setData(data);

        */

        // line chart - end


        // piechart - start

        PieChart pieChart = view.findViewById(R.id.balancePiechart);
        ArrayList sectorPercent = new ArrayList();
        ArrayList fullPrice = new ArrayList();
        ArrayList sector = new ArrayList();
        ArrayList<String> stockId, name, price, quantity,  sectorName;
        stockId = new ArrayList<>();
        name = new ArrayList<>();
        price = new ArrayList<>();
        quantity = new ArrayList<>();
        sectorName = new ArrayList<>();


        storeSectorsDataInArrays(stockId, name, price, quantity, sectorName);

        float allStockValue = 0.0F;
        for (int i = 0; i < quantity.size(); i++) {
            allStockValue += Double.parseDouble(price.get(i))*Double.parseDouble(quantity.get(i));
            fullPrice.add(Double.parseDouble(price.get(i))*Double.parseDouble(quantity.get(i)));

        }

        ArrayList percentageTotal = new ArrayList();
        ArrayList<String> sectorTotal = new ArrayList<>();

        for (int i = 0; i < fullPrice.size(); i++) {
            Double dFull = (Double) fullPrice.get(i);
            float full = dFull.floatValue();


            if(sectorTotal.contains(sectorName.get(i))) {

                for(int j = 0; j < sectorTotal.size(); j++) {
                    if(sectorTotal.get(j).equals(sectorName.get(i)) ) {
                        float full2 = full*1/allStockValue*100;
                        float full3 = (float) percentageTotal.get(j);


                        percentageTotal.set(j, full3 +full2);
                    }
                }
            }else {
                percentageTotal.add(full*1/allStockValue*100);
                sectorTotal.add(sectorName.get(i));
            }




        }
        for (int i = 0; i < sectorTotal.size(); i++) {
            float full = (float) percentageTotal.get(i);
            sectorPercent.add(new Entry( full*1, i));
            sector.add(sectorTotal.get(i));


        }

        //sectorPercent.add(new Entry(full*1/allStockValue*100 , i));
        // sector.add(sectorTotal);





        //sectorPercent.add(new Entry(.5f*10, 0));
        //sectorPercent.add(new Entry(50f, 1));

        PieDataSet dataSet = new PieDataSet(sectorPercent, "Your Sector Allocation");

        dataSet.setValueTextSize(10f);




        //sector.add("Consumer Commerce");

        PieData sectorData = new PieData(sector, dataSet);
        pieChart.setData(sectorData);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(100, 100);

        pieChart.setRotationEnabled(true);
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        pieChart.setTouchEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setDescription("Sectors Percentage");
        pieChart.setOnChartValueSelectedListener(new pieChartOnChartValueSelectedListener());


        // pie chart - end

    }
    void storeSectorsDataInArrays( ArrayList<String> stockId, ArrayList<String> stockName ,ArrayList<String> price, ArrayList<String> quantity, ArrayList<String>  sector) {
        Cursor cursor = myDB.readAllData();

        if (cursor.getCount() == 0) {
            //Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                stockId.add(cursor.getString(0));
                stockName.add(cursor.getString(1));
                price.add(cursor.getString(2));
                quantity.add(cursor.getString(3));
                sector.add(cursor.getString(4));

            }

        }
    }

    private ArrayList<Entry> dataValues1() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(0, 13333));
        dataVals.add(new Entry(1, 14333));
        dataVals.add(new Entry(2, 15333));
        dataVals.add(new Entry(3, 11333));
        dataVals.add(new Entry(4, 7333));
        dataVals.add(new Entry(5, 0));

        return dataVals;
    }

    // From Jimmy's code
    void storeDataInArraysPortfolio(ArrayList<String> id, ArrayList<String> balance, ArrayList<String> date_entry) {
        Cursor cursor = mysecondDB.readAllData();
        if(cursor.getCount() == 0) {
            //Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
        }else {
            while(cursor.moveToNext()) {
                id.add(cursor.getString(0));
                balance.add(cursor.getString(1));
                date_entry.add(cursor.getString(2));


            }
        }
    }



    public void startsetbalnce(){
        dialogbuilder = new AlertDialog.Builder(getActivity()); //the video used this might be an issue
        final View popupview = getLayoutInflater().inflate(R.layout.popupsetinitbalance, null);
        popup_stockname = (EditText) popupview.findViewById(R.id.popupsetinitbalance_text);
        popup_savebutton = (Button) popupview.findViewById(R.id.popupaddsetinitbalance_savebutton);
        popup_cancelbutton = (Button) popupview.findViewById(R.id.popupsetinitbalance_cancelbutton);
        dialogbuilder.setView(popupview);
        dialog = dialogbuilder.create();
        dialog.show();


        popup_cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        popup_savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    int x = Integer.parseInt(String.valueOf(popup_stockname.getText()));
                    balanceDB.deleteallbalance();
                    myAchievementDB.deleteallachievements();
                    //Integer.parseInt(popup_stockamount.getText().toString())
                    balanceDB.addinitial(String.valueOf(popup_stockname.getText())); //actual balance for use on portfolio
                    //balanceDB.addinitial(String.valueOf(popup_stockname.getText()));
                    myAchievementDB.addachievement("set a balance","0", "true");
                    myAchievementDB.addachievement("make double your money",String.valueOf(Integer.parseInt(popup_stockname.getText().toString())*2), "false");
                    myAchievementDB.addachievement("make 5 times your money",String.valueOf(Integer.parseInt(popup_stockname.getText().toString())*5), "false");
                    if(Integer.parseInt(popup_stockname.getText().toString()) > 1000000){
                        myAchievementDB.addachievement("have a million dollars in balance","1000000", "true");
                    }
                    else{
                        myAchievementDB.addachievement("have a million dollars in balance","1000000", "false");
                    }
                    if(Integer.parseInt(popup_stockname.getText().toString()) > 2000000){
                        myAchievementDB.addachievement("have 2 million dollars in balance","2000000", "true");
                    }
                    else{
                        myAchievementDB.addachievement("have 2 million dollars in balance","2000000", "false");
                    }
                    if(Integer.parseInt(popup_stockname.getText().toString()) > 10000000){
                        myAchievementDB.addachievement("have 10 million dollars in balance","10000000", "true");
                    }
                    else{
                        myAchievementDB.addachievement("have 10 million dollars in balance","10000000", "false");
                    }
                    double test = Integer.parseInt(popup_stockname.getText().toString())*.75;

                    myAchievementDB.addachievement("have 75% of your original balance",String.valueOf((int)(test)), "false");
                    test = Integer.parseInt(popup_stockname.getText().toString())*.5;
                    myAchievementDB.addachievement("have half of your original balance",String.valueOf((int)(test)), "false");
                    test = Integer.parseInt(popup_stockname.getText().toString())*.01;
                    myAchievementDB.addachievement("have 1% of your original balance",String.valueOf((int)(test)), "false");


                    balanceee.setText(String.valueOf(returnbalance()));
                    dialog.dismiss();
                }
                catch(NumberFormatException e){
                    Toast.makeText(getActivity(), "Error, not an integer!", Toast.LENGTH_SHORT).show();
                }

                }


        });
    }
    //this is for the add stock popup
    public void createNewDialog() {
        dialogbuilder = new AlertDialog.Builder(getActivity()); //the video used this might be an issue
        final View popupview = getLayoutInflater().inflate(R.layout.popupaddstockcrypto, null);
        popup_stockname = (EditText) popupview.findViewById(R.id.popupaddstockcrypto_stock);
        popup_stockamount = (EditText) popupview.findViewById(R.id.popupaddstockcrypto_amount);
        popup_cancelbutton = (Button) popupview.findViewById(R.id.popupaddstockcrypto_cancelbutton);
        popup_savebutton = (Button) popupview.findViewById(R.id.popupaddstockcrypto_savebutton);
        dialogbuilder.setView(popupview);
        dialog = dialogbuilder.create();
        dialog.show();


        popup_cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        popup_savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkifexists = popup_stockname.getText().toString().trim().toLowerCase();
                Cursor cursor9 = myDB.readAllData();
                int checkagain = 0;
                if (cursor9.getCount() == 0) {

                } else {
                    while (cursor9.moveToNext()) {
                        if(checkifexists.equals(cursor9.getString(1).toLowerCase())){
                            checkagain = 1;
                            System.out.println("p");
                        }
                    }
                }
                if(checkagain == 1) {
                    Toast.makeText(getActivity(), "Error!, Stock already exists", Toast.LENGTH_SHORT).show();

                } else {

                    AsyncHttpClient client = new AsyncHttpClient();

                    /*String testapi = "https://cloud.iexapis.com/stable/stock/market/batch?symbols=" + popup_stockname.getText().toString().trim() +"&types=quote&range=1m&last=5&token=sk_312389e990ff49af9d13a20cc770ec95";
                     */
                    String testapi = "https://financialmodelingprep.com/api/v3/profile/" + popup_stockname.getText().toString().trim() + "?apikey=d610507a84e6b54992411a018867a0b7";
                    client.get(testapi, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            JSONArray jsonObject = json.jsonArray;
                            try {
                                JSONObject p = jsonObject.getJSONObject(0);
                            /*
                            JSONObject p = results.getJSONObject(toUpperCase(popup_stockname.getText().toString().trim()));

                            p = p.getJSONObject("quote");
                            */
                                try {
                                    int x = Integer.parseInt(popup_stockamount.getText().toString());
                                    if (x < 0) {
                                        Toast.makeText(getActivity(), "Error!, amount less than 0", Toast.LENGTH_SHORT).show();

                                    } else {

                                    System.out.println((int) Double.parseDouble(p.getString("price")));
                                    x = x * (int) Double.parseDouble(p.getString("price"));

                                    x = returnbalance() - x;
                                    if (x < 0) {
                                        Toast.makeText(getActivity(), "Error!, not enough money", Toast.LENGTH_SHORT).show();
                                    } else {
                                        myDB.addstock(popup_stockname.getText().toString().trim(), p.getString("price"), Integer.parseInt(popup_stockamount.getText().toString()), p.getString("sector"));
                                        balanceDB.updateData(String.valueOf(returnbalanceid()), String.valueOf(x));
                                        tryredraw();
                                        checkachievements(returnbalance());
                                        balanceee.setText(String.valueOf(returnbalance()));
                                        dialog.dismiss();
                                    }
                                }
                                } catch (NumberFormatException e) {
                                    Toast.makeText(getActivity(), "Error!, not an integer on stock amount", Toast.LENGTH_SHORT).show();

                                }


                            } catch (JSONException e) {

                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Error!, please check stock name", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            System.out.println("Failed");
                            Toast.makeText(getActivity(), "Error!, please check stock name", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                }

        });
    }
/*
    void fixsector(String sector){
        AsyncHttpClient client = new AsyncHttpClient();

        String testapi = "https://financialmodelingprep.com/api/v3/profile/" + sector + "?apikey=d610507a84e6b54992411a018867a0b7";
        System.out.println(testapi);
        client.get(testapi, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonObject = json.jsonArray;
                try {
                JSONObject p = jsonObject.getJSONObject(0);
                String pa = p.getString("sector");
                System.out.println(pa);
                pa = p.getString("price");
                System.out.println(pa);

                } catch (JSONException e) {
                    System.out.println("JSONEXCEPTION1");
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                System.out.println("JSONEXCEPTION2");
            }
        });



    }

*/



    void storeDatainArrays() {

        Cursor cursor = myDB.readAllData();

        if (cursor.getCount() == 0) {
            //Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));

            }

        }

    }

    void returnstock(String stocknames,Vector<String> stockss) {
        AsyncHttpClient client = new AsyncHttpClient();
        String testapi = "https://api.twelvedata.com/price?symbol=" + stocknames + "&apikey=f0b21df90101477184b43faf1d393bc9";
        System.out.println(testapi);
        ArrayList<String> testing = new ArrayList<>();
        client.get(testapi, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject results = jsonObject;
                    for(int i=0;i<stockss.size();i++){
                        JSONObject p = results.getJSONObject(stockss.get(i));
                        String symbol = p.getString("price");
                        testing.add(p.getString("price"));
                        System.out.println(testing.size());
                        System.out.println(symbol);
                    }

                tryredraw();
                } catch (JSONException e) {

                    e.printStackTrace();


                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });


    }

    void tryredraw(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerview.setAdapter(null);
                recyclerview.setLayoutManager(null);
                book_id = new ArrayList<>();
                book_author = new ArrayList<>();
                book_title = new ArrayList<>();
                book_pages = new ArrayList<>();
                storeDatainArrays();
                portfoliostockadapter = new portfoliostockrecycleradapter(getActivity(),getActivity(), book_id, book_title, book_author, book_pages);
                recyclerview.setAdapter(portfoliostockadapter);
                recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });

    }

    void updatestock(){
        Cursor cursor = myDB.readAllData();
        String stocknamesb = "";
        ArrayList<String> stonks = new ArrayList<>();
        ArrayList<String> testvector = new ArrayList();
        if (cursor.getCount() == 0) {
            //Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {

                stocknamesb = stocknamesb + cursor.getString(1).toString() + ",";
                stonks.add(cursor.getString(1));
            }
        }
        AsyncHttpClient client = new AsyncHttpClient();
        String testapi = "https://api.twelvedata.com/price?symbol=" + stocknamesb + "&apikey=f0b21df90101477184b43faf1d393bc9";
        System.out.println(testapi);
        client.get(testapi, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;

                try {
                    JSONObject results = jsonObject;
                    for(int i=0;i<stonks.size();i++)
                    {

                        JSONObject p = results.getJSONObject(stonks.get(i));

                       testvector.add(p.getString("price"));


                    }
                    System.out.println(testvector.size());
                    myportfoliodatabase testdbthing = new myportfoliodatabase(getActivity());
                     Cursor cursor3 = myDB.readAllData();
                    int testnum = 0;
                    if (cursor3.getCount() == 0) {
                        //Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
                    } else {
                        while (cursor3.moveToNext()) {
                            testdbthing.updateData(cursor3.getString(0),
                                    cursor3.getString(1),
                                    testvector.get(testnum),
                                    cursor3.getString(3));
                            System.out.println(testvector.get(testnum));
                            testnum = testnum + 1;
                        }
                        tryredraw();
                    }
                } catch (JSONException e) {
                    System.out.println("JSONEXCEPTION1");
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                System.out.println("JSONEXCEPTION2");
            }
        });




    }
    void calcbal(TextView a){
        Cursor cursor = myDB.readAllData();
        Double balance = Double.parseDouble("0");
        if (cursor.getCount() == 0) {
            //Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                balance = balance + Double.parseDouble(cursor.getString(2)) * Double.parseDouble(cursor.getString(3));
            }

        }
        a.setText(String.valueOf(balance));
        System.out.println(balance);
    }
    void firsttimesetupever(){
        valueDB.addinitial("0");
        balanceDB.addinitial("0");

    }


    void checkifaddtobalancedb(){
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        currentDateTimeString = currentDateTimeString.substring(0,currentDateTimeString.length()-10);

        Cursor cursor = mysecondDB.readAllData();
        int checkfordate = 0;
        if (cursor.getCount() == 0) {
            System.out.println("nothing in second database");
        } else {
            while (cursor.moveToNext()) {
                if(currentDateTimeString.equals(cursor.getString(2))){
                    checkfordate = 1;
                };

            }

        }

        if(checkfordate == 0){
            Cursor cursor2 = balanceDB.readAllData();
            if(cursor2.getCount()==0){
                Toast.makeText(getActivity(), "No balance", Toast.LENGTH_SHORT).show();
            }
            else {
                while (cursor2.moveToNext()) {

                    System.out.println(cursor2.getString(1));
                    mysecondDB.addentry(cursor2.getString(1), currentDateTimeString);
                    Toast.makeText(getActivity(), "New entry made in the daily graph", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            //Toast.makeText(getActivity(), "Entry for balance was already made today", Toast.LENGTH_SHORT).show();
        }
    }
    int returnbalance(){
        Cursor cursor2 = balanceDB.readAllData();
        int bal = 0;
        if(cursor2.getCount()==0){
            return 0;
        }
        else {
            while (cursor2.moveToNext()) {
                bal = Integer.parseInt(cursor2.getString(1));
            }
        }
        return bal;
    }
    int returnbalanceid(){
        Cursor cursor2 = balanceDB.readAllData();
        int bal = 0;
        if(cursor2.getCount()==0){
            return 0;
        }
        else {
            while (cursor2.moveToNext()) {
                bal = Integer.parseInt(cursor2.getString(0));
            }
        }
        return bal;
    }
    void checkachievements(int value){

        Cursor cursor = myAchievementDB.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No achievements, please set balance", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                System.out.println(Integer.valueOf(cursor.getString(2)));
                if(value >= Integer.valueOf(cursor.getString(2))){
                    if(Integer.valueOf(cursor.getString(0))>5){
                        if(value <= Integer.valueOf(cursor.getString(2))){
                            myAchievementDB.updateData2(cursor.getString(0),cursor.getString(1),cursor.getString(2),"true");

                        }
                    }
                    else{
                        myAchievementDB.updateData2(cursor.getString(0),cursor.getString(1),cursor.getString(2),"true");
                    }

                }
            }
        }
    }
}

/*
Handler handler = new Handler();
Runnable runnable;
int delay = 15*1000; //Delay for 15 seconds.  One second = 1000 milliseconds.

*/

/*
// If onPause() is not included the threads will double up when you
// reload the activity

@Override
protected void onPause() {
    handler.removeCallbacks(runnable); //stop handler when activity not visible
    super.onPause();
}
*/