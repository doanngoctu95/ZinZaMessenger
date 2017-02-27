package chotot.prect.aptech.zinzamessenger.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.adapter.AdapterFriendSearch;
import chotot.prect.aptech.zinzamessenger.adapter.AdapterMessage;
import chotot.prect.aptech.zinzamessenger.model.Friend;
import chotot.prect.aptech.zinzamessenger.model.Message;
import chotot.prect.aptech.zinzamessenger.model.User;
import chotot.prect.aptech.zinzamessenger.utils.Utils;

public class MessageFriendActivity extends AppCompatActivity implements ListView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private long mBackPressed = 0;

    private ListView mListMessage;
    private ListView mLstFriendSearch;
    private List<Message> mList;
    private List<User> mListFriendSearch;
    private AdapterFriendSearch mAdapterFriendSearch;

    private NavigationView mNavigationView;
    private AdapterMessage mAdapterMessage;

    private static final String TAG_NAME = "NAME";
    private static final String TAG_AVATAR = "AVATAR";
    public static final String TAG_MESSAGE = "MESSAGE";
    private FloatingActionButton mFabAddFriend;

    private Dialog mDlAddFriend;
    private AlertDialog.Builder mBuilder;
    private Dialog mDlDetailFriend;
    private ProgressDialog mProgressDialog;

    private TextView mUsername;
    private TextView mEmail;
    private ImageView mImCurrenUser;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private GoogleApiClient mGoogleApiClient;
    private String mProvider;
    private String idCurrentUser;
    private String idFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_friend);
        initControl();
        setAuthInstace();
        loadData();
        loadListview();
        loadUser();
        //Setup implement
        mListMessage.setOnItemClickListener(this);
        mNavigationView.setNavigationItemSelectedListener(this);

    }

    private void initControl(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        mToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        mListMessage = (ListView)findViewById(R.id.lstListmessage);
        mNavigationView = (NavigationView)findViewById(R.id.navigation_view);
        View hView =  mNavigationView.getHeaderView(0);
        mUsername = (TextView)hView.findViewById(R.id.txtUsername);
        mEmail = (TextView)hView.findViewById(R.id.txtEmail);
        mImCurrenUser= (ImageView) hView.findViewById(R.id.imCurrentUser);

        setSupportActionBar(mToolbar);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.draw_open,R.string.draw_close){
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

        mFabAddFriend= (FloatingActionButton) findViewById(R.id.fabAdd);
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
        inflater.inflate(R.menu.menu_messenger,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setQueryHint("Search friend...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_friend_ol:
                Intent frOL = new Intent(MessageFriendActivity.this,FriendOnlineActivity.class);
                startActivity(frOL);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData(){
        mList = new ArrayList<>();
        mList.add(new Message(0,1,2,1,"Hello","13/02"));
        mList.add(new Message(0,1,3,1,"How are you","13/02"));
    }
    private void loadListview(){
        mAdapterMessage = new AdapterMessage(this,R.layout.activity_item_message, mList);
        mListMessage.setAdapter(mAdapterMessage);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent chatting = new Intent(this,ChattingActivity.class);
        Message message = mList.get(position);
        chatting.putExtra(TAG_MESSAGE,message);
        startActivity(chatting);
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
                setUpAlert("Log out","Are you sure to log out ? ");
                break;
        }
        return true;
    }
    private void shareApp(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_EMAIL, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    private void aboutUs(){
        Intent aboutIntent = new Intent(this,AboutActivity.class);
        startActivity(aboutIntent);
    }
    @Override
    public void onClick(View view) {
        int id= view.getId();
        switch (id){
            case R.id.fabAdd:
                showAddDialog();
                break;
        }
    }

    private void showAddDialog() {
        mDlAddFriend= new Dialog(this,android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);;
        mDlAddFriend.setContentView(R.layout.dialog_search_friend);
        mDlAddFriend.show();
        mDlAddFriend.setCancelable(true);
        final EditText edtSearchFr= (EditText) mDlAddFriend.findViewById(R.id.edtSearchNamePhone);
        edtSearchFr.requestFocus();
        Button btnAddFr= (Button) mDlAddFriend.findViewById(R.id.btnSearchFriend);
        btnAddFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress("Search Friend","Searching");
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                searchByUsername(edtSearchFr.getText().toString().trim());

            }
        });
    }
    private void searchByUsername(String username){
        mListFriendSearch = new ArrayList<>();
        mReference = mDatabase.getInstance().getReference();
        mReference.child("users").orderByChild("mUsername").startAt(username)
                .endAt(username+"\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = (String) ds.child("mId").getValue();
                    String url = (String) ds.child("mAvatar").getValue();
                    String name = (String) ds.child("mUsername").getValue();
                    String email = (String) ds.child("mEmail").getValue();
                    User mUser = new User(id,name,email,"",url,"",0,"","");
                    mListFriendSearch.add(mUser);
                }
                mProgressDialog.dismiss();
                if (mListFriendSearch.size()>0) {
                    mDlAddFriend.dismiss();
                    showDetailProfileDialog(mListFriendSearch);
                } else {
                    Utils.showToast("Can't search your friend.Please input another name!!", getApplicationContext());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressDialog.dismiss();
            }
        });

    }
    private void showDetailProfileDialog(List<User> list){
        mDlDetailFriend= new Dialog(this,android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        mDlDetailFriend.setContentView(R.layout.dialog_detail_friend);

        mDlDetailFriend.show();
        mDlDetailFriend.setCancelable(true);
        mLstFriendSearch = (ListView) mDlDetailFriend.findViewById(R.id.lstFriendSearch);
        mAdapterFriendSearch = new AdapterFriendSearch(this,R.layout.item_search_friend,mListFriendSearch,idCurrentUser);
        mLstFriendSearch.setAdapter(mAdapterFriendSearch);

    }
    private void setUpAlert(String title,String message){
        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle(title);
        mBuilder.setMessage(message);

        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();

                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                Intent intent = new Intent(MessageFriendActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
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
    private void setAuthInstace(){
        mAuth = FirebaseAuth.getInstance();
        idCurrentUser=mAuth.getCurrentUser().getUid();
    }
    private void loadUser(){
        String mProvider = getTypeLogIn();
        String email = "";
        String avatarURL = "";
        String displayName = "";
        if(mProvider.equals("facebook.com")){
            email = mAuth.getCurrentUser().getEmail();
            avatarURL = String.valueOf(mAuth.getCurrentUser().getPhotoUrl());
            displayName = mAuth.getCurrentUser().getDisplayName();
            mEmail.setText(email);
            mUsername.setText(displayName);
            Picasso.with(getApplicationContext()).load(avatarURL).into(mImCurrenUser);

        } else {
            mReference = mDatabase.getInstance().getReference();
            final String Uemail = mAuth.getCurrentUser().getEmail();
                mReference.child("users").orderByChild("mEmail").equalTo(Uemail).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String Uname = "";
                        String Uavatar="";
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            Uname = (String) ds.child("mUsername").getValue();
                            Uavatar= (String) ds.child("mAvatar").getValue();
                        }
                        mUsername.setText(Uname);
                        if (!Uavatar.equals("")){
                            Picasso.with(getApplicationContext()).load(Uavatar).into(mImCurrenUser);
                        }
                        mEmail.setText(Uemail);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mBackPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
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
    private String getTypeLogIn(){
        return mAuth.getCurrentUser().getProviders().get(0);
    }

    private void addNewFriend(){
        String idTblFriend="";
        final String tblContact=idCurrentUser+"-"+idFriend;
        idTblFriend= tblContact;
        final Friend friend= new Friend(idTblFriend,idCurrentUser,idFriend,createAt());


        mReference= mDatabase.getInstance().getReference();
        mReference.orderByChild("tblFriend").equalTo(tblContact).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0){
                    Toast.makeText(getApplicationContext(),"no add",Toast.LENGTH_LONG).show();
                }
                else {
                    mReference= mDatabase.getInstance().getReference("tblFriend");
                    mReference.child(tblContact).setValue(friend);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private String createAt(){
        return java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    }

    private boolean checkIdFriendExisted(String tblContact){

        return false;
    }



    private void showProgress(String title, String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
}
