package muchbeer.raum.com.gcertificationproject1.screen;

import java.util.List;

import muchbeer.raum.com.gcertificationproject1.recyclerv.CoinModel;

public interface MainScreen {

    void updateData(List<CoinModel> data);
    void setError(String msg);
}
