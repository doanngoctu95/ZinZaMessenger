package chotot.prect.aptech.zinzamessenger.notification;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import chotot.prect.aptech.zinzamessenger.utils.Utils;

/**
 * Created by ASUS on 02/28/2017.
 */

public class FireIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Utils.TOKEN = token;
    }
}
