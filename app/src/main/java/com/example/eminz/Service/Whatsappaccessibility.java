package com.example.eminz.Service;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.List;

public class Whatsappaccessibility extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (getRootInActiveWindow() == null){
            return;
        }

        AccessibilityNodeInfoCompat rootNodeInfo=AccessibilityNodeInfoCompat
                .wrap(getRootInActiveWindow());
        List<AccessibilityNodeInfoCompat> messageModeList=rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry");
        if (messageModeList == null || messageModeList.isEmpty())
            return;

            AccessibilityNodeInfoCompat messageField = messageModeList.get(0);
            if (messageField == null || messageField.getText().length()==0 || !messageField.getText().toString().endsWith("   "))
                return;


        List<AccessibilityNodeInfoCompat> sendMessageNodeList=rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
        if (sendMessageNodeList == null || sendMessageNodeList.isEmpty())
            return;

        AccessibilityNodeInfoCompat sendMessage=sendMessageNodeList.get(0);
        if (!sendMessage.isVisibleToUser())
            return;

        sendMessage.performAction(AccessibilityNodeInfo.ACTION_CLICK);

        try {
            Thread.sleep(2000);
            performGlobalAction(GLOBAL_ACTION_BACK);
            Thread.sleep(2000);
        }catch (InterruptedException ignored){}

        performGlobalAction(GLOBAL_ACTION_BACK);


    }

    @Override
    public void onInterrupt() {

    }
}
