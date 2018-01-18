package com.example.gavin.newapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Addmarker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove header
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       //xml layout
        setContentView(R.layout.activity_addmarker);

//input forms
        final EditText yourlongitude        = (EditText) findViewById(R.id.your_longitude);
        final EditText yourlatitude        = (EditText) findViewById(R.id.your_latitude);
        final EditText youremail       = (EditText) findViewById(R.id.your_email);
        final EditText youreventname     = (EditText) findViewById(R.id.your_eventname);
        final EditText youradditionalinformation     = (EditText) findViewById(R.id.your_additionalinformation);


//button to send email
        Button email = (Button) findViewById(R.id.sendrequest);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String longitude      = yourlongitude.getText().toString();
                String latitude      = yourlatitude.getText().toString();
                String email     = youremail.getText().toString();
                String eventname   = youreventname.getText().toString();
                String additionalinformation   = youradditionalinformation.getText().toString();
                //longitude not filled in
                if (TextUtils.isEmpty(longitude)){
                    yourlongitude.setError("Enter Your longitude");
                    yourlongitude.requestFocus();
                    return;
                }
//latitude not filled in
                if (TextUtils.isEmpty(latitude)) {
                    yourlatitude.setError("Enter Your latitude");
                    yourlatitude.requestFocus();
                    return;
                }
//email doesn't contain required characters
                Boolean onError = false;
                if (!isValidEmail(email)) {
                    onError = true;
                    youremail.setError("Invalid Email");
                    return;
                }
//subject empty
                if (TextUtils.isEmpty(eventname)){
                    youreventname.setError("Enter Your Subject");
                    youreventname.requestFocus();
                    return;
                }
//information blank
                if (TextUtils.isEmpty(additionalinformation)){
                    youradditionalinformation.setError("Enter additional Information");
                    youradditionalinformation.requestFocus();
                    return;
                }

                Intent sendEmail = new Intent(android.content.Intent.ACTION_SEND);

            /* Fill it with Data */
                sendEmail.setType("plain/text");
                sendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"RECIEVING-EMAIL-HERE@gmail.com"});
                sendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT, eventname);
                sendEmail.putExtra(android.content.Intent.EXTRA_TEXT,
                        "longitude:"+longitude+'\n'+"longitude:"+latitude+'\n'+"Email ID:"+email+'\n'+"Message:"+'\n'+additionalinformation);

            /* Send it off to the Activity-Chooser */
                startActivity(Intent.createChooser(sendEmail, "Sending request..."));


            }
        });
    }


    // correct email characters

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}
