package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import org.w3c.dom.Text;

public class SymptomResult extends AppCompatActivity {

    private TextView result;
    private TextView helpline;
    private DatabaseReference firebaseDatabase, demoRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    String state;

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
                        switch(state){
                            case "Jammu and Kashmir":
                                intent.setData(Uri.parse("tel:01912520982"));
                            case "Andhra Pradesh":
                                intent.setData(Uri.parse("tel:08662410978"));
                            case "Andaman and Nicobar Islands":
                                intent.setData(Uri.parse("tel:03192232102"));
                            case "Assam":
                                intent.setData(Uri.parse("tel:6913347770"));
                            case "Bihar":
                                intent.setData(Uri.parse("tel:104"));
                            case "Chandigarh":
                                intent.setData(Uri.parse("tel:9779558282"));
                            case "Chhattisgarh":
                                intent.setData(Uri.parse("tel:104"));
                            case "Delhi":
                                intent.setData(Uri.parse("tel:01122307145"));
                            case "Goa":
                                intent.setData(Uri.parse("tel:104"));
                            case "Gujarat":
                                intent.setData(Uri.parse("tel:104"));
                            case "Arunachal Pradesh":
                                intent.setData(Uri.parse("tel:9436055743"));
                            case "Haryana":
                                intent.setData(Uri.parse("tel:8558893911"));
                            case "Himachal Pradesh":
                                intent.setData(Uri.parse("tel:104"));
                            case "Jharkhand":
                                intent.setData(Uri.parse("tel:104"));
                            case "Karnataka":
                                intent.setData(Uri.parse("tel:104"));
                            case "Kerala":
                                intent.setData(Uri.parse("tel:04712552056"));
                            case "Ladakh":
                                intent.setData(Uri.parse("tel:01982256462"));
                            case "Madhya Pradesh":
                                intent.setData(Uri.parse("tel:104"));
                            case "Maharashtra":
                                intent.setData(Uri.parse("tel:02026127394"));
                            case "Manipur":
                                intent.setData(Uri.parse("tel:3852411668"));
                            case "Mizoram":
                                intent.setData(Uri.parse("tel:102"));
                            case "Odisha":
                                intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:9439994859"));
                            case "Puducherry":
                                intent.setData(Uri.parse("tel:104"));
                            case "Punjab":
                                intent.setData(Uri.parse("tel:104"));
                            case "Rajasthan":
                                intent.setData(Uri.parse("tel:0141-2225624"));
                            case "Tamil Nadu":
                                intent.setData(Uri.parse("tel:04429510500"));
                            case "Telengana":
                                intent.setData(Uri.parse("tel:104"));
                            case "Tripura":
                                intent.setData(Uri.parse("tel:03812315879"));
                            case "Uttarakhand":
                                intent.setData(Uri.parse("tel:104"));
                            case "Uttar Pradesh":
                                intent.setData(Uri.parse("tel:18001805145"));
                            case "West Bengal":
                                intent.setData(Uri.parse("tel:1800313444222"));
                            default:
                                intent.setData(Uri.parse("tel:02026127394"));
                        }
                        startActivity(intent);
                    }
                });

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
}
