package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddTimeControl extends AppCompatActivity
{
    int hoursP1;
    int hoursP2;
    int minutesP1;
    int minutesP2;
    int secondsP1;
    int secondsP2;
    int incrementP1;
    int incrementP2;
    String tcName;
    enum validationCode {valid, numTooHigh, numNegative, allNumsZero, emptyFields}
    validationCode v;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time_control);
    }

    public void addTC(View view)
    {
        v = validationCode.valid;
        EditText temp;
        temp = findViewById(R.id.numPicker_hours);
        if(!temp.getText().toString().isEmpty()){
            hoursP1 = Integer.parseInt(temp.getText().toString());
        }
        else{
            v = validationCode.emptyFields;
        }
        temp = findViewById(R.id.numPicker_hours2);
        if(!temp.getText().toString().isEmpty()){
            hoursP2 = Integer.parseInt(temp.getText().toString());
        }
        else{
            v = validationCode.emptyFields;
        }
        temp = findViewById(R.id.numPicker_mins);
        if(!temp.getText().toString().isEmpty()){
            minutesP1 = Integer.parseInt(temp.getText().toString());
        }
        else{
            v = validationCode.emptyFields;
        }
        temp = findViewById(R.id.numPicker_mins2);
        if(!temp.getText().toString().isEmpty()){
            minutesP2 = Integer.parseInt(temp.getText().toString());
        }
        else{
            v = validationCode.emptyFields;
        }
        temp = findViewById(R.id.numPicker_secs);
        if(!temp.getText().toString().isEmpty()){
            secondsP1 = Integer.parseInt(temp.getText().toString());
        }
        else{
            v = validationCode.emptyFields;
        }
        temp = findViewById(R.id.numPicker_secs2);
        if(!temp.getText().toString().isEmpty()){
            secondsP2 = Integer.parseInt(temp.getText().toString());
        }
        else{
            v = validationCode.emptyFields;
        }
        temp = findViewById(R.id.name);
        tcName = temp.getText().toString();
        if(tcName.isEmpty())
        {
            v = validationCode.emptyFields;
        }
        temp = findViewById(R.id.incrementP1);
        if(!temp.getText().toString().isEmpty()){
            incrementP1 = (Integer.parseInt(temp.getText().toString()) * 1000);
        }
        else{
            v = validationCode.emptyFields;
        }
        temp = findViewById(R.id.incrementP2);
        if(!temp.getText().toString().isEmpty()){
            incrementP2 = (Integer.parseInt(temp.getText().toString())*1000);
        }
        else{
            v = validationCode.emptyFields;
        }

        if(hoursP1 > 23 || hoursP2 > 23 || minutesP1 > 59 || minutesP2 > 59 || secondsP1 > 59 || secondsP2 > 59 || incrementP1 > 59000 || incrementP2 > 59000)
        {
            v = validationCode.numTooHigh;
        }
        else if(hoursP1 < 0 || hoursP2 < 0 || minutesP1 < 0 || minutesP2 < 0 || secondsP1 < 0 || secondsP2 < 0 || incrementP1 < 0 || incrementP2 < 0){
            v = validationCode.numNegative;
        }
        else if(hoursP1 == 0 &&  minutesP1 == 0 && secondsP1 == 0){
            v = validationCode.allNumsZero;
        }
        else if(hoursP2 == 0 &&  minutesP2 == 0 && secondsP2 == 0){
            v = validationCode.allNumsZero;
        }

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        switch (v){
            case numNegative:
                b.setMessage("A number you entered is below the minimum. Please correct the number before saving.")
                        .setTitle("Number negative");
                break;

            case numTooHigh:
                b.setMessage("A number you entered is above the maximum. Please correct the number before saving.")
                        .setTitle("Number too high");
                break;

            case allNumsZero:
                b.setMessage("The time control for either player can not be 0. Please correct the time control before saving.")
                        .setTitle("Time Zero");
                break;

            case emptyFields:
                b.setMessage("One of the mandatory fields has been left empty. Please enter a valid value before saving.")
                        .setTitle("Mandatory field missing");
                break;
        }
        if(v != validationCode.valid)
        {
            AlertDialog d = b.create();
            d.show();
        }
        else{
            long timeP1 = (hoursP1*3600 + minutesP1*60 + secondsP1)*1000;
            long timeP2 = (hoursP2*3600 + minutesP2*60 + secondsP2)*1000;
            Intent intent = new Intent(this, Settings.class);
            Bundle bun = new Bundle();
            bun.putString("name", tcName);
            bun.putLong("p1", timeP1);
            bun.putLong("p2", timeP2);
            bun.putLong("incP1",incrementP1);
            bun.putLong("incP2", incrementP2);
            intent.putExtras(bun);
            startActivity(intent);
        }
    }

    public Context getActivity()
    {
        return this;
    }

    public void cancelTC(View view)
    {
        super.finish();
    }
}