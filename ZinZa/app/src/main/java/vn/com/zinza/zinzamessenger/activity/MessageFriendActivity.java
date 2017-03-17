package vn.com.zinza.zinzamessenger.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.com.zinza.zinzamessenger.R;
import vn.com.zinza.zinzamessenger.adapter.AdapterFriendSearch;
import vn.com.zinza.zinzamessenger.adapter.AdapterMessage;
import vn.com.zinza.zinzamessenger.model.Message;
import vn.com.zinza.zinzamessenger.model.User;
import vn.com.zinza.zinzamessenger.utils.Helper;
import vn.com.zinza.zinzamessenger.utils.Utils;

public class MessageFriendActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private long mBackPressed = 0;

    private ListView mListMessage;
    private List<Message> mList;
    private AdapterMessage mAdapterMessage;
    private List<String> mListMessageKeys;
    private List<String> mListConversationKeys;


    private ListView mLstFriendSearch;
    private List<User> mListFriendSearch;
    private AdapterFriendSearch mAdapterFriendSearch;

    private List<String> mListUserKey;
    private List<User> mListUser;
    private NavigationView mNavigationView;


    private static final String TAG_NAME = "NAME";
    private static final String TAG_AVATAR = "AVATAR";
    public static final String TAG_MESSAGE = "MESSAGE";
    private FloatingActionButton mFabAddFriend;

    private Dialog mDlAddFriend;
    private AlertDialog.Builder mBuilder;
    private Dialog mDlDetailFriend;
    private ProgressDialog mProgressDialog;
    private List<String> mListSearchFrKeys;

    private TextView mUsername;
    private TextView mEmail;
    private ImageView mImCurrenUser;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private DatabaseReference mRefMessage;

    private GoogleApiClient mGoogleApiClient;
    private String mProvider;
    private User mUser;
    private String idFriend;

    private boolean checkListResult = false;
    private boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_friend);
        Helper.createDirectory();
        initControl();
        setAuthInstace();
        showProgress("Loading...", "Please wait...");
        loadConversation();
        loadUser();
        loadListview();
        mNavigationView.setNavigationItemSelectedListener(this);

    }

    private void initControl() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        mListMessage = (ListView) findViewById(R.id.lstListmessage);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        View hView = mNavigationView.getHeaderView(0);
        mUsername = (TextView) hView.findViewById(R.id.txtUsername);
        mEmail = (TextView) hView.findViewById(R.id.txtEmail);
        mImCurrenUser = (ImageView) hView.findViewById(R.id.imCurrentUser);

        setSupportActionBar(mToolbar);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.draw_open, R.string.draw_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        mFabAddFriend = (FloatingActionButton) findViewById(R.id.fabAdd);
        mFabAddFriend.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_messenger, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search friend...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_friend_ol:
                Intent frOL = new Intent(MessageFriendActivity.this, FriendOnlineActivity.class);
                startActivity(frOL);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void getMessageFromConversation(final String keyConversation) {
        DatabaseReference mConverRef = mDatabase.getInstance().getReference().child("tblChat").child(keyConversation);
        mConverRef.limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Message message = ds.getValue(Message.class);
                    if (message.getmType().equals(Utils.IMAGE)) {
                        message.setmContent("\uD83C\uDFDD Image");
                    } else if(message.getmType().equals(Utils.FILE)) {
                        message.setmContent("\uD83D\uDCDA Attachment");
                    }else if(message.getmType().equals(Utils.VIDEO)){
                        message.setmContent("\uD83C\uDF9E video.mp4");
                    }
                    if (isMyConvesation(keyConversation)) {
                        mAdapterMessage.refill(message);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mAdapterMessage.isNewMessage(false);
    }

    private boolean isMyConvesation(String key) {
        String[] parts = key.split("-");
        String uId1 = parts[0];
        String uId2 = parts[1];

        if (Utils.USER_ID.equals(uId1)) {
            return true;
        } else if (Utils.USER_ID.equals(uId2)) {
            return true;
        } else {
            return false;
        }

    }

    private void loadConversation() {
        mListUserKey = new ArrayList<>();
        mListMessageKeys = new ArrayList<>();
        mListConversationKeys = new ArrayList<>();
        mList = new ArrayList<>();
        mRefMessage = mDatabase.getInstance().getReference().child("tblChat");

        mRefMessage.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    String key = dataSnapshot.getKey();
                    mListConversationKeys.add(key);
                    getMessageFromConversation(key);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRefMessage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadListview() {
        mAdapterMessage = new AdapterMessage(this, R.layout.activity_item_message, mList);
        mListMessage.setAdapter(mAdapterMessage);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                Intent setting = new Intent(MessageFriendActivity.this, SettingActivity.class);
                startActivity(setting);
                break;
            case R.id.action_history_file:
                Intent history_file = new Intent(MessageFriendActivity.this, HistoryFileActivity.class);
                startActivity(history_file);
                break;
            case R.id.action_rating:
                shareApp();
                break;
            case R.id.action_about:
                aboutUs();
                break;
            case R.id.action_logout:
                setUpAlert("Log out", "Are you sure to log out ? ");

                break;
        }
        return true;
    }

    private void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_EMAIL, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void aboutUs() {
        Intent aboutIntent = new Intent(this, AboutActivity.class);
        startActivity(aboutIntent);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fabAdd:
                showAddDialog();
                break;
        }
    }

    private void showAddDialog() {
        mDlAddFriend = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);

        mDlAddFriend.setContentView(R.layout.dialog_search_friend);
        mDlAddFriend.show();
        mDlAddFriend.setCancelable(true);
        final EditText edtSearchFr = (EditText) mDlAddFriend.findViewById(R.id.edtSearchNamePhone);
        edtSearchFr.requestFocus();
        Button btnAddFr = (Button) mDlAddFriend.findViewById(R.id.btnSearchFriend);
        btnAddFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                String input = edtSearchFr.getText().toString().trim();
                if(input.equals("")){
                    edtSearchFr.setError("Please input name ");
                } else {
                    searchByUsername(edtSearchFr.getText().toString().trim());
                    showDetailProfileDialog(mListFriendSearch);
                }

            }
        });


    }

    private void searchByUsername(final String username) {

        mListFriendSearch = new ArrayList<>();
        mListSearchFrKeys = new ArrayList<>();
        mReference = mDatabase.getInstance().getReference();
        mReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    String key = dataSnapshot.getKey();
                    User user = dataSnapshot.getValue(User.class);
                    mListSearchFrKeys.add(key);
                        if (user.getmUsername().toLowerCase().contains(username.toLowerCase())) {
                            if(!user.getmUsername().equals(Utils.USER_NAME)){
                                mAdapterFriendSearch.refill(user);
                                checkListResult = true;
                            }

                        }

                }


                //So sanh vs username xem co trung hay k

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

    private void showDetailProfileDialog(List<User> list) {

        mDlDetailFriend = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        mDlDetailFriend.setContentView(R.layout.dialog_detail_friend);

        mDlDetailFriend.show();
        mDlAddFriend.dismiss();
        mDlDetailFriend.setCancelable(true);

        mLstFriendSearch = (ListView) mDlDetailFriend.findViewById(R.id.lstFriendSearch);
        mAdapterFriendSearch = new AdapterFriendSearch(this, R.layout.item_search_friend, list, mUser);
        mLstFriendSearch.setAdapter(mAdapterFriendSearch);

    }

    private void setUpAlert(String title, String message) {
        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle(title);
        mBuilder.setMessage(message);

        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
//                LoginManager.getInstance().logOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);

                Helper.setUserOffline(mReference);
                Intent intent = new Intent(MessageFriendActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void setAuthInstace() {
        mAuth = FirebaseAuth.getInstance();

        final String id = Utils.USER_ID = mAuth.getCurrentUser().getUid();
        final String photoUrl = Utils.AVATAR_URL = String.valueOf(mAuth.getCurrentUser().getPhotoUrl());
        mReference = mDatabase.getInstance().getReference();
        final String Uemail = mAuth.getCurrentUser().getEmail();
        mReference.child("users").orderByChild("mEmail").equalTo(Uemail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Uname = "";
                String Uavatar = "";
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Uname = (String) ds.child("mUsername").getValue();
                    Uavatar = (String) ds.child("mAvatar").getValue();
                }
                mUser = new User(id, Uname, "", "", Uavatar, "", "on", Utils.getToken(), "");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void loadUser() {
        String mProvider = getTypeLogIn();
        String email = "";
        String avatarURL = "";
        String displayName = "";
        if (mProvider.equals("facebook.com")) {
            email = mAuth.getCurrentUser().getEmail();
            avatarURL = String.valueOf(mAuth.getCurrentUser().getPhotoUrl());
            displayName = mAuth.getCurrentUser().getDisplayName();
            mEmail.setText(email);
            mUsername.setText(displayName);
            Utils.USER_NAME = displayName;
            Glide.with(getApplicationContext()).load(avatarURL).into(mImCurrenUser);

        } else {
            mReference = mDatabase.getInstance().getReference();
            final String Uemail = mAuth.getCurrentUser().getEmail();
            mReference.child("users").orderByChild("mEmail").equalTo(Uemail).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String Uname = "";
                    String Uavatar = "";
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Uname = (String) ds.child("mUsername").getValue();
                        Uavatar = (String) ds.child("mAvatar").getValue();
                    }
                    mUsername.setText(Uname);
                    if (!Uavatar.equals("")) {
                        Glide.with(getApplicationContext()).load(Uavatar).crossFade().into(mImCurrenUser);
                    }
                    mEmail.setText(Uemail);
                    Utils.USER_NAME = Uname;

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private String getTypeLogIn() {
        return mAuth.getCurrentUser().getProviders().get(0);
    }

    private void showProgress(String title, String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
}
