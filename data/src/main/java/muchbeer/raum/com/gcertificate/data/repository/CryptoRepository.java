package muchbeer.raum.com.gcertificate.data.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import muchbeer.raum.com.gcertificate.data.models.CoinModel;

public interface CryptoRepository {

    LiveData<List<CoinModel>> getCryptoCoinsData();
    LiveData<String> getErrorStream();
    LiveData<Double> getTotalMarketCapStream();
    void fetchData();
}
