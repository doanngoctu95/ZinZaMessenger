package chotot.prect.aptech.zinzamessenger.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import chotot.prect.aptech.zinzamessenger.Adapter.AdapterFriendOnline;
import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.model.User;

public class FriendOnlineActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ListView mListFriend;
    private List<User> mListUser;
    private AdapterFriendOnline mAdapterFriendOnline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_online);
        initControl();
        loadData();
        loadListview();

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
    private void loadData(){
        mListUser = new ArrayList<>();
        mListUser.add(new User(1,"Văn Cường","cuong@gmail.com","123","http://i.9mobi.vn/cf/images/2015/04/nkk/hinh-avatar-dep-11.jpg",0,R.drawable.online,"",01/11/1995));
        mListUser.add(new User(2,"Văn Nam","cuong@gmail.com","123","http://4.bp.blogspot.com/-8Kef_ymC9t0/U2TgDlHZwiI/AAAAAAAACi8/XzNlxhXtR80/s1600/anh+avatar+dep+4.jpg",0,R.drawable.offline,"",01/11/1995));
        mListUser.add(new User(3,"Nhung","cuong@gmail.com","123","http://1.bp.blogspot.com/-U96MqFNsOGA/Uzv6DLtpsHI/AAAAAAAABHs/P7lp0Kc-hYg/s1600/hinh+avatar+dep+6.jpg",0,R.drawable.online,"",01/11/1995));
    }
    private void loadListview(){
        mAdapterFriendOnline = new AdapterFriendOnline(this,R.layout.item_friend_ol, mListUser);
        mListFriend.setAdapter(mAdapterFriendOnline);
    }
}
