package vn.com.zinza.zinzamessenger.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import butterknife.ButterKnife;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import vn.com.zinza.zinzamessenger.R;
import vn.com.zinza.zinzamessenger.adapter.AdapterMessageChat;
import vn.com.zinza.zinzamessenger.model.Message;
import vn.com.zinza.zinzamessenger.model.User;
import vn.com.zinza.zinzamessenger.utils.Utils;

public class ChattingActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton mBtnBack;
    private ImageView mImgAvatar;
    private TextView mTxtName;

    private String mIdRecipient;
    private String mIdSender;
    private String keyConversation;

    private AdapterMessageChat mAdapterMessageChat;
    private RecyclerView mListview;
    private List<Message> mMessageList;

    private Button mBtnSendMessage;
    private EmojiconEditText mEdtMessage;
    private EmojIconActions mEmojIcon;
    private ImageView mBtEmoji;
    private View contentRoot;

    private FirebaseDatabase mMsDatabase;
    private DatabaseReference mMsRef;
    private ChildEventListener messageChatListener;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        bindButterKnife();
        initControl();
        setFirebaseInstance();
        mBtnBack.setOnClickListener(this);
        mBtnSendMessage.setOnClickListener(this);
        getExtra();
//        showProgress("Loading message..", "Please wait");
        loadData();
        setListview();
//        mProgressDialog.dismiss();
    }
    private void bindButterKnife() {
        ButterKnife.bind(this);
    }
    private void initControl() {
        mMessageList = new ArrayList<>();
        contentRoot = findViewById(R.id.activity_chatting);
        mBtnBack = (ImageButton) findViewById(R.id.btnBackChatting);

        mImgAvatar = (ImageView) findViewById(R.id.imgAvatarFriend);
        mTxtName = (TextView) findViewById(R.id.txtnameFriendChatting);

        mBtnSendMessage = (Button) findViewById(R.id.btnSendMessage);
        mListview = (RecyclerView) findViewById(R.id.list_content_message);
        mBtEmoji = (ImageView) findViewById(R.id.btnEmotion);
        mEdtMessage = (EmojiconEditText) findViewById(R.id.edtMessageInput);
        mEmojIcon = new EmojIconActions(this, contentRoot, mEdtMessage, mBtEmoji);
        mEmojIcon.ShowEmojIcon();
        mBtEmoji.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnEmotion:
                mEmojIcon.ShowEmojIcon();
                break;
            case R.id.btnBackChatting:
                finish();
                break;
            case R.id.btnSendMessage:
                String message = mEdtMessage.getText().toString();
                if (message.equals("")) {

                } else {
                    sendMessage(message);
                    mEdtMessage.setText("");
                }

                break;
        }

    }

    private void setListview() {
        mListview.setLayoutManager(new LinearLayoutManager(this));
        mListview.setHasFixedSize(true);
//        ((SimpleItemAnimator) mListview.getItemAnimator()).setSupportsChangeAnimations(false);
        mAdapterMessageChat = new AdapterMessageChat(this, mMessageList);
        mListview.setAdapter(mAdapterMessageChat);
    }

    private void sendMessage(String message) {

        String mId = mMsRef.push().getKey();
        Message mMessage = new Message(mId, Utils.USER_ID, mIdRecipient, Utils.TEXT, message, Utils.createAt());
        mAdapterMessageChat.addMessage(mMessage);
        mMsRef.child(keyConversation).child(mId).setValue(mMessage);
    }

    private void getExtra() {
        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            Intent t = getIntent();
            User user = (User) t.getSerializableExtra(Utils.FR_USER);
            if (!user.getmAvatar().equals("")) {
                Picasso.with(this).load(user.getmAvatar()).into(mImgAvatar);
            }
            mTxtName.setText(user.getmUsername());
            mIdRecipient = t.getStringExtra(Utils.RECIPIENT_ID);
            mIdSender = t.getStringExtra(Utils.SENDER_ID);

        }
    }

    private void getMessage() {
        mAdapterMessageChat.cleanUp();
        mMsRef.child(keyConversation).limitToLast(100).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Load message here
                if (dataSnapshot.exists()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (message.getmIdSender().equals(Utils.USER_ID)) {
                        message.setRecipientOrSenderStatus(AdapterMessageChat.SENDER);
                    } else {
                        message.setRecipientOrSenderStatus(AdapterMessageChat.RECIPENT);
                    }
                    mAdapterMessageChat.addMessage(message);
                    mListview.scrollToPosition(mAdapterMessageChat.getItemCount()-1);
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

    private void loadData() {

        final String kcv1 = mIdSender + "-" + mIdRecipient;
        final String kcv2 = mIdRecipient + "-" + mIdSender;

        mMsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(kcv1).exists()) {
                    keyConversation = kcv1;
                } else {
                    keyConversation = kcv2;
                }
                getMessage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void setFirebaseInstance() {
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