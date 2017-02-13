package chotot.prect.aptech.zinzamessenger.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import chotot.prect.aptech.zinzamessenger.Adapter.AdapterMessage;
import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.Utils.AndroidUtilities;
import chotot.prect.aptech.zinzamessenger.model.Message;

public class MessageFriendActivity extends AppCompatActivity implements ListView.OnItemClickListener,NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ListView listMessage;
    List<Message> list;
    NavigationView navigationView;
    AdapterMessage adapterMessage;
    private static final String TAG_NAME = "NAME";
    private static final String TAG_AVATAR = "AVATAR";
    public static final String TAG_MESSAGE = "MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_friend);
        initControl();
        loadData();
        loadListview();
        //Setup implement
        listMessage.setOnItemClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void initControl(){
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        toolbar = (Toolbar)findViewById(R.id.mainToolbar);
        listMessage  = (ListView)findViewById(R.id.lstListmessage);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.draw_open,R.string.draw_close){
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
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
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
        list = new ArrayList<>();
        list.add(new Message(0,1,2,1,"Hello",13/02));
        list.add(new Message(0,1,3,1,"How are you",13/02));
    }
    private void loadListview(){
        adapterMessage = new AdapterMessage(this,R.layout.activity_item_message,list);
        listMessage.setAdapter(adapterMessage);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent chatting = new Intent(this,ChattingActivity.class);
        Message message = list.get(position);
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
            case R.id.action_rating:
                AndroidUtilities.showAlert("Rating",this);
                break;
            case R.id.action_about:
                AndroidUtilities.showAlert("About",this);
                break;
            case R.id.action_logout:
                AndroidUtilities.showAlert("Logout",this);
                break;
        }
        return true;
    }
}
