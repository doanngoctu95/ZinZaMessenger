package chotot.prect.aptech.zinzamessenger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.model.Message;

public class ChattingActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton btnBack;
    ImageView imgAvatar;
    TextView txtName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        initControl();
        btnBack.setOnClickListener(this);
        getExtra();
    }
    private void initControl(){
        btnBack = (ImageButton)findViewById(R.id.btnBackChatting);
        imgAvatar = (ImageView)findViewById(R.id.imgAvatarFriend);
        txtName = (TextView)findViewById(R.id.txtnameFriendChatting);
    }
    @Override
    public void onClick(View v) {
        if(v==btnBack){
            finish();
        }
    }
    private void getExtra(){
        Bundle bd = getIntent().getExtras();
        if(bd!= null){
            Intent t= getIntent();
            String urlAvatarRecipient="";
            String nameRecipient ="";
            Message message = (Message)t.getSerializableExtra("MESSAGE");
            if(message.getIdRecipient()==2){
                urlAvatarRecipient = "http://4.bp.blogspot.com/-8Kef_ymC9t0/U2TgDlHZwiI/AAAAAAAACi8/XzNlxhXtR80/s1600/anh+avatar+dep+4.jpg";
                nameRecipient = "Văn Nam";
            } else if(message.getIdRecipient()==3){
                nameRecipient = "Nhung";
                urlAvatarRecipient = "http://1.bp.blogspot.com/-U96MqFNsOGA/Uzv6DLtpsHI/AAAAAAAABHs/P7lp0Kc-hYg/s1600/hinh+avatar+dep+6.jpg";
            }
            Picasso.with(this).load(urlAvatarRecipient).into(imgAvatar);
            txtName.setText(nameRecipient);

        }
    }
}
