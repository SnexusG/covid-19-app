package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Login extends AppCompatActivity {

    private EditText emailid;
    private MaterialEditText newUserName, newEmailId;
    private MaterialEditText newPassword;
    private EditText passWord;
    private Button signInButton;
    private Button signUpButton;
    private GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Spinner stateSelect;
    private DatabaseReference userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailid = findViewById(R.id.entered_email);
        passWord = findViewById(R.id.entered_password);
        signInButton = findViewById(R.id.signInBtn);
        signUpButton = findViewById(R.id.signUpBtn);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar);
        userInfo = FirebaseDatabase.getInstance().getReference("Users");


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticate(); }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpDialog();
            }
        });

    }

    private void showSignUpDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please Fill in the details");
        LayoutInflater inflater = this.getLayoutInflater();
        final View sign_up_layout = inflater.inflate(R.layout.sign_up_onlu, null);
        newPassword = sign_up_layout.findViewById(R.id.editPassword);
        newEmailId = sign_up_layout.findViewById(R.id.editEmail);
        stateSelect = sign_up_layout.findViewById(R.id.spinner1);

        final String states[] = {"Andhra Pradesh", "Andaman and Nicobar Islands", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh"
                ,"Chhattisgarh", "Delhi", "Goa", "Gujarat", "Haryana","Himachal Pradesh", "Jammu and Kashmir", "Jharkhand",
                "Karnataka", "Kerala", "Ladakh", "Madhya Pradesh", "Maharashtra", "Manipur", "Mizoram", "Odisha", "Puducherry",
                "Punjab", "Rajasthan", "Tamil Nadu", "Telengana", "Tripura", "Uttarakhand", "Uttar Pradesh", "West Bengal"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, states);
        stateSelect.setAdapter(adapter);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.account_icon);

        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // sToast.makeText(SignUP_SIgnIn.this, "Register Clicked", Toast.LENGTH_SHORT).show();
                String email = newEmailId.getText().toString();
                String  password = newPassword.getText().toString();
                final String state = stateSelect.getSelectedItem().toString();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this,"Please enter email",Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this,"Please enter password",Toast.LENGTH_LONG).show();
                    return;
                }
                progressDialog.setMessage("Registering Please Wait...");
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Registeration Sucessful", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
                            userInfo.child(firebaseUser.getUid()).setValue(state);

                            Intent intent = new Intent(Login.this, home.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),"Registeration Failed", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

            }
        });
        alertDialog.show();
    }
    public void authenticate() {
        String email = emailid.getText().toString().trim();
        String password = passWord.getText().toString().trim();

        if (email.isEmpty()) {
            emailid.setError("Email is required");
            emailid.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailid.setError("Please enter a valid email");
            emailid.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passWord.setError("Password is required");
            passWord.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passWord.setError("Minimum length of password should be 6");
            passWord.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(Login.this, home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onStart() {
      super.onStart();
     if (mAuth.getCurrentUser() != null ) {
        startActivity(new Intent(Login.this, home.class));
       finish();
    }
    }

}
