package chotot.prect.aptech.zinzamessenger.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.activity.ChattingActivity;
import chotot.prect.aptech.zinzamessenger.model.Message;
import chotot.prect.aptech.zinzamessenger.model.User;
import chotot.prect.aptech.zinzamessenger.utils.Utils;

/**
 * Created by ASUS on 02/13/2017.
 */

public class AdapterMessage extends BaseAdapter {
    private Context mContext;
    private int mLayout;
    private List<Message> mListMessage;
    private List<Message> searchList;
    private List<String> mListUserKeys;
    private List<String> mListMessageKeys;
    private String mPreviousMessage;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserReference;

    public AdapterMessage(Context mContext, int mLayout, List<Message> mListMessage) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mListMessage = mListMessage;

        mListUserKeys = new ArrayList<>();
        mListMessageKeys = new ArrayList<>();
        searchList = new ArrayList<>();
        searchList.addAll(mListMessage);
    }

    public void refill(Message message) {
        if(mListMessage.size() == 0){
            mPreviousMessage = message.getmId();
            mListMessageKeys.add(mPreviousMessage);
            mListMessage.add(message);
        } else {
            int index = mListMessageKeys.indexOf(mPreviousMessage);
            mListMessage.remove(index);
            mListMessage.add(message);
        }


        notifyDataSetChanged();
    }

    public void chaneMessage(int index, Message message) {
        mListMessage.add(index, message);
        notifyDataSetChanged();
    }

    public void removeMessage(int index) {
        mListMessage.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListMessage.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(mLayout, null);
        ImageView avatarUser = (ImageView) convertView.findViewById(R.id.imgAvatar);
        TextView nameUser = (TextView) convertView.findViewById(R.id.txtName);
        TextView contentMessage = (TextView) convertView.findViewById(R.id.txtContent);
        TextView timeMessage = (TextView) convertView.findViewById(R.id.txtTime);
        TextView numberNewMs = (TextView) convertView.findViewById(R.id.txtNumberNewMessage);

        String idFriend = "";
        String idSender = mListMessage.get(position).getmIdSender();
        String idRecipent = mListMessage.get(position).getmIdRecipient();
        if (Utils.USER_ID.equals(idSender)) {
            idFriend = idRecipent;
        } else {
            idFriend = idSender;
        }
        numberNewMs.setText("   1   ");
        getUser(idFriend, avatarUser, nameUser,convertView);
        contentMessage.setText(mListMessage.get(position).getmContent());
        timeMessage.setText(this.convertTime(mListMessage.get(position).getmTime()));
        return convertView;
    }

    private void getUser(String idFriend, final ImageView avatar, final TextView txtname,final View view) {
        mUserReference = mDatabase.getInstance().getReference().child("users");
        mUserReference.orderByChild("mId").equalTo(idFriend).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    mListUserKeys.add(dataSnapshot.getKey());
                    final User user = dataSnapshot.getValue(User.class);
                    Picasso.with(mContext).load(user.getmAvatar()).into(avatar);
                    txtname.setText(user.getmUsername());
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent chatting = new Intent(mContext, ChattingActivity.class);
                            chatting.putExtra(Utils.FR_URL,user.getmAvatar());
                            chatting.putExtra(Utils.FR_NAME,user.getmUsername());
                            mContext.startActivity(chatting);
                        }
                    });
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mListMessage.clear();
        if (charText.length() == 0) {
            mListMessage.addAll(searchList);
        } else {
            for (Message s : searchList) {
                // search by name...
                String nameSearch = "";
                if (nameSearch.toLowerCase(Locale.getDefault()).contains(charText)) {
                    mListMessage.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }

    private String convertTime(String time) {
        SimpleDateFormat output = new SimpleDateFormat("hh:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        try {
            Date parsed = formatter.parse(time);
            return output.format(parsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;

    }
}
