package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    ArrayList<TimeControlSettings> allTimeControls = new ArrayList<>();
    SharedPreferences s;
    SharedPreferences.Editor e;
    int numTimeControls;
    TableLayout tbL;
    TableLayout.LayoutParams tableRowParams;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        tbL = findViewById(R.id.tableLayout);
        tableRowParams=
                new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT);
        tableRowParams.setMargins(0,10,0,10);
        Bundle b = getIntent().getExtras();
        loadTimeControls();
        if(b != null)
        {
            String ntemp = b.getString("name");
            Long p1Temp = b.getLong("p1");
            Long p2Temp = b.getLong("p2");
            Long inc1Temp = b.getLong("incP1");
            Long inc2Temp = b.getLong("incP2");
            createTimeControl(ntemp,p1Temp,p2Temp, inc1Temp, inc2Temp);
        }
        loadTable();
    }

    public void add(View view)
    {
        Intent intent = new Intent(this, AddTimeControl.class);
        startActivity(intent);
    }

    public void loadTimeControls()
    {
      s = getActivity().getSharedPreferences("timeControlData", Context.MODE_PRIVATE);
      e = s.edit();
      numTimeControls = s.getInt("numTimeControls",0);
      for(int j = 0; j < numTimeControls; j++)
        {
            String nameTemp = s.getString("name" + j, "Error");
            long time1 = s.getLong("timeP1" + j,0);
            long time2 = s.getLong("timeP2"+ j,0);
            long inc1 = s.getLong("incP1"+ j,0);
            long inc2 = s.getLong("incP2"+ j,0);
            TimeControlSettings t = new TimeControlSettings(nameTemp, time1, time2, inc1, inc2);
            allTimeControls.add(t);
        }
    }

    public void createTimeControl(String n, long t1, long t2, long inc1, long inc2)
    {
        TimeControlSettings t = new TimeControlSettings(n, t1, t2, inc1, inc2);
        allTimeControls.add(t);
        e.putString("name" + numTimeControls, n);
        e.putLong("timeP1" + numTimeControls, t1);
        e.putLong("timeP2" + numTimeControls, t2);
        e.putLong("incP1" + numTimeControls, inc1);
        e.putLong("incP2" + numTimeControls, inc2);
        numTimeControls++;
        e.putInt("numTimeControls", numTimeControls);
        e.commit();
    }

    public void clearTimeControls(View view)
    {
        e.clear();
        e.apply();
        numTimeControls = 0;
        allTimeControls = new ArrayList<>();
        tbL.removeAllViews();
    }

    public void loadTable()
    {
        tbL.removeAllViews();
       for(TimeControlSettings s : allTimeControls)
       {
           TableRow r = new TableRow(this);
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
           {
               r.setOutlineAmbientShadowColor(getResources().getColor(R.color.black));
           }
           r.setBackgroundColor(getResources().getColor(R.color.gray));
           r.setLayoutParams(tableRowParams);
           r.setBackgroundResource(R.drawable.rounded);

           TextView t1 = new TextView(this);
           t1.setText(s.name);
           t1.setTextSize(40);
           t1.setTextColor(getResources().getColor(R.color.white));
           r.addView(t1);

           TextView t2 = new TextView(this);
           t2.setText(Long.toString(s.timeControlP1));
           t2.setTextSize(40);
           t2.setVisibility(View.INVISIBLE);
           r.addView(t2);

           TextView t3 = new TextView(this);
           t3.setText(Long.toString(s.timeControlP2));
           t3.setTextSize(40);
           t3.setVisibility(View.INVISIBLE);
           r.addView(t3);

           TextView t4 = new TextView(this);
           t4.setText(Long.toString(s.incrementP1));
           t4.setTextSize(40);
           t4.setVisibility(View.INVISIBLE);
           r.addView(t4);

           TextView t5 = new TextView(this);
           t5.setText(Long.toString(s.incrementP2));
           t5.setTextSize(40);
           t5.setVisibility(View.INVISIBLE);
           r.addView(t5);

            View.OnTouchListener t = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            view.getBackground().setColorFilter(Color.rgb(201,201,201), PorterDuff.Mode.SRC_ATOP);
                            view.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            break;
                        }
                    }
                    return false;
                }
            };
           View.OnClickListener l = this::select;
           r.setOnClickListener(l);
           r.setOnTouchListener(t);
           tbL.addView(r);
       }
    }

    public void select(View view)
    {
        //get Settings from TableRow
        TableRow t = (TableRow) view;
        TextView tvName = (TextView) t.getChildAt(0);
        String name = (String) tvName.getText();
        TextView tvP1 = (TextView) t.getChildAt(1);
        long p1 = Long.parseLong(tvP1.getText().toString());
        TextView tvP2 = (TextView) t.getChildAt(2);
        long p2 = Long.parseLong(tvP2.getText().toString());
        TextView incP1 = (TextView) t.getChildAt(3);
        long inc1 = Long.parseLong(incP1.getText().toString());
        TextView incP2 = (TextView) t.getChildAt(4);
        long inc2 = Long.parseLong(incP2.getText().toString());

        //pass Settings to MainActivity and call MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        Bundle b = new Bundle();
        b.putString("name", name);
        b.putLong("p1", p1);
        b.putLong("p2", p2);
        b.putLong("incP1", inc1);
        b.putLong("incP2", inc2);
        intent.putExtras(b);
        startActivity(intent);
    }

    public Context getActivity()
    {
        return this;
    }
}