package chotot.prect.aptech.zinzamessenger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import chotot.prect.aptech.zinzamessenger.R;

/**
 * Created by dell on 13/02/2017.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
    }

    private void initView() {
        mImgBack = (ImageView) findViewById(R.id.btnBackSignUp);
        mImgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id= view.getId();
        switch (id){
            case R.id.btnBackSignUp:
                Intent intent2 = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }
}