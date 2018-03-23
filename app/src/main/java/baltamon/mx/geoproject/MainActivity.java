package baltamon.mx.geoproject;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupRealm();

        setupToolbar();
        setupRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tb_empty_list:
                Toast.makeText(this, "Empty list", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null)
            mActionBar.setTitle("Geo Project");
    }

    private void setupRealm(){
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("geoprojectdb.realm").build();
        mRealm = Realm.getInstance(realmConfiguration);
    }

    public void setupRecyclerView() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 1; i <= 10; i++)
            arrayList.add(i + " Number of address");

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AddressesRecyclerAdapter mAdapter = new AddressesRecyclerAdapter(arrayList);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }
}
