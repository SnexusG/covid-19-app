package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

public class SymptomResult extends AppCompatActivity {

    private TextView result;
    private TextView helpline;
    private TextView cases;
    private TextView cured;
    private TextView death;
    private DatabaseReference firebaseDatabase, demoRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    String state;
    int stateNo = 0;
    String casesCount;
    String curedCount;
    String deathCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_result);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            firebaseUser = firebaseAuth.getCurrentUser();
        }

        Resources resource = getResources();
        Drawable drawable = resource.getDrawable(R.drawable.circular);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
        result = findViewById(R.id.tv);
        helpline = findViewById(R.id.stateHelpline);
        cases = findViewById(R.id.confirmed_cases);
        cured = findViewById(R.id.Cured_Migrated);
        death = findViewById(R.id.death);

        firebaseDatabase.child("Users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                state = dataSnapshot.getValue(String.class);
                System.out.println(state);
                helpline.setText(state);
                helpline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);;
                        if(state.equals("Jammu and Kashmir")) {
                            intent.setData(Uri.parse("tel:01912520982"));
                        }else if(state.equals("Andhra Pradesh")) {
                            intent.setData(Uri.parse("tel:08662410978"));
                        }else if(state.equals("Andaman and Nicobar Islands")) {
                            intent.setData(Uri.parse("tel:03192232102"));
                        }else if(state.equals("Assam")){
                                intent.setData(Uri.parse("tel:6913347770"));
                        }else if(state.equals("Bihar")){
                                intent.setData(Uri.parse("tel:104"));
                        }else if(state.equals("Chandigarh")){
                                intent.setData(Uri.parse("tel:9779558282"));
                        }else if(state.equals("Chhattisgarh")){
                                intent.setData(Uri.parse("tel:104"));
                        }else if(state.equals("Delhi")){
                                intent.setData(Uri.parse("tel:01122307145"));
                        }else if(state.equals("Goa")){
                                intent.setData(Uri.parse("tel:104"));
                        }else if(state.equals("Gujarat")){
                                intent.setData(Uri.parse("tel:104"));
                        }else if(state.equals("Arunachal Pradesh")){
                                intent.setData(Uri.parse("tel:9436055743"));
                        }else if(state.equals("Haryana")){
                                intent.setData(Uri.parse("tel:8558893911"));
                        }else if(state.equals("Himachal Pradesh")){
                                intent.setData(Uri.parse("tel:104"));
                        }else if(state.equals("Jharkhand")){
                                intent.setData(Uri.parse("tel:104"));
                        }else if(state.equals("Karnataka")){
                                intent.setData(Uri.parse("tel:104"));
                        }else if(state.equals("Kerala")){
                                intent.setData(Uri.parse("tel:04712552056"));
                        }else if(state.equals("Ladakh")){
                                intent.setData(Uri.parse("tel:01982256462"));
                        }else if(state.equals("Madhya Pradesh")){
                                intent.setData(Uri.parse("tel:104"));
                        }else if(state.equals("Maharashtra")){
                                intent.setData(Uri.parse("tel:02026127394"));
                        }else if(state.equals("Manipur")){
                                intent.setData(Uri.parse("tel:3852411668"));
                        }else if(state.equals("Mizoram")){
                                intent.setData(Uri.parse("tel:102"));
                        }else if(state.equals("Odisha")){
                                intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:9439994859"));
                        }else if(state.equals("Puducherry")){
                                intent.setData(Uri.parse("tel:104"));
                        }else if(state.equals("Punjab")){
                                intent.setData(Uri.parse("tel:104"));
                        }else if(state.equals("Rajasthan")){
                                intent.setData(Uri.parse("tel:0141-2225624"));
                        }else if(state.equals("Tamil Nadu")){
                                intent.setData(Uri.parse("tel:04429510500"));
                        }else if(state.equals("Telengana")){
                                intent.setData(Uri.parse("tel:104"));
                        }else if(state.equals("Tripura")){
                                intent.setData(Uri.parse("tel:03812315879"));
                        }else if(state.equals("Uttarakhand")){
                                intent.setData(Uri.parse("tel:104"));
                        }else if(state.equals("Uttar Pradesh")){
                                intent.setData(Uri.parse("tel:18001805145"));
                        }else if(state.equals("West Bengal")) {
                            intent.setData(Uri.parse("tel:1800313444222"));
                        }else{
                                intent.setData(Uri.parse("tel:02026127394"));
                            }

                        startActivity(intent);
                    }
                });
                content content = new content();
                content.execute();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Intent mIntent = getIntent();
        int[] finalScores = mIntent.getIntArrayExtra("result");

        //checking if the data was received properly from SymptomTest activity
        int chance = 0;
        System.out.println("HERE result : ");
        for(int i : finalScores){
            System.out.println(i);
            chance+=i;
        }
        float chanceP = (float)chance/33;
        result.setText(String.valueOf(Math.round(chanceP*100)) + "%\nprobability\nof corona");
        System.out.println(chanceP*100 + " " +  chance);
        mProgress.setProgress(Math.round(chanceP*100));   // Main Progress
        mProgress.setSecondaryProgress(100-Math.round(chanceP*100)); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);


    }

    private class content extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            cases.setText("Confirmed cases : " + casesCount);
            cured.setText("Cured/Migrated : " + curedCount);
            death.setText("Death " + deathCount);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://www.mohfw.gov.in/";
            try{
                System.out.println("HERE" + state);
                Document doc = Jsoup.connect(url).get();
                Elements data = doc.select("div.col-xs-12");
                System.out.println(stateNo);
                if(state.equals("Jammu and Kashmir")) {
                    stateNo = 12;
                }else if(state.equals("Andhra Pradesh")) {
                    stateNo = 0;
                }else if(state.equals("Andaman and Nicobar Islands")) {
                    stateNo = 1;
                }else if(state.equals("Assam")){
                    stateNo = 3;
                }else if(state.equals("Bihar")){
                    stateNo = 4;
                }else if(state.equals("Chandigarh")){
                    stateNo = 5;
                }else if(state.equals("Chhattisgarh")){
                    stateNo = 6;
                }else if(state.equals("Delhi")){
                    stateNo = 7;
                }else if(state.equals("Goa")){
                    stateNo = 8;
                }else if(state.equals("Gujarat")){
                    stateNo = 9;
                }else if(state.equals("Arunachal Pradesh")){
                    stateNo = 2;
                }else if(state.equals("Haryana")){
                    stateNo = 10;
                }else if(state.equals("Himachal Pradesh")){
                    stateNo = 11;
                }else if(state.equals("Jharkhand")){
                    stateNo = 13;
                }else if(state.equals("Karnataka")){
                    stateNo = 14;
                }else if(state.equals("Kerala")){
                    stateNo = 15;
                }else if(state.equals("Ladakh")){
                    stateNo = 16;
                }else if(state.equals("Madhya Pradesh")){
                    stateNo = 17;
                }else if(state.equals("Maharashtra")){
                    stateNo = 18;
                }else if(state.equals("Manipur")){
                    stateNo = 19;
                }else if(state.equals("Mizoram")){
                    stateNo = 20;
                }else if(state.equals("Odisha")){
                    stateNo = 21;
                }else if(state.equals("Puducherry")){
                    stateNo = 22;
                }else if(state.equals("Punjab")){
                    stateNo = 23;
                }else if(state.equals("Rajasthan")){
                    stateNo = 24;
                }else if(state.equals("Tamil Nadu")){
                    stateNo = 25;
                }else if(state.equals("Telengana")){
                    stateNo = 26;
                }else if(state.equals("Tripura")){
                    stateNo = 27;
                }else if(state.equals("Uttarakhand")){
                    stateNo = 28;
                }else if(state.equals("Uttar Pradesh")){
                    stateNo = 29;
                }else if(state.equals("West Bengal")) {
                    stateNo = 30;
                }else{
                    stateNo = -1;
                }
                System.out.println(stateNo);
                if(stateNo != -1) {
                    casesCount = data.select("tbody")
                            .select("tr")
                            .eq(stateNo)
                            .select("td")
                            .eq(2)
                            .text();
                    curedCount = data.select("tbody")
                            .select("tr")
                            .eq(stateNo)
                            .select("td")
                            .eq(3)
                            .text();
                    deathCount = data.select("tbody")
                            .select("tr")
                            .eq(stateNo)
                            .select("td")
                            .eq(4)
                            .text();
                }else{
                    casesCount = "NO data";
                }
            System.out.println("HERE"  + casesCount);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
