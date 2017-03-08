package chotot.prect.aptech.zinzamessenger.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.adapter.AdapterMessageChat;
import chotot.prect.aptech.zinzamessenger.model.Message;
import chotot.prect.aptech.zinzamessenger.model.User;
import chotot.prect.aptech.zinzamessenger.utils.Utils;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class ChattingActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton mBtnBack;
    private ImageView mImgAvatar;
    private TextView mTxtName;

    private String mIdRecipient;
    private String mIdSender;
    private String keyConversation;

    private AdapterMessageChat mAdapterMessageChat;
    private ListView mListview;
    private List<Message> mMessageList;

    private Button mBtnSendMessage;
    private EmojiconEditText mEdtMessage;
    private EmojIconActions mEmojIcon;
    private ImageView mBtEmoji;
    private View contentRoot;

    private FirebaseDatabase mMsDatabase;
    private DatabaseReference mMsRef;

    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        initControl();
        setFirebaseInstance();
        mBtnBack.setOnClickListener(this);
        mBtnSendMessage.setOnClickListener(this);
        getExtra();
        showProgress("Loading message..","Please wait");
        loadData();
        setListview();
        mProgressDialog.dismiss();
    }
    private void initControl(){
        mMessageList = new ArrayList<>();
        contentRoot = findViewById(R.id.activity_chatting);
        mBtnBack = (ImageButton)findViewById(R.id.btnBackChatting);

        mImgAvatar = (ImageView)findViewById(R.id.imgAvatarFriend);
        mTxtName = (TextView)findViewById(R.id.txtnameFriendChatting);

        mBtnSendMessage = (Button)findViewById(R.id.btnSendMessage);
        mListview = (ListView) findViewById(R.id.list_content_message);
        mBtEmoji = (ImageView)findViewById(R.id.btnEmotion);
        mEdtMessage = (EmojiconEditText)findViewById(R.id.edtMessageInput);
        mEmojIcon = new EmojIconActions(this,contentRoot,mEdtMessage,mBtEmoji);
        mEmojIcon.ShowEmojIcon();
        mBtEmoji.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id= v.getId();
        switch (id){
            case R.id.btnEmotion:
                mEmojIcon.ShowEmojIcon();
                break;
            case R.id.btnBackChatting:
                finish();
                break;
            case R.id.btnSendMessage:
                String message = mEdtMessage.getText().toString();
                if(message.equals("")){

                } else {
                    sendMessage(message);
                    mEdtMessage.setText("");
                }

                break;
        }

    }
    private void setListview() {
       mAdapterMessageChat = new AdapterMessageChat(this,mMessageList);
        mListview.setAdapter(mAdapterMessageChat);
    }
    private void sendMessage(String message){

        String mId = mMsRef.push().getKey();
        Message mMessage = new Message(mId,Utils.USER_ID,mIdRecipient,Utils.TEXT,message,Utils.createAt());
        mAdapterMessageChat.addMessage(mMessage);
        mMsRef.child(keyConversation).child(mId).setValue(mMessage);
    }
    private void getExtra(){
        Bundle bd = getIntent().getExtras();
        if(bd!= null){
            Intent t= getIntent();
            User user = (User)t.getSerializableExtra(Utils.FR_USER);
            if(!user.getmAvatar().equals("")){
                Picasso.with(this).load(user.getmAvatar()).into(mImgAvatar);
            }
            mTxtName.setText(user.getmUsername());
            mIdRecipient = t.getStringExtra(Utils.RECIPIENT_ID);
            mIdSender = t.getStringExtra(Utils.SENDER_ID);

        }
    }
    private void getMessage() {
        mAdapterMessageChat.cleanUp();
        mMsRef.child(keyConversation).orderByChild("mTime").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Load message here
                if(dataSnapshot.exists()){
                    Message message = dataSnapshot.getValue(Message.class);
                    if(message.getmIdSender().equals(Utils.USER_ID)){
                        message.setRecipientOrSenderStatus(AdapterMessageChat.SENDER);
                    } else {
                        message.setRecipientOrSenderStatus(AdapterMessageChat.RECIPENT);
                    }
                    mAdapterMessageChat.addMessage(message);
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
    private void loadData(){

        final String kcv1 = mIdSender+"-"+mIdRecipient;
        final String kcv2 = mIdRecipient+"-"+mIdSender;

        mMsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(kcv1).exists()){
                    keyConversation = kcv1;
                }else {
                    keyConversation = kcv2;
                }
                getMessage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setFirebaseInstance(){
        mMsRef = mMsDatabase.getInstance().getReference().child("tblChat");
    }
    private void showProgress(String title, String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
}
