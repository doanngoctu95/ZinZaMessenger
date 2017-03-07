package chotot.prect.aptech.zinzamessenger.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import chotot.prect.aptech.zinzamessenger.model.BodyRequest;
import chotot.prect.aptech.zinzamessenger.model.Data;
import chotot.prect.aptech.zinzamessenger.model.Friend;
import chotot.prect.aptech.zinzamessenger.model.Message;
import chotot.prect.aptech.zinzamessenger.model.Notification;
import chotot.prect.aptech.zinzamessenger.model.ResultRequest;
import chotot.prect.aptech.zinzamessenger.service.FCMService;
import chotot.prect.aptech.zinzamessenger.utils.Utils;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ASUS on 03/01/2017.
 */

public class SwitchButtonListener extends BroadcastReceiver {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Utils.REJECT_ACTION.equals(action)) {
            Utils.cancelNotification(context);
        } else if (Utils.ACCEPT_ACTION.equals(action)) {
            //Sender
            String id = intent.getStringExtra(FireMsgService.SENDER_ID);
            String token = intent.getStringExtra(FireMsgService.SENDER_TOKEN);
            String body = intent.getStringExtra(FireMsgService.SENDER_NAME);
            String title = "Zinza Messenger";
            //Me
            String mID = Utils.USER_ID;
            String mUrl = Utils.AVATAR_URL;
            String mUserName = Utils.USER_NAME;
            String mToken = Utils.USER_TOKEN;
            Notification mNotification = new Notification(title, mUserName + " đã chấp nhận lời kêt bạn của bạn");
            Data mData = new Data(mID, mToken, mUrl, Utils.TYPE_ANSWER);
            addNewFriend(mID, id);
            instanceRetrofit(mNotification, mData, token, context);
            Utils.cancelNotification(context);
        }

    }

    private void instanceRetrofit(Notification mNotification, Data mData, String tokenFr, final Context mContext) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.FCM_SEND_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FCMService service = retrofit.create(FCMService.class);
        BodyRequest mBodyRequest = new BodyRequest(tokenFr, mNotification, mData);
        Call<ResultRequest> call = service.sendPush(mBodyRequest);

        call.enqueue(new Callback<ResultRequest>() {
            @Override
            public void onResponse(Response<ResultRequest> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Utils.showToast("Success", mContext);
                } else {
                    Utils.showToast("Respone is null", mContext);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Utils.showToast("Failure:" + t.toString(), mContext);
            }
        });

    }

    private void addNewFriend(String idCurrentUser, String idFriend) {
        String idTblFriend = "";
        final String tblContact = idCurrentUser + "-" + idFriend;
        idTblFriend = tblContact;
        final Friend friend = new Friend(idTblFriend, idCurrentUser, idFriend, Utils.createAt());

        createconversation(idCurrentUser,idFriend);

        mReference = mDatabase.getInstance().getReference("tblFriend");
        mReference.orderByChild("mID").equalTo(tblContact).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Toast.makeText(getApplicationContext(), "no add", Toast.LENGTH_LONG).show();
                } else {
                    mReference = mDatabase.getInstance().getReference("tblFriend");
                    mReference.child(tblContact).setValue(friend);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void createconversation(String idCurrentUser, String idFriend){
        String mId = idCurrentUser + "-" + idFriend;
        DatabaseReference messRef = mDatabase.getInstance().getReference("tblChat");
        String idMessage = messRef.push().getKey();
        Message mMessage = new Message(idMessage,idCurrentUser,idFriend,Utils.TEXT,Utils.INTRO_ACCEPT,Utils.createAt());
        messRef.child(mId).child(idMessage).setValue(mMessage);


    }
}
