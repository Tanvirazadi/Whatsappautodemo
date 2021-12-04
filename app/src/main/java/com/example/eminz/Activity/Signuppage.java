package com.example.eminz.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsappdemo.R;
import com.example.eminz.Sqlite.Databsehelper;
import com.example.eminz.Sqlite.User;


public class Signuppage extends AppCompatActivity {

    EditText name,email,user,pass,conpass;
    Button signup;
    CheckBox checkBox;
    Databsehelper databsehelper;
    User use;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppage);

        name=findViewById(R.id.namesignup);
        email=findViewById(R.id.emailsignup);
        user=findViewById(R.id.usersignup);
        pass=findViewById(R.id.editTextTextPassword);
        conpass=findViewById(R.id.passconfirmsignup);
        signup=findViewById(R.id.signupbutton);
        checkBox=findViewById(R.id.checkBoxsignup);


        databsehelper= new Databsehelper(this);
        use=new User();




        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  Nam=name.getText().toString();
                String  mail=email.getText().toString();
                String  username=user.getText().toString();
                String  password=pass.getText().toString();
                String  confirmpass=conpass.getText().toString();
                String pattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                use.setName(Nam);
                use.setUsername(username);
                use.setPassword(password);
                use.setEmail(mail);
                use.setConfirmpass(confirmpass);

                if (Nam.length()==0){
                    name.setError("This field is required");
                }if (mail.isEmpty() || !mail.matches(pattern)){
                    email.setError("Invalid Email Address");
                }if (username.isEmpty()){
                    user.setError("This field is required");
                }if (password.isEmpty()|| password.length()<4){
                    pass.setError("Password must be minimum 4 characters");

                }if (!confirmpass.matches(password) || confirmpass.isEmpty()){
                    conpass.setError("Password does not match");

                }
                if (!checkBox.isChecked()){
                    Toast.makeText(getApplicationContext(), "You have to agree to Scheduler's Terms of use and privacy policy", Toast.LENGTH_SHORT).show();

                }

                long rowid;
                rowid=databsehelper.insert(use);
                if (rowid>0){
                    Toast.makeText(getApplicationContext(), "Signup Successful", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Signuppage.this, Signinpage.class);
                    startActivity(intent);

                }else {
                    Toast.makeText(getApplicationContext(), "Signup Unsuccessful", Toast.LENGTH_SHORT).show();

                }







            }
        });




    }




}