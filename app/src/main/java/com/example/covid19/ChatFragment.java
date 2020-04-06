package com.example.covid19;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static android.app.Activity.RESULT_OK;

public class ChatFragment extends Fragment {

    private static int sign_in_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activity_chat;
    FloatingActionButton fab;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity_chat = getActivity().findViewById(R.id.chat_act);
        fab = getActivity().findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = getActivity().findViewById(R.id.input_message);
                FirebaseDatabase.getInstance().getReference("chat").push().setValue(new ChatMessage(input.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");
            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Toast.makeText(getActivity(), "No one is logged in!", Toast.LENGTH_SHORT).show();
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), sign_in_REQUEST_CODE);
        }else{
            Snackbar.make(activity_chat,"Welcome, " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();
            displayMessage();
        }


    }

    private void displayMessage() {
        ListView listOfMessage = getActivity().findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference("chat")){
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText, messageUser, messageTime;
                messageText = v.findViewById(R.id.message_text);
                messageUser = v.findViewById(R.id.message_user);
                messageTime = v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getMessageTime()));

            }
        };
        listOfMessage.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == sign_in_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Snackbar.make(activity_chat, "Successfully signed in, Welcome", Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(activity_chat, "Sign in failed", Snackbar.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.main_sign_out){
            AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_chat ,"You have been signed out", Snackbar.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat,container,false);
    }
}
