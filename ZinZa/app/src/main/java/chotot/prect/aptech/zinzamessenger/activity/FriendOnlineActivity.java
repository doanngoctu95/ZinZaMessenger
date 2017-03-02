package chotot.prect.aptech.zinzamessenger.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.adapter.AdapterFriendOnline;
import chotot.prect.aptech.zinzamessenger.model.Friend;
import chotot.prect.aptech.zinzamessenger.model.User;
import chotot.prect.aptech.zinzamessenger.utils.Utils;

public class FriendOnlineActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ListView mListFriend;
    private List<User> mListUser;
    private AdapterFriendOnline mAdapterFriendOnline;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<Friend> mListFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_online);
        initControl();
        setFirebaseInstace();
//        getDataFriend();
//
//        loadListview();
        mListFriends = new ArrayList<>();
        mListUser = new ArrayList<>();

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String mIdCurrentUser = (String)ds.child("mIdCurrentUser").getValue();
                    String mIdFriend = (String)ds.child("mIdFriend").getValue();
                    String mID = (String)ds.child("mId").getValue();
                    String mDateCreated = (String)ds.child("dateCreated").getValue();
                    Friend mFriend = new Friend(mID,mIdCurrentUser,mIdFriend,mDateCreated);
                    mListFriends.add(mFriend);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error load child friend",databaseError.toString());
            }
        });

    }
    private void initControl(){
        mDrawerLayout = (DrawerLayout)findViewById(R.id.olDrawerlayout);
        mToolbar = (Toolbar)findViewById(R.id.toolbarFr);
        mListFriend = (ListView)findViewById(R.id.lstListFriend);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Friend online");
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    private void getDataFriend(){
        mListFriends = new ArrayList<>();
        mListUser = new ArrayList<>();

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String mIdCurrentUser = (String)ds.child("mIdCurrentUser").getValue();
                    String mIdFriend = (String)ds.child("mIdFriend").getValue();
                    String mID = (String)ds.child("mId").getValue();
                    String mDateCreated = (String)ds.child("dateCreated").getValue();
                    Friend mFriend = new Friend(mID,mIdCurrentUser,mIdFriend,mDateCreated);
                    mListFriends.add(mFriend);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error load child friend",databaseError.toString());
            }
        });

//        mListUser.add(new User("1","Văn Cường","cuong@gmail.com","123","http://i.9mobi.vn/cf/images/2015/04/nkk/hinh-avatar-dep-11.jpg","",R.drawable.online,"","01/11/1995"));
//        mListUser.add(new User("2","Văn Nam","cuong@gmail.com","123","http://4.bp.blogspot.com/-8Kef_ymC9t0/U2TgDlHZwiI/AAAAAAAACi8/XzNlxhXtR80/s1600/anh+avatar+dep+4.jpg","",R.drawable.offline,"","01/11/1995"));
//        mListUser.add(new User("3","Nhung","cuong@gmail.com","123","http://1.bp.blogspot.com/-U96MqFNsOGA/Uzv6DLtpsHI/AAAAAAAABHs/P7lp0Kc-hYg/s1600/hinh+avatar+dep+6.jpg","",R.drawable.online,"","01/11/1995"));
    }
    private void loadListview(){
        mAdapterFriendOnline = new AdapterFriendOnline(this,R.layout.item_friend_ol, mListUser);
        mListFriend.setAdapter(mAdapterFriendOnline);
    }
    private void setFirebaseInstace(){
        mReference = mDatabase.getInstance().getReference("tblFriend");
    }
    private void loadData(final String id){
        for(int i=0;i<mListFriends.size();i++){
            String myFriendID = "";
            String currentID = mListFriends.get(i).getmIdCurrentUser();
            String friendID = mListFriends.get(i).getmIdFriend();
            if(currentID.equals(Utils.USER_ID)){
                myFriendID = friendID;
                getUser(myFriendID);
            } else if(friendID.equals(Utils.USER_ID)) {
                myFriendID = currentID;
                getUser(myFriendID);
            } else {

            }
        }
    }
    private void getUser(final String id){
        mReference.child("users").orderByChild("mId").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = "";
                String email = "";
                String password = "";
                String avatar = "";
                String status = "";
                String token = "";
                String dateOfBirth = "";
                String create = "";
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    username = (String)ds.child("mUsername").getValue();
                    email = (String)ds.child("mEmail").getValue();
                    password = (String)ds.child("mPassword").getValue();
                    status = (String)ds.child("mStatus").getValue();
                    token = (String)ds.child("mToken").getValue();
                    dateOfBirth = (String)ds.child("mDateOfBirth").getValue();
                    create = (String)ds.child("mCreatedAt").getValue();
                    int resID;
                    if(Integer.parseInt(status) == 1) {
                        resID = R.drawable.online;
                    } else {
                        resID = R.drawable.offline;
                    }
                    User mUser;
                    if(!email.equals("")){
                        mUser = new User(id,username,email,password,avatar,dateOfBirth,resID,token,create);
                    }else {
                        mUser = new User(id,username,"",password,avatar,dateOfBirth,resID,token,create);
                    }
                    mListUser.add(mUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error load child users",databaseError.toString());
            }
        });

    }

}
