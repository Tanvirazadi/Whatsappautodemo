package com.example.eminz.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsappdemo.R;
import com.example.eminz.Util.Util;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class Loginpage extends AppCompatActivity  implements FacebookCallback<LoginResult> {
    Button signin;
    TextView skipo,creato;
    CheckBox checkBox;
    CallbackManager manager;
    LoginButton fb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);


        Util.keyhashes(this);
        manager=CallbackManager.Factory.create();
        fb=findViewById(R.id.facebook);
        fb.registerCallback(manager,this);
        


        signin=findViewById(R.id.signin);
        skipo=findViewById(R.id.skipo);
        creato=findViewById(R.id.create);
        checkBox=findViewById(R.id.checkBox);






        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Signinpage.class);
                startActivity(intent);

            }

        });



        skipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
             else {
                    Toast.makeText(getApplicationContext(), "You have to agree to Scheduler's Terms of use and privacy policy", Toast.LENGTH_SHORT).show();
                }
            }

        });
        creato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Signuppage.class);
                startActivity(intent);
            }
        });



    }


    @Override
    public void onSuccess(LoginResult loginResult) {
        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {
        Toast.makeText(getApplicationContext(), "Login Error found", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        manager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}