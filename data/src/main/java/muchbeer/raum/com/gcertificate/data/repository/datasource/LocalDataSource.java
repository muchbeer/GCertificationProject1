package muchbeer.raum.com.gcertificate.data.repository.datasource;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import muchbeer.raum.com.gcertificate.data.db.RoomDb;
import muchbeer.raum.com.gcertificate.data.entities.CryptoCoinEntity;

public class LocalDataSource implements DataSource<List<CryptoCoinEntity>> {

    private final RoomDb mDb;
    private final MutableLiveData<String> mError=new MutableLiveData<>();
    public LocalDataSource(Context mAppContext) {
        mDb= RoomDb.getDatabase(mAppContext);
    }
    @Override
    public LiveData<List<CryptoCoinEntity>> getDataStream() {
        return mDb.coinDao().getAllCoinsLive();
    }

    @Override
    public LiveData<String> getErrorStream() {
        return mError;
    }

    public void writeData(List<CryptoCoinEntity> coins) {
        try {
            mDb.coinDao().insertCoins(coins);
        }catch(Exception e)
        {
            e.printStackTrace();
            mError.postValue(e.getMessage());
        }
    }

    public List<CryptoCoinEntity> getALlCoins() {
        return mDb.coinDao().getAllCoins();
    }
}
