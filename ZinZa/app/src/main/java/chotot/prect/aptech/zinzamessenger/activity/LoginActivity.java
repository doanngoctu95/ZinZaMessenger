package chotot.prect.aptech.zinzamessenger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import chotot.prect.aptech.zinzamessenger.R;

/**
 * Created by dell on 13/02/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnSignUp;
    private Button mBtnLogin;
    private EditText mEdtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mBtnSignUp = (Button) findViewById(R.id.btnSignUp);

        mBtnLogin= (Button) findViewById(R.id.btnLogin);
        mEdtUsername= (EditText) findViewById(R.id.edtEmailLogin);
        mEdtUsername.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mEdtUsername.setCompoundDrawablePadding(55);


        mBtnSignUp.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id= view.getId();
        switch (id){
            case R.id.btnSignUp:
                Intent intent2 = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent2);
                break;
            case R.id.btnLogin:
                Intent intent1 = new Intent(LoginActivity.this, MessageFriendActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
