package com.example.gavin.newapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivitycontact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove header
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //xml layout
        setContentView(R.layout.activity_main_activitycontact);

//input values
        final EditText yourlastname        = (EditText) findViewById(R.id.yourlastname);
        final EditText yourfirstname        = (EditText) findViewById(R.id.yourfirstname);
        final EditText yoursubject     = (EditText) findViewById(R.id.yoursubject);
        final EditText youremail       = (EditText) findViewById(R.id.youremail);
        final EditText yourmessage     = (EditText) findViewById(R.id.yourmessage);

        Button email = (Button) findViewById(R.id.postmessage);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstname      = yourfirstname.getText().toString();
                String lastname      = yourlastname.getText().toString();
                String subject   = yoursubject.getText().toString();
                String email     = youremail.getText().toString();
                String message   = yourmessage.getText().toString();
               //warning if no name is added
                if (TextUtils.isEmpty(firstname)){
                    yourfirstname.setError("Enter Your First Name");
                    yourfirstname.requestFocus();
                    return;
                }
//warning if no last name is added
                if (TextUtils.isEmpty(lastname)){
                    yourlastname.setError("Enter Your last Name");
                    yourlastname.requestFocus();
                    return;
                }
//if email doesn't contain required characters
                Boolean onError = false;
                if (!isValidEmail(email)) {
                    onError = true;
                    youremail.setError("Invalid Email");
                    return;
                }
//subject left empty
                if (TextUtils.isEmpty(subject)){
                    yoursubject.setError("Enter Your Subject");
                    yoursubject.requestFocus();
                    return;
                }
//message left empty
                if (TextUtils.isEmpty(message)){
                    yourmessage.setError("Enter Your Message");
                    yourmessage.requestFocus();
                    return;
                }
                Intent sendEmail = new Intent(android.content.Intent.ACTION_SEND);

            //auto fill data on email app
                sendEmail.setType("plain/text");
                sendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"RECIEVING-EMAIL-HERE@gmail.com"});
                sendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                sendEmail.putExtra(android.content.Intent.EXTRA_TEXT,
                        "First name:"+firstname+'\n'+'\n'+"Last name:"+lastname+'\n'+'\n'+"Email ID:"+email+'\n'+'\n'+"Message:"+'\n'+'\n'+message);

            // Sends to email
                startActivity(Intent.createChooser(sendEmail, "Send mail..."));


            }
        });
    }
    //valid email characters
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_0-9a-zA-Z-\\+]+(\\.[_0-9a-zA-Z-]+)*@"
                + "[0-9a-zA-Z-]+(\\.[0-9a-zA-Z]+)*(\\.[a-zA-Z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    }












/***************************************************************************************
 *    Title: Develop simple Email app in Android Studio
 *    Author: Tihomir RAdeff

 *    Date: 20/12/2017
 *
 *    Availability: https://www.youtube.com/watch?v=_25Rb5R1Ywg
 *
 ***************************************************************************************/








