package chotot.prect.aptech.zinzamessenger.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by ASUS on 02/20/2017.
 */

public class Utils {
    public static AlertDialog buildAlertDialog(String title, String message, boolean isCancelable, Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title);

        if(isCancelable){
            builder.setPositiveButton(android.R.string.ok, null);
        }else {
            builder.setCancelable(false);
        }
        return builder.create();
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    public final static void showToast(String alert,Context mContext){
        Toast.makeText(mContext,alert,Toast.LENGTH_SHORT).show();
    }

    public final static void dissmiss(ProgressDialog progressDialog){
        progressDialog.dismiss();
    }
}
