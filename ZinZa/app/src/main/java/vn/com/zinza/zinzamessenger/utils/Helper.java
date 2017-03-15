package vn.com.zinza.zinzamessenger.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ASUS on 03/06/2017.
 */

public class Helper {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public static void setUserOnline(DatabaseReference mReference){
        mReference = FirebaseDatabase.getInstance().getReference();
        mReference.child("users").child(Utils.USER_ID).child("mStatus").setValue("on");//Set user online
    }
    public static void setUserOffline(DatabaseReference mReference){
        mReference = FirebaseDatabase.getInstance().getReference();
        mReference.child("users").child(Utils.USER_ID).child("mStatus").setValue("off");//Set user offline
    }
    public static String convertTime(String time) {
        SimpleDateFormat output = new SimpleDateFormat("hh:mm");
        SimpleDateFormat formatter = new SimpleDateFormat(Utils.FORMAT_TIME);
        try {
            Date parsed = formatter.parse(time);
            return output.format(parsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;

    }
    public static String getUrlDownload(String url){
        String mURl = url.substring(url.lastIndexOf("apis.com")+8);
        return mURl.substring(0,mURl.lastIndexOf("---"));
    }
    public static String getURLImage(String url){
        return url.substring(0,url.lastIndexOf("---"));
    }
    public static String getName(String url){
        return url.substring(url.lastIndexOf("---")+3);
    }

}
