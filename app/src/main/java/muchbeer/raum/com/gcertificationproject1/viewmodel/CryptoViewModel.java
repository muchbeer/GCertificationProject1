package muchbeer.raum.com.gcertificationproject1.viewmodel;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import muchbeer.raum.com.gcertificationproject1.MainActivity;
import muchbeer.raum.com.gcertificationproject1.entities.CryptoCoinEntity;
import muchbeer.raum.com.gcertificationproject1.recyclerv.CoinModel;
import muchbeer.raum.com.gcertificationproject1.screen.MainScreen;

public class CryptoViewModel extends ViewModel {


    private static final String LOG_TAG = CryptoViewModel.class.getSimpleName();
    ////////////////////////////////////////////////////////////////////////////////////NETWORK RELATED CODE///////////////////////////////////////////////////////////////////////////////////////
    public final String CRYPTO_URL_PATH = "https://files.coinmarketcap.com/static/img/coins/128x128/%s.png";
    public final String ENDPOINT_FETCH_CRYPTO_DATA = "https://api.coinmarketcap.com/v1/ticker/?limit=100";
    private RequestQueue mQueue;
    private final ObjectMapper mObjMapper = new ObjectMapper();

    private MainScreen mView;
    private Context mAppContext;

    private MutableLiveData<List<CoinModel>> mDataApi = new MutableLiveData<>();

    private MutableLiveData<String> mError = new MutableLiveData<>();

    private ExecutorService mExecutor = Executors.newFixedThreadPool(5);

    public LiveData<List<CoinModel>> getCoinsMarketData() {
        return mDataApi;
    }

    public LiveData<String> getErrorUpdates() {
        return mError;
    }

    public LiveData<Double> getTotalMarketCap() {

        return Transformations.map(mDataApi, input -> {
            double totalMarketCap = 0;
            for (int i = 0; i < input.size(); i++) {
                totalMarketCap += input.get(i).marketCap;
            }
            return totalMarketCap;
        });
    }


    public CryptoViewModel() {
        Log.d(LOG_TAG, "NEW VIEWMODEL WAS CREATED");
    }

    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
        if (mQueue == null)
            mQueue = Volley.newRequestQueue(mAppContext);
        fetchData();
    }
    public void bind(MainActivity view) {
        mView= (MainScreen) view;
        mAppContext=view.getApplicationContext();

    }
    public void unbind()
    {
        mView=null;
    }

    @Override
    protected void onCleared() {
        Log.d(LOG_TAG, "onCleared() called");
        super.onCleared();
    }

    @NonNull
    private List<CoinModel> mapEntityToModel(List<CryptoCoinEntity> datum) {
        final ArrayList<CoinModel> listData = new ArrayList<>();
        CryptoCoinEntity entity;
        for (int i = 0; i < datum.size(); i++) {
            entity = datum.get(i);
            listData.add(new CoinModel(entity.getName(), entity.getSymbol(), String.format(CRYPTO_URL_PATH, entity.getId())
                    ,entity.getPriceUsd(),entity.get24hVolumeUsd(), Double.valueOf(entity.getMarketCapUsd())));

                }

        return listData;
    }

    private  class EntityToModelMapperTask extends AsyncTask<List<CryptoCoinEntity>, Void, List<CoinModel>> {
        @Override
        protected List<CoinModel> doInBackground(List<CryptoCoinEntity>... data) {
            final ArrayList<CoinModel> listData = new ArrayList<>();
            CryptoCoinEntity entity;
            for (int i = 0; i < data[0].size(); i++) {
                entity = data[0].get(i);
                Log.d("MainActivity", data.toString());
              /*  listData.add(new CoinModel(entity.getName(), entity.getSymbol(), String.format(CRYPTO_URL_PATH, entity.getId()),
                        entity.getPriceUsd(), entity.get24hVolumeUsd()));
*/
            }

            return listData;
        }

        @Override
        protected void onPostExecute(List<CoinModel> data) {
          /*  mAdapter.setItems(data);
            mSwipeRefreshLayout.setRefreshing(false);*/
            if (mView!=null)
                mView.updateData(data);
        }

    }

    private ArrayList<CryptoCoinEntity> data2;
    private  Response.Listener<JSONArray> mResponseListener = response -> {
        /*writeDataToInternalStorage(response);
        ArrayList<CryptoCoinEntity> data = parseJSON(response.toString());
        data2 = parseJSON(response.toString());
        Log.d(LOG_TAG, "data fetched:" + data2);
        Log.d(LOG_TAG, "data fetched from the link is: " + response.toString());
        new EntityToModelMapperTask().execute(data2);*/

        Log.d(LOG_TAG, "Thread->" +
                Thread.currentThread().getName()+"\tGot some network response");
        writeDataToInternalStorage(response);
        final ArrayList<CryptoCoinEntity> data = parseJSON(response.toString());
        List<CoinModel> mappedData = mapEntityToModel(data);
        mDataApi.setValue(mappedData);
    };

    private  Response.ErrorListener mErrorListener= error -> {
        //showErrorToast(error.toString());
        if (mView!=null)
            mView.setError(error.toString());
        try {
            JSONArray data = readDataFromStorage();
            ArrayList<CryptoCoinEntity> entities = parseJSON(data.toString());
            new EntityToModelMapperTask().execute(entities);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    };
    private JsonArrayRequest mJsonObjReq;

    public void fetchData() {
        final JsonArrayRequest jsonObjReq =
                new JsonArrayRequest(ENDPOINT_FETCH_CRYPTO_DATA,
                        response -> {
                            Log.d(LOG_TAG, "Thread->" +
                                    Thread.currentThread().getName()+"\tGot some network response");
                            writeDataToInternalStorage(response);
                            final ArrayList<CryptoCoinEntity> data = parseJSON(response.toString());
                            List<CoinModel> mappedData = mapEntityToModel(data);
                            mDataApi.setValue(mappedData);
                        },
                        error -> {
                            Log.d(LOG_TAG, "Thread->" +
                                    Thread.currentThread().getName()+"\tGot network error");
                            mError.setValue(error.toString());
                            mExecutor.execute(() -> {
                                try {
                                    Log.d(LOG_TAG, "Thread->"+Thread.currentThread().getName()+
                                            "\tNot fetching from network because of network error - fetching from disk");
                                    JSONArray data = readDataFromStorage();
                                    ArrayList<CryptoCoinEntity> entities = parseJSON(data.toString());
                                    List<CoinModel> mappedData = mapEntityToModel(entities);
                                    mDataApi.postValue(mappedData);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            });


                        });
        // Add the request to the RequestQueue.
        mQueue.add(jsonObjReq);
    }
    public void fetchData2() {
        if (mQueue == null)
            mQueue = Volley.newRequestQueue(mAppContext);
        // Request a string response from the provided URL.
        mJsonObjReq = new JsonArrayRequest(ENDPOINT_FETCH_CRYPTO_DATA,
                mResponseListener,mErrorListener);
        // Add the request to the RequestQueue.
        mQueue.add(mJsonObjReq);
    }
    public ArrayList<CryptoCoinEntity> parseJSON(String jsonStr) {
        ArrayList<CryptoCoinEntity> data = null;

        try {
            data = mObjMapper.readValue(jsonStr, new TypeReference<ArrayList<CryptoCoinEntity>>() {
            });
            Log.d("MainActivity", "The mapper output is: " + data);
        } catch (Exception e) {
            if (mView!=null)
                mView.setError(e.getMessage());
            Log.d("MainActivity", "The error will be "+ e.getMessage());
            e.printStackTrace();
        }
        return data;
    }
    //////////////////////////////////////////////////////////////////////////////////////STORAGE CODE///////////////////////////////////////////////////////////////////////////////////////////
    String DATA_FILE_NAME = "crypto.data";

    private void writeDataToInternalStorage(JSONArray data) {
        FileOutputStream fos = null;
        try {
            fos = mAppContext.openFileOutput(DATA_FILE_NAME, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(data.toString().getBytes());
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private JSONArray readDataFromStorage() throws JSONException {
        FileInputStream fis = null;
        try {
            fis = mAppContext.openFileInput(DATA_FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONArray(sb.toString());
    }
}
