package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SymptomResult extends AppCompatActivity {

    //temporary
    private TextView ageQR;
    private TextView symptomQR;
    private TextView diseaseQR;
    private TextView travelCQR;
    private TextView qurantineCQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_result);

        //temporary
        ageQR = findViewById(R.id.ageresults);
        symptomQR = findViewById(R.id.symptomresults);
        diseaseQR = findViewById(R.id.diseaseresults);
        travelCQR = findViewById(R.id.travelCesults);
        qurantineCQR = findViewById(R.id.quarantineCresults);

        Intent mIntent = getIntent();
        int[] finalScores = mIntent.getIntArrayExtra("result");

        //checking if the data was received properly from SymptomTest activity
        System.out.println("HERE result : ");
        for(int i : finalScores){
            System.out.println(i);
        }

        //temporary
        ageQR.setText("Age factor : " + finalScores[0] + "/1");
        symptomQR.setText("Symptoms satisfying criteria : " + finalScores[1] + "/10");
        diseaseQR.setText("Pre existing diseases : " + finalScores[2] + "/6");
        travelCQR.setText("Travel criteria satisfied : " + finalScores[3] + "/7");
        qurantineCQR.setText("Quarantine criteria satisfied : " + finalScores[4] + "/9");
    }
}
