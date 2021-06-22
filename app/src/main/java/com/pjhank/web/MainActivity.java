package com.pjhank.web;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    //ArrayList<HashMap<String,String>> arrayList2 = new ArrayList<>();
    ArrayList<HashMap<String,Integer>> iArrayList = new ArrayList<>();
    private int mJsonHeight = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        catchData();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                catchData();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void catchData(){
        String catchData = "https://fhy.wra.gov.tw/WraApi/v1/Reservoir/RealTimeInfo";
        String catchData2 = "https://fhy.wra.gov.tw/WraApi/v1/Reservoir/Station?$select=StationName%2CStationNo";
        //https://fhy.wra.gov.tw/WraApi/v1/Reservoir/Station?$select=StationName

        new Thread(()->{

            try {
                URL url = new URL(catchData);
                URL url2 = new URL(catchData2);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
                InputStream is = connection.getInputStream();
                InputStream is2 = connection2.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                BufferedReader in2 = new BufferedReader(new InputStreamReader(is2));
                String line = in.readLine();
                String line2 = in2.readLine();
                StringBuffer json = new StringBuffer();
                StringBuffer json2 = new StringBuffer();
                arrayList.clear();

                while (line2 != null) {
                    json.append(line);
                    json2.append(line2);
                    line = in.readLine();
                    line2 = in2.readLine();
                }


                JSONArray jsonArray= new JSONArray(String.valueOf(json));
                JSONArray jsonArray2= new JSONArray(String.valueOf(json2));
                int i =0;
                //----------------------------------------------//


                for (int q =0;q<jsonArray2.length();q++){
                    JSONObject jsonObject = jsonArray.getJSONObject(q);
                    JSONObject jsonObject2 = jsonArray2.getJSONObject(q);
                    Integer iPercentageOfStorage = jsonObject.optInt("PercentageOfStorage");
                    String StationNo = jsonObject.getString("StationNo");
                    //String StationNo2 = jsonObject2.getString("StationNo");
                    String StationName = jsonObject2.getString("StationName");
                    String Time = jsonObject.optString("Time");
                    String WaterHeight= jsonObject.optString("WaterHeight");
                    String EffectiveStorage  = jsonObject.optString("EffectiveStorage");
                    String PercentageOfStorage = jsonObject.optString("PercentageOfStorage");


                    //System.out.println(iPercentageOfStorage);
                    HashMap<String,String> hashMap = new HashMap<>();
                    HashMap<String,Integer> ihashMap = new HashMap<>();
                    //hashMap.put("StationNo",StationNo);
                    //hashMap.put("StationName",StationName);

                    //hashMap2.put("StationName",StationName);

                    if(jsonObject.get("StationNo").equals(jsonObject2.get("StationNo"))){
                        hashMap.put("StationNo",StationNo);
                        hashMap.put("StationName",StationName);
                        hashMap.put("PercentageOfStorage",PercentageOfStorage);
                        hashMap.put("EffectiveStorage",EffectiveStorage);
                        hashMap.put("WaterHeight",WaterHeight);
                        hashMap.put("Time",Time);
                        ihashMap.put("PercentageOfStorage",iPercentageOfStorage);
                        //System.out.println(iPercentageOfStorage/10);
                        //arrayList2.remove(q);
                        //q--;
                        //Log.d("TAG2",""+arrayList2+" end");
                        //System.out.println(q);
                        //System.out.println(arrayList);
                        iArrayList.add(ihashMap);
                        //System.out.println(iArrayList.get(q).toString());

                    }else{
                        jsonArray2.remove(q);
                    }

                    arrayList.add(hashMap);

                    if(arrayList.get(q).isEmpty()){
                        arrayList.remove(q);
                        //System.out.println(arrayList2.size());
                        q--;
                    }

                }
                System.out.println(arrayList.size() + " "+iArrayList.size());
                //System.out.println(arrayList2);

                runOnUiThread(()->{

                    RecyclerView recyclerView;
                    MyAdapter myAdapter;
                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    myAdapter = new MyAdapter();
                    recyclerView.setAdapter(myAdapter);

                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvPos,tvType,tvPrice,tvCar,tvDateTime;
            WaveView1 mWaveView1;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvPos = itemView.findViewById(R.id.textView_pos);
                tvType = itemView.findViewById(R.id.textView_type);
                tvPrice = itemView.findViewById(R.id.textView_price);
                tvCar = itemView.findViewById(R.id.textView_car);
                tvDateTime = itemView.findViewById(R.id.textView_time);
                mWaveView1 = (WaveView1)itemView.findViewById(R.id.water_height);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item,parent, false);
            
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.mWaveView1.setValues(iArrayList.get(position).get("PercentageOfStorage"));
            holder.tvPos.setText(arrayList.get(position).get("StationName"));
            holder.tvType.setText("蓄水百分比："+arrayList.get(position).get("PercentageOfStorage"));
            holder.tvPrice.setText("有效蓄水量："+arrayList.get(position).get("EffectiveStorage")+"萬立方公尺");
            holder.tvCar.setText("水位高："+arrayList.get(position).get("WaterHeight")+"公尺");
            holder.tvDateTime.setText("更新時間："+arrayList.get(position).get("Time"));
            System.out.println(arrayList.get(position).get("StationName")+" "+position+" "+iArrayList.get(position).get("PercentageOfStorage")+" "+position);

        }
        @Override
        public int getItemCount() {
            return arrayList.size();
        }


    }




}

