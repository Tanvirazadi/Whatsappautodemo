package com.example.eminz.Util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.eminz.Scheduler.SMSScheduler;
import com.example.whatsappdemo.R;

public class EmptinessTextWatcher implements TextWatcher {
    private SMSScheduler addSmsActivity;
    private final AutoCompleteTextView formContact;
    private final EditText sms;

    public EmptinessTextWatcher(SMSScheduler addSmsActivity, AutoCompleteTextView formContact, EditText sms) {
        this.addSmsActivity = addSmsActivity;
        this.formContact = formContact;
        this.sms = sms;

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
