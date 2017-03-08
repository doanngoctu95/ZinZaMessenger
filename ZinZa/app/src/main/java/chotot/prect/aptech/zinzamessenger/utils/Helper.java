package chotot.prect.aptech.zinzamessenger.utils;

import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ASUS on 03/06/2017.
 */

public class Helper {
    public static void setUserOnline(DatabaseReference mReference){
        mReference.child("users").child(Utils.USER_ID).child("mStatus").setValue("on");//Set user online
    }
    public static void setUserOffline(DatabaseReference mReference){
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
}
