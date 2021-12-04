package com.example.eminz.Util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {

    public  static void  keyhashes(Context context){
        try {
            PackageInfo info=context.getPackageManager().
                    getPackageInfo(context.getPackageName(),
                            PackageManager.GET_SIGNATURES);

            for (Signature signature:info.signatures){
                MessageDigest messageDigest=MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                String KeyHashes=new String(Base64.encode(messageDigest.digest(),0));
                Log.d("key hashes","Facebook key Hashes"+KeyHashes);
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
    }
}
