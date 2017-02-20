package chotot.prect.aptech.zinzamessenger.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import chotot.prect.aptech.zinzamessenger.adapter.AdapterHistoryFile;
import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.model.FileHistory;

/**
 * Created by dell on 17/02/2017.
 */

public class HistoryFileActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Toolbar mToolbarHis;
    private ListView lvListHistory;
    private AdapterHistoryFile adapterHistoryFile;
    private ArrayList<FileHistory> listFileHis;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history_file);
        initViews();

    }

    private void initViews() {
        mToolbarHis= (Toolbar) findViewById(R.id.historyToolbar);
        setSupportActionBar(mToolbarHis);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        lvListHistory= (ListView) findViewById(R.id.lvListFileHistory);
        loadData();
        adapterHistoryFile= new AdapterHistoryFile(this,android.R.layout.simple_list_item_1,listFileHis);
        lvListHistory.setAdapter(adapterHistoryFile);
        lvListHistory.setOnItemClickListener(this);
    }

    //load data file.
    private void loadData() {
        listFileHis= new ArrayList<>();
        FileHistory f1= new FileHistory(R.drawable.word_icon,"Amazing animal","17/02/2017");
        listFileHis.add(f1);
        FileHistory f2= new FileHistory(R.drawable.exel_icon,"Report today","17/02/2017");
        listFileHis.add(f2);
        FileHistory f3= new FileHistory(R.drawable.ppt_icon,"Slide architecture computer","14/02/2017");
        listFileHis.add(f3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_history_file,menu);
        MenuItem item = menu.findItem(R.id.action_search_history);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setQueryHint("Search file...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    adapterHistoryFile.filter("");
                    lvListHistory.clearTextFilter();
                } else {
                    adapterHistoryFile.filter(newText);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
