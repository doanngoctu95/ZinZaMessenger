package vn.com.zinza.zinzamessenger.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import vn.com.zinza.zinzamessenger.R;
import vn.com.zinza.zinzamessenger.activity.ChattingActivity;
import vn.com.zinza.zinzamessenger.model.Message;
import vn.com.zinza.zinzamessenger.model.User;
import vn.com.zinza.zinzamessenger.utils.Helper;
import vn.com.zinza.zinzamessenger.utils.Utils;

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

    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserReference;

    private String mSender_id = "";
    private String mRecipient_id = "";

    public AdapterMessage(Context mContext, int mLayout, List<Message> mListMessage) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mListMessage = mListMessage;

        mListUserKeys = new ArrayList<>();
        mListMessageKeys = new ArrayList<>();
        searchList = new ArrayList<>();
        searchList.addAll(mListMessage);
    }

    private String getIdFr(Message message) {
        String result = "";
        if (Utils.USER_ID.equals(message.getmIdSender())) {
            result = message.getmIdRecipient();
        } else if (Utils.USER_ID.equals(message.getmIdRecipient())) {
            result = message.getmIdSender();
        }
        return result;
    }

    public void refill(Message message) {
        String idFr = getIdFr(message);
        if (!mListMessageKeys.contains(idFr)) {
            mListMessageKeys.add(idFr);
            mListMessage.add(message);
        } else {
            for (int i = 0; i < mListMessage.size(); i++) {
                if (mListMessage.get(i).getmIdRecipient().equals(idFr)) {
                    mListMessage.remove(i);
                    mListMessage.add(message);
                }
            }
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
        EmojiconTextView contentMessage = (EmojiconTextView) convertView.findViewById(R.id.txtContent);
        TextView timeMessage = (TextView) convertView.findViewById(R.id.txtTime);
        TextView numberNewMs = (TextView) convertView.findViewById(R.id.txtNumberNewMessage);

        String idFriend = "";
        mSender_id = mListMessage.get(position).getmIdSender();
        mRecipient_id = mListMessage.get(position).getmIdRecipient();
        if (Utils.USER_ID.equals(mSender_id)) {
            idFriend = mRecipient_id;
        } else {
            idFriend = mSender_id;
        }
        mListMessageKeys.add(idFriend);
        numberNewMs.setText("   1   ");
        getUser(idFriend, avatarUser, nameUser, convertView);
        contentMessage.setText(mListMessage.get(position).getmContent());
        timeMessage.setText(Helper.convertTime(mListMessage.get(position).getmTime()));
        return convertView;
    }

    private void getUser(String idFriend, final ImageView avatar, final TextView txtname, final View view) {
        mUserReference = mDatabase.getInstance().getReference().child("users");
        mUserReference.orderByChild("mId").equalTo(idFriend).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    mListUserKeys.add(dataSnapshot.getKey());
                    final User user = dataSnapshot.getValue(User.class);
                    Glide.with(mContext).load(user.getmAvatar()).crossFade().into(avatar);
                    txtname.setText(user.getmUsername());
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent chatting = new Intent(mContext, ChattingActivity.class);
                            chatting.putExtra(Utils.FR_USER, user);
                            chatting.putExtra(Utils.SENDER_ID, Utils.USER_ID);
                            chatting.putExtra(Utils.RECIPIENT_ID, user.getmId());
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
                String nameSearch = "Phan";
                if (nameSearch.toLowerCase(Locale.getDefault()).contains(charText)) {
                    mListMessage.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }
}
