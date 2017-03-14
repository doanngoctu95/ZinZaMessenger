package vn.com.zinza.zinzamessenger.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    private Button mBtnOpenCamera;
    private Button mBtnOpenGallery;
    private Button mBtnOpenAttach;

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
    private static int REQUEST_CAMERA = 1;
    private static int REQUEST_GALLERY = 2;


    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;

    public static final String MESSAGE_PROGRESS = "message_progress";
    public static final int RESULT_OPEN_ATTACH = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        initControl();
        setFirebaseInstance();
        setFirebaseStorage();
        implementLisenter();

        getExtra();
        loadData();
        setListview();

    }

    private void implementLisenter() {
        mBtnBack.setOnClickListener(this);
        mBtnSendMessage.setOnClickListener(this);
        mBtnOpenCamera.setOnClickListener(this);
        mBtnOpenGallery.setOnClickListener(this);
        mBtnOpenAttach.setOnClickListener(this);
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

        //Extra action
        mBtnOpenCamera = (Button) findViewById(R.id.btnOpenCamera);
        mBtnOpenGallery = (Button) findViewById(R.id.btnOpenGallery);
        mBtnSendMessage = (Button) findViewById(R.id.btnSendMessage);
        mBtnOpenAttach = (Button) findViewById(R.id.btnOpenAttachment);

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
            case R.id.btnOpenCamera:
                openCamera();
                break;
            case R.id.btnOpenGallery:
                openGallery();
                break;
            case R.id.btnOpenAttachment:
                openFileAttach();
                break;

        }

    }


    private void openFileAttach() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, RESULT_OPEN_ATTACH);
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

    private void sendMessageAttach(Uri uriContent, String type) {
        String nameOfFile = Utils.NAME_FILE;
        String mId = mMsRef.push().getKey();
        Message mMessage = new Message(mId, Utils.USER_ID, mIdRecipient, type, uriContent.toString() + "---" + nameOfFile, Utils.createAt());
        mAdapterMessageChat.addMessage(mMessage);
        mMsRef.child(keyConversation).child(mId).setValue(mMessage);
    }

    private void getExtra() {
        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            Intent t = getIntent();
            User user = (User) t.getSerializableExtra(Utils.FR_USER);
            if (!user.getmAvatar().equals("")) {
                Glide.with(this).load(user.getmAvatar()).into(mImgAvatar);
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
                    if (message.getmIdSender().equals(Utils.USER_ID) && message.getmType().equals(Utils.TEXT)) {
                        message.setRecipientOrSenderStatus(AdapterMessageChat.SENDER_TEXT);
                    } else if (message.getmIdSender().equals(Utils.USER_ID) && message.getmType().equals(Utils.IMAGE)) {
                        message.setRecipientOrSenderStatus(AdapterMessageChat.SENDER_IMAGE);
                    } else if (!message.getmIdSender().equals(Utils.USER_ID) && message.getmType().equals(Utils.TEXT)) {
                        message.setRecipientOrSenderStatus(AdapterMessageChat.RECIPENT_TEXT);
                    } else if (!message.getmIdSender().equals(Utils.USER_ID) && message.getmType().equals(Utils.IMAGE)) {
                        message.setRecipientOrSenderStatus(AdapterMessageChat.RECIPENT_IMAGE);
                    }
                    mAdapterMessageChat.addMessage(message);
                    mListview.scrollToPosition(mAdapterMessageChat.getItemCount() - 1);
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

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA);
                }
            }
        } else {
            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intentCamera, REQUEST_CAMERA);
        }

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            uploadData("Send images", data, "images", Utils.IMAGE);
        } else if ((requestCode == REQUEST_GALLERY && resultCode == RESULT_OK)) {
            uploadData("Send images", data, "images", Utils.IMAGE);
        } else if (requestCode == RESULT_OPEN_ATTACH && resultCode == RESULT_OK) {
            uploadData("Send file", data, "files", Utils.FILE);
        }
    }

    private void uploadData(final String title, Intent data, String folder, final String type) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(title);
        mProgressDialog.show();
        final Uri uri = data.getData();
        Utils.NAME_FILE = getNameData(uri);
        StorageReference filePath = mStorageReference.child(keyConversation).child(folder).child(Utils.NAME_FILE);
        Log.e("File path:", filePath + "--- " + uri.getLastPathSegment() + "");
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgressDialog.dismiss();
                Uri url = taskSnapshot.getDownloadUrl();
                sendMessageAttach(url, type);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressDialog.dismiss();
                Utils.showToast(e.toString(), getApplicationContext());
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                //displaying percentage in progress dialog
                mProgressDialog.setMessage(title + " " + ((int) progress) + "%...");
            }
        });
    }

    // get name of file upload
    private String getNameData(Uri uri) {
        String nameFile = "";
        if (uri.toString().startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = this.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    nameFile = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        return nameFile;
    }

    private void setFirebaseInstance() {
        mMsRef = mMsDatabase.getInstance().getReference().child("tblChat");
    }

    private void setFirebaseStorage() {
        mStorageReference = FirebaseStorage.getInstance().getReference();
    }

    private void showProgress(String title, String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(ChattingActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(ChattingActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                REQUEST_CAMERA);
                    }
                });
        alertDialog.show();
    }


}
