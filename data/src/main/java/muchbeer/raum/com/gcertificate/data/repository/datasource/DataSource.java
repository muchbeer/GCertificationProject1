package muchbeer.raum.com.gcertificate.data.repository.datasource;

import androidx.lifecycle.LiveData;

public interface DataSource<T> {

    LiveData<T> getDataStream();
    LiveData<String> getErrorStream();
}
