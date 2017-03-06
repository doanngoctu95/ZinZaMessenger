package chotot.prect.aptech.zinzamessenger.utils;

import com.google.firebase.database.DatabaseReference;

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
}
