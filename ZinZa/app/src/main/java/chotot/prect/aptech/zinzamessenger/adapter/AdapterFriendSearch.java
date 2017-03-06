package chotot.prect.aptech.zinzamessenger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.model.BodyRequest;
import chotot.prect.aptech.zinzamessenger.model.Data;
import chotot.prect.aptech.zinzamessenger.model.Notification;
import chotot.prect.aptech.zinzamessenger.model.ResultRequest;
import chotot.prect.aptech.zinzamessenger.model.User;
import chotot.prect.aptech.zinzamessenger.service.FCMService;
import chotot.prect.aptech.zinzamessenger.utils.Utils;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ASUS on 02/27/2017.
 */

public class AdapterFriendSearch extends BaseAdapter {
    private Context mContext;
    private int mLayout;
    private List<User> mListUser;
    private User mUser;
    public static boolean ishaveData = false;

    private DatabaseReference mReference;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    public AdapterFriendSearch(Context mContext, int mLayout, List<User> mListUser, User mUser) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mListUser = mListUser;
        mAuth = FirebaseAuth.getInstance();
        this.mUser = mUser;
    }
    public void refill(User user){
        mListUser.add(user);
        notifyDataSetChanged();
    }
    public void changeUser(int index, User user) {
        mListUser.set(index,user);
        notifyDataSetChanged();
    }
    public void removeUser(int index){
        mListUser.remove(index);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mListUser.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Utils.showToast("Size"+mListUser.size(),mContext);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(mLayout,null);
        ImageView avatarUser = (ImageView)convertView.findViewById(R.id.imgFriendSearch);
        TextView nameUser = (TextView)convertView.findViewById(R.id.txtNameFriendSearch);
        Button btnAddFr= (Button) convertView.findViewById(R.id.btnAddFriend);
        if(!mListUser.get(position).getmAvatar().equals("")){
            Picasso.with(mContext).load(mListUser.get(position).getmAvatar()).into(avatarUser);
        }

        nameUser.setText(mListUser.get(position).getmUsername());
        btnAddFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idFr= mListUser.get(position).getmId();
                String tokenFr = mListUser.get(position).getmToken();
                String currentAvatarUrl = mUser.getmAvatar();
                String title = "Zinza Messenger";
                String body = mUser.getmUsername() + " muốn gửi lời mời kết bạn đến bạn.Bạn có đồng ý không ?";

                String currentToken = mUser.getmToken();
                Notification mNotification = new Notification(title,body);
                Data mData = new Data(Utils.USER_ID,currentToken,currentAvatarUrl,Utils.TYPE_ADD);
                instanceRetrofit(mNotification,mData,tokenFr);
            }
        });
        return convertView;
    }


    private String createAt(){
        return java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    }
    private void instanceRetrofit(Notification mNotification,Data mData,String tokenFr){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.FCM_SEND_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FCMService service = retrofit.create(FCMService.class);
        BodyRequest mBodyRequest = new BodyRequest(tokenFr,mNotification,mData);
        Call<ResultRequest> call = service.sendPush(mBodyRequest);

        call.enqueue(new Callback<ResultRequest>() {
            @Override
            public void onResponse(Response<ResultRequest> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    Utils.showToast("Success",mContext);
                } else {
                    Utils.showToast("Respone is null",mContext);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Utils.showToast("Failure:"+t.toString(),mContext);
            }
        });
    }

}
