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
    private TextView nextQ;

    int count = 0;

    private RadioGroup symptomsOptions;
    private RadioButton symptomsA;

    //to store the results of different categories
    int symptomsR = 0;
    int diseaseR = 0;
    int travelR = 0;
    int ageR = 0;
    int quarantineR = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_test);

        symptomsQ = findViewById(R.id.symptomsQ);
        nextQ = findViewById(R.id.next_question);
        symptomsOptions = findViewById(R.id.symptomsOptions);

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
        System.out.println("HERE questionaire length " + len);


        nextQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = symptomsOptions.getCheckedRadioButtonId();
                if(selectedId != -1) {  //an option is selected

                    //get the index of the selected option, 0 for YES and 1 for NO
                    symptomsA = findViewById(selectedId);
                    View radioButton = symptomsOptions.findViewById(selectedId);
                    int idx = symptomsOptions.indexOfChild(radioButton);


                    if (count > 0) {
                        if (count < 6) {
                            //symptoms category
                            symptomsR += symptomsW[count - 1] * (idx ^ 1);
                            System.out.println("HERE symptomsR " + symptomsR);
                        } else if (count < 9) {
                            //diseases category
                            diseaseR += diseasesW[count - 6] * (idx ^ 1);
                            System.out.println("HERE diseaseR " + diseaseR);
                        } else if (count < 11) {
                            //travel category
                            travelR += travelCW[count - 9] * (idx ^ 1);
                            System.out.println("HERE travelR " + travelR);
                        }else if(count < 16){
                            //quarantine category
                            quarantineR += quarantineCW[count-11] * (idx^1);
                            System.out.println("HERE quarantineR " + quarantineR);
                        }
                    }else{
                            //age category
                            ageR = idx^1;
                    }
                        count++;
                    if(count != len)
                        symptomsQ.setText(Questions[count]);

                }else{
                    Toast.makeText(SymptomTest.this, "You need to select one of the options to proceed", Toast.LENGTH_SHORT).show();
                }
                if(count == len){
                    int[] finalScore = {ageR,symptomsR,diseaseR,travelR,quarantineR};

                    Intent intent = new Intent(SymptomTest.this, SymptomResult.class);
                    intent.putExtra("result", finalScore);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
