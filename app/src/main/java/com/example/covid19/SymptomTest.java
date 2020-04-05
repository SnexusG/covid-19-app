package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SymptomTest extends AppCompatActivity {


    private TextView symptomsQ;

    int count = 0;

    private RadioGroup symptomsOptions;
    private RadioButton symptomsA;
    private View rb1,rb2;

    //to store the results of different categories
    int symptomsR = 0;
    int diseaseR = 0;
    int travelR = 0;
    int ageR = 0;
    int quarantineR = 0;

    final String[] Questions = {"Is your age greater than 40?", "Do you have fever?",
            "Do you have cough?", "Do you feel difficulty in breathing?", "Do you face difficulty in speaking?",
            "Do you experience constant or severe sizzure", "Do you have asthma?", "Do you have diabities?", "Do you have any heart related problems?",
            "Have you travelled out of India in the past few months?", "Were you in contact with someone who returned from a foreign trip recently?",
            "Did you follow the lockdown as soon as it was declared?", "Do you go outside a lot?", "Do you often visit places with high pedestrian traffic?",
            "Were you involved in a social gathering of any form in te past few weeks?", "Was anyone around you diagnosed with corona?"};


    //weightage to each question as per category
    final int[] symptomsW = {2,1,2,2,3};
    final int[] diseasesW = {2,1,3};
    final int[] travelCW = {4,3};
    final int[] quarantineCW = {-2, 1, 2, 3, 3};

    final int len = Questions.length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_test_advait);
        rb1 = findViewById(R.id.yesBtn);
        rb2 = findViewById(R.id.noBtn);
        symptomsQ = findViewById(R.id.symptomsQ);
        //symptomsOptions = findViewById(R.id.symptomsOptions);
    }

    public void onClickedRadio(View view){
        int idx = 0;
        if(view == rb1){
            //yes
            idx = 1;
        }
        if (count > 0) {
            if (count < 6) {
                //symptoms category
                symptomsR += symptomsW[count - 1] * (idx);
                System.out.println("HERE symptomsR " + symptomsR);
            } else if (count < 9) {
                //diseases category
                diseaseR += diseasesW[count - 6] * (idx);
                System.out.println("HERE diseaseR " + diseaseR);
            } else if (count < 11) {
                //travel category
                travelR += travelCW[count - 9] * (idx);
                System.out.println("HERE travelR " + travelR);
            }else if(count < 16){
                //quarantine category
                quarantineR += quarantineCW[count-11] * (idx);
                System.out.println("HERE quarantineR " + quarantineR);
            }
        }else{
            //age category
            ageR = idx;
        }
        count++;
        if(count == len){
            int[] finalScore = {ageR,symptomsR,diseaseR,travelR,quarantineR};
            System.out.println("HERE DONE");
            Intent intent = new Intent(SymptomTest.this, SymptomResult.class);
            intent.putExtra("result", finalScore);
            startActivity(intent);
            finish();
        }
        symptomsQ.setText(Questions[count]);
        //symptomsOptions.clearCheck();
    }

}
