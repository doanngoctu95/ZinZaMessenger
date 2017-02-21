package chotot.prect.aptech.zinzamessenger.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.utils.Utils;

/**
 * Created by dell on 13/02/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnSignUp;
    private Button mBtnLogin;
    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private ProgressDialog mProgressDialog;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setAuthInstance();
    }

    private void initView() {
        mBtnSignUp = (Button) findViewById(R.id.btnSignUp);

        mBtnLogin= (Button) findViewById(R.id.btnLogin);
        mEdtEmail = (EditText) findViewById(R.id.edtEmailLogin);
        mEdtPassword = (EditText) findViewById(R.id.edtPassWrLogin);
//        mEdtUsername.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//        mEdtUsername.setCompoundDrawablePadding(55);

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
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                logIn(getEmail(),getPassword());
                break;
        }
    }

    private String getEmail(){
        return mEdtEmail.getText().toString().trim();
    }
    private String getPassword(){
        return mEdtPassword.getText().toString().trim();
    }
    private boolean validateLogin(){
        if(getEmail().equals("") || getPassword().equals("")){
            return false;
        } else {
            return true;
        }
    }
    private void setAuthInstance(){
        mAuth = FirebaseAuth.getInstance();
    }
    private void logIn(String email,String password){
        showProgress("Login","Logging");
        if(validateLogin()){
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mProgressDialog.dismiss();
                    if(task.isSuccessful()){
                        Utils.showToast("Login Success",getApplicationContext());
                        Intent intent1 = new Intent(LoginActivity.this, MessageFriendActivity.class);
                        startActivity(intent1);
                    } else {
                        Utils.showToast("Login failed",getApplicationContext());
                    }
                }
            });
        } else {
            mProgressDialog.dismiss();
            Utils.showToast("Not enough information to login",this);
        }


    }
    private void showProgress(String title,String message){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(title);
        mProgressDialog.setTitle(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
}
