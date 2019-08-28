package muchbeer.raum.com.gcertificationproject1.viewmodel;

import android.app.Application;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import muchbeer.raum.com.gcertificate.data.models.CoinModel;
import muchbeer.raum.com.gcertificate.data.repository.CryptoRepository;
import muchbeer.raum.com.gcertificate.data.repository.CryptoRepositoryImpl;


public class CryptoViewModel extends AndroidViewModel {

    private static final String LOG_TAG = CryptoViewModel.class.getSimpleName();

    private static final String TAG = CryptoViewModel.class.getSimpleName();
    private CryptoRepository mCryptoRepository;
    public LiveData<List<CoinModel>> getCoinsMarketData() {
        return mCryptoRepository.getCryptoCoinsData();
    }
    public LiveData<String> getErrorUpdates() {
        return mCryptoRepository.getErrorStream();
    }

    public CryptoViewModel(@NonNull Application application) {
        super(application);
        mCryptoRepository= CryptoRepositoryImpl.create(application);
    }


    @Override
    protected void onCleared() {
        Log.d(LOG_TAG, "onCleared() called");
        super.onCleared();
    }

    public void fetchData() {
        mCryptoRepository.fetchData();
    }

    public LiveData<Double>getTotalMarketCap()
    {
        return mCryptoRepository.getTotalMarketCapStream();
    }
}
