package com.example.eminz.Sqlite;

public class User {

    private String Name,Username, Email, Password, Confirmpass;



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name=name;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
       this.Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
      this.Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getConfirmpass() {
        return Confirmpass;
    }

    public void setConfirmpass(String confirmpass) {
        this.Confirmpass = confirmpass;
    }
}
