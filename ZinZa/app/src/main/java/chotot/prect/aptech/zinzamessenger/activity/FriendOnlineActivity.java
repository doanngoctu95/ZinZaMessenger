package chotot.prect.aptech.zinzamessenger.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private List<String> mListFrStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_online);
        initControl();
        setFirebaseInstace();

        mListFriends = new ArrayList<>();
        mListUser = new ArrayList<>();
        mListFrStr = new ArrayList<>();
        getListFr();
        loadListview();
    }

    private void initControl() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.olDrawerlayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbarFr);
        mListFriend = (ListView) findViewById(R.id.lstListFriend);
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

    private void loadListview() {
        mAdapterFriendOnline = new AdapterFriendOnline(this, R.layout.item_friend_ol, mListUser);
        mListFriend.setAdapter(mAdapterFriendOnline);
    }

    private void setFirebaseInstace() {
        mReference = mDatabase.getInstance().getReference();
    }

    private void loadFr(Friend friend){
        String myFriendID = "";
        String currentID =friend.getmIdCurrentUser();
        String friendID = friend.getmIdFriend();
        if (currentID.equals(Utils.USER_ID)) {
            myFriendID = friendID;
            getListUser(myFriendID);
        } else if (friendID.equals(Utils.USER_ID)) {
            myFriendID = currentID;
            getListUser(myFriendID);
        } else {

        }
    }

    // get list friends
    private void getListFr() {
        mReference.child("tblFriend").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    mListFriends.add(friend);

                    loadFr(friend);

                } else {

                }
//                loadData();
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

    //get list fr online
    private void getListUser(String id) {
        mReference.child("users").orderByChild("mId").equalTo(id).addChildEventListener(new ChildEventListener() {//
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                //  mListUser.add(user);
                    mListFrStr.add(user.getmId());
                    mAdapterFriendOnline.refill(user);
                } else {

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    String id = (String) dataSnapshot.child("mId").getValue();
                    User user = dataSnapshot.getValue(User.class);
                    int index = mListFrStr.indexOf(id);
                    if (index > -1) {
                        mAdapterFriendOnline.changeStatusUser(index, user);
                    }

                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String id = (String) dataSnapshot.child("mId").getValue();

                    int index = mListFrStr.indexOf(id);
                    if (index > -1) {
                        mAdapterFriendOnline.removeUser(index);
                    }

                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
//        mListUser.clear();
//        mListFriends.clear();
    }
}
