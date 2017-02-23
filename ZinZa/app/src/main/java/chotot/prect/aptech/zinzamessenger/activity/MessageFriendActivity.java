package chotot.prect.aptech.zinzamessenger.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.adapter.AdapterMessage;
import chotot.prect.aptech.zinzamessenger.model.Message;
import chotot.prect.aptech.zinzamessenger.utils.AndroidUtilities;

public class MessageFriendActivity extends AppCompatActivity implements ListView.OnItemClickListener,NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private long mBackPressed = 0;

    private ListView mListMessage;
    private List<Message> mList;
    private NavigationView mNavigationView;
    private AdapterMessage mAdapterMessage;

    private static final String TAG_NAME = "NAME";
    private static final String TAG_AVATAR = "AVATAR";
    public static final String TAG_MESSAGE = "MESSAGE";
    private FloatingActionButton mFabAddFriend;

    private Dialog mDlAddFriend;
    private AlertDialog.Builder mBuilder;

    private TextView mUsername;
    private TextView mEmail;
    private ImageView mImCurrenUser;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private GoogleApiClient mGoogleApiClient;

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
                AndroidUtilities.showAlert("Rating",this);
                break;
            case R.id.action_about:
                AndroidUtilities.showAlert("About",this);
                break;
            case R.id.action_logout:
                setUpAlert("Log out","Are you sure to log out ? ");
                break;
        }
        return true;
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
        mDlAddFriend= new Dialog(this);
        mDlAddFriend.setContentView(R.layout.dialog_search_friend);
        mDlAddFriend.show();
        mDlAddFriend.setCancelable(true);
        final EditText edtSearchFr= (EditText) mDlAddFriend.findViewById(R.id.edtSearchNamePhone);
        edtSearchFr.requestFocus();
        Button btnAddFr= (Button) mDlAddFriend.findViewById(R.id.btnAddContact);
        btnAddFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"add friend "+edtSearchFr.getText(),Toast.LENGTH_LONG).show();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }
    private void setUpAlert(String title,String message){
        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle(title);
        mBuilder.setMessage(message);

        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(MessageFriendActivity.this,LoginActivity.class));
                FirebaseAuth.getInstance().signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                finish();
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
    }
    private void loadUser(){
        List provider=mAuth.getCurrentUser().getProviders();
//        String providers=mAuth.getCurrentUser().getProviderId();
        final String userId = mAuth.getCurrentUser().getUid();
        String email = mAuth.getCurrentUser().getEmail();
        mReference = mDatabase.getInstance().getReference();
        mReference.child("users").orderByChild("mEmail").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = "";
                String avataUrl="";
               for(DataSnapshot ds:dataSnapshot.getChildren()){
                   username = (String) ds.child("mUsername").getValue();
                   avataUrl= (String) ds.child("mAvatar").getValue();
               }
                mUsername.setText(username);

                if (!avataUrl.equals("")){
                    Picasso.with(getApplicationContext()).load(avataUrl).into(mImCurrenUser);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mEmail.setText(email);
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
}
