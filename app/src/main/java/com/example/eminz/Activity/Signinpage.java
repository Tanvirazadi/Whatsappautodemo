package com.example.eminz.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eminz.Sqlite.Databsehelper;
import com.example.whatsappdemo.R;


public class Signinpage extends AppCompatActivity implements View.OnClickListener {
    Button sign;
    EditText usernam,pass;
    TextView forget;
    CheckBox check;
    Databsehelper databsehelper;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinpage);

        sign=findViewById(R.id.signbutton);
        usernam=findViewById(R.id.usernam);
        pass=findViewById(R.id.passwod);
        forget=findViewById(R.id.forgetpass);
        check=findViewById(R.id.checkBox2);


        sign.setOnClickListener(this);
        forget.setOnClickListener(this);

        databsehelper= new Databsehelper(this);
        SQLiteDatabase sqLiteDatabase=databsehelper.getWritableDatabase();




    }

    @Override
    public void onClick(View v) {
        String  username = usernam.getText().toString();
        String password=pass.getText().toString();

        if (check.isChecked()) {

            if (v.getId() == R.id.signbutton) {
                Boolean result = databsehelper.findpassword(username, password);

                if (result) {
                    Intent intent = new Intent(Signinpage.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Username or password is invalid", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "You have to agree to Scheduler's Terms of use and privacy policy", Toast.LENGTH_SHORT).show();

            }
        }
    }
}