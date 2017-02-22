package chotot.prect.aptech.zinzamessenger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import chotot.prect.aptech.zinzamessenger.adapter.AdapterMessageChat;
import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.model.Message;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class ChattingActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton mBtnBack;
    private ImageView mImgAvatar;
    private TextView mTxtName;

    private AdapterMessageChat mAdapterMessageChat;
    private ListView mListview;
    private List<Message> mMessageList;

    private Button mBtnSendMessage;
    private EmojiconEditText mEdtMessage;
    private EmojIconActions mEmojIcon;
    private ImageView mBtEmoji;
    private View contentRoot;

    private Calendar mCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        initControl();
        mBtnBack.setOnClickListener(this);
        mBtnSendMessage.setOnClickListener(this);
        getExtra();
        loadMessageContent();
        setListview();
    }
    private void initControl(){
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
                sendMessage(message);
                mEdtMessage.setText("");
                break;
        }

    }
    private void setListview() {
       mAdapterMessageChat = new AdapterMessageChat(this,mMessageList);
        mListview.setAdapter(mAdapterMessageChat);
    }
    private void sendMessage(String message){
        mMessageList.add(new Message(mMessageList.size()+1,2,1,1,message,getTimeNow()));
        mAdapterMessageChat.notifyDataSetChanged();
    }
    private void getExtra(){
        Bundle bd = getIntent().getExtras();
        if(bd!= null){
            Intent t= getIntent();
            String urlAvatarRecipient="";
            String nameRecipient ="";
            Message message = (Message)t.getSerializableExtra("MESSAGE");
            if(message.getmIdRecipient()==2){
                urlAvatarRecipient = "http://4.bp.blogspot.com/-8Kef_ymC9t0/U2TgDlHZwiI/AAAAAAAACi8/XzNlxhXtR80/s1600/anh+avatar+dep+4.jpg";
                nameRecipient = "Văn Nam";
            } else if(message.getmIdRecipient()==3){
                nameRecipient = "Nhung";
                urlAvatarRecipient = "http://1.bp.blogspot.com/-U96MqFNsOGA/Uzv6DLtpsHI/AAAAAAAABHs/P7lp0Kc-hYg/s1600/hinh+avatar+dep+6.jpg";
            }
            Picasso.with(this).load(urlAvatarRecipient).into(mImgAvatar);
            mTxtName.setText(nameRecipient);

        }
    }
    private void loadMessageContent() {
        String url = "http://1.bp.blogspot.com/-U96MqFNsOGA/Uzv6DLtpsHI/AAAAAAAABHs/P7lp0Kc-hYg/s1600/hinh+avatar+dep+6.jpg";
        mMessageList = new ArrayList<>();
        String[] content = {"Hello","How are you","I'm fine,thank you","How old are you","16 years old",url};
        Message newMessage;
        for(int j=0;j<content.length;j++){
            if (j % 2 == 0 && j != 5) {
                newMessage = new Message(j,1,2,1,content[j],"12:30");
                newMessage.setRecipientOrSenderStatus(AdapterMessageChat.SENDER);
            } else if(j == 5) {
                newMessage = new Message(j,1,2,3,content[j],"12:30");
                newMessage.setRecipientOrSenderStatus(AdapterMessageChat.SENDER);
            } else {
                newMessage = new Message(j,2,1,1,content[j],"09:41");
                newMessage.setRecipientOrSenderStatus(AdapterMessageChat.RECIPENT);
            }
            mMessageList.add(newMessage);
//            mAdapterMessageChat.refillAdapter(newMessage);
//            mChatRecyclerView.scrollToPosition(mAdapterMessageChat.getItemCount()-1);
        }
    }

    private String getTimeNow(){
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);
        return String.valueOf(hour)+":" + String.valueOf(minute);
    }
}
