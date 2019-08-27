package muchbeer.raum.com.gcertificationproject1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import muchbeer.raum.com.gcertificationproject1.entities.CryptoCoinEntity;
import muchbeer.raum.com.gcertificationproject1.fragment.UILessFragment;
import muchbeer.raum.com.gcertificationproject1.recyclerv.CoinModel;
import muchbeer.raum.com.gcertificationproject1.recyclerv.Divider;
import muchbeer.raum.com.gcertificationproject1.recyclerv.MyCryptoAdapter;
import muchbeer.raum.com.gcertificationproject1.screen.MainScreen;
import muchbeer.raum.com.gcertificationproject1.viewmodel.CryptoViewModel;

public class MainActivity extends AppCompatActivity implements MainScreen {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recView;
    private MyCryptoAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CryptoViewModel mViewModel;
    private long mLastFetchedDataTimeStamp;


    private final Observer<List<CoinModel>> dataObserver = coinModels -> updateData(coinModels);

    private final Observer<String> errorObserver = errorMsg -> setError(errorMsg);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
       // fetchData();
       /* mViewModel=new CryptoViewModel();
        mViewModel.bind(this);
        mViewModel.fetchData();*/

        mViewModel= ViewModelProviders.of(this).get(CryptoViewModel.class);
        mViewModel.setAppContext(getApplicationContext());

        mViewModel.getCoinsMarketData().observe(this, dataObserver);

        mViewModel.getErrorUpdates().observe(this, errorObserver);

        getSupportFragmentManager().beginTransaction()
                .add(new UILessFragment(),"UILessFragment").commit();
    }


    @Override
    protected void onDestroy() {
        //mViewModel.unbind();
        Log.d(TAG, "BEFORE super.onDestroy() called");
        super.onDestroy();
        Log.d(TAG, "AFTER super.onDestroy() called");
    }
    private final static int DATA_FETCHING_INTERVAL=10*1000; //10 seconds
    private void bindViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        recView = findViewById(R.id.rec_View);
        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
           // fetchData();
           // mViewModel.fetchData();

            if (System.currentTimeMillis() - mLastFetchedDataTimeStamp < DATA_FETCHING_INTERVAL) {
                Log.d(TAG, "\tNot fetching from network because interval didn't reach");
                mSwipeRefreshLayout.setRefreshing(false);
                return;
            }
            mViewModel.fetchData();
        });
        mAdapter = new MyCryptoAdapter();
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recView.setLayoutManager(lm);
        recView.setAdapter(mAdapter);
        recView.addItemDecoration(new Divider(this));
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> recView.smoothScrollToPosition(0));
    }

    private void showErrorToast(String error) {
        Toast.makeText(this, "Error:" + error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void updateData(List<CoinModel> data) {
       /* mAdapter.setItems(data);
        mSwipeRefreshLayout.setRefreshing(false);*/

        mLastFetchedDataTimeStamp=System.currentTimeMillis();
        mAdapter.setItems(data);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setError(String msg) {
        showErrorToast(msg);
    }
}
