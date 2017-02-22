package chotot.prect.aptech.zinzamessenger.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.model.User;
import chotot.prect.aptech.zinzamessenger.utils.Utils;

/**
 * Created by dell on 13/02/2017.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView mImgBack;
    private EditText mEdtUsername;
    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private EditText mEdtConfirm;
    private EditText mEdtDOB;
    private Button mBtnSignup;

    private FirebaseAuth mAuth;
    private AlertDialog mDialog;
    private ProgressDialog mProgressDialog;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private Calendar datetime = Calendar.getInstance();
    private DateFormat mDateFormat = DateFormat.getDateInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setAuthInstance();
        setDbReference();
        initView();
        showDiaglogPicker();
    }

    private void initView() {
        mImgBack = (ImageView) findViewById(R.id.btnBackSignUp);
        mImgBack.setOnClickListener(this);
        mBtnSignup = (Button)findViewById(R.id.btnSignUp);
        mBtnSignup.setOnClickListener(this);
        mEdtUsername = (EditText)findViewById(R.id.edtUsernameSign);
        mEdtEmail = (EditText)findViewById(R.id.edtEmailSign);
        mEdtPassword = (EditText)findViewById(R.id.edtPassWrLogin);
        mEdtConfirm = (EditText)findViewById(R.id.edtConfirm);
        mEdtDOB = (EditText)findViewById(R.id.edtDob);
    }

    private void showDiaglogPicker(){
        mEdtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignUpActivity.this,d,datetime.get(Calendar.YEAR),datetime.get(Calendar.MONTH),datetime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            datetime.set(Calendar.YEAR,year);
            datetime.set(Calendar.MONTH,month);
            datetime.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            mEdtDOB.setText(mDateFormat.format(datetime.getTime()));
        }
    };

    private void logIn(){
        String id = mReference.push().getKey();
        String username = mEdtUsername.getText().toString().trim();
        String email = mEdtEmail.getText().toString().trim();
        String password = mEdtPassword.getText().toString().trim();
        String confirm = mEdtConfirm.getText().toString().trim();
        String dateOfBirth = mEdtDOB.getText().toString().trim();
        if(username.equals("") || email.equals("") || dateOfBirth.equals("") || password.equals("") || confirm.equals("")){
            Utils.showToast("Not enough information",this);
        }else if(Utils.isValidEmail(email)== false){
            Utils.showToast("Email is uncorrect",this);
        } else if(isCorrectPassword(password,confirm) == false){
            Utils.showToast("Password is uncorrect",this);
        } else {
            showProgress("Sign in","Registering");
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                    }else {
                        showAlertDialog(task.getException().getMessage(),true);
                    }
                }
            });

            //Register to database

            Utils.showToast("Register success",this);
            goToMainActivity();
            mProgressDialog.dismiss();
        }

    }
    private void showAlertDialog(String message, boolean isCancelable){
        mDialog = Utils.buildAlertDialog(getString(R.string.title_alert), message,isCancelable,SignUpActivity.this);
        mDialog.show();
    }
    private void dismissAlertDialog(){
        mDialog.dismiss();
    }
    private void setDbReference(){
        mReference = mDatabase.getInstance().getReference("users");
    }
    private void setAuthInstance(){
        mAuth = FirebaseAuth.getInstance();
    }
    private void goToMainActivity(){
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
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
            case R.id.btnSignUp:
                logIn();
                break;
        }
    }

    private String createAt(){
        return java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    }
    private boolean isCorrectPassword(String pass,String confirm){
        if(pass.equals(confirm)){
            return true;
        } else {
            return false;
        }
    }
    private void showProgress(String title, String message){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

}