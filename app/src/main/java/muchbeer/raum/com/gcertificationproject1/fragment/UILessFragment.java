package muchbeer.raum.com.gcertificationproject1.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import muchbeer.raum.com.gcertificationproject1.viewmodel.CryptoViewModel;

public class UILessFragment extends Fragment {

    private static final String TAG = UILessFragment.class.getSimpleName();
    private CryptoViewModel mViewModel;
    private final Observer<Double> mObserver= totalMarketCap ->
            Log.d(TAG, "onChanged() called with: aDouble = [" +totalMarketCap + "]");

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //mViewModel = ViewModelProviders.of(this).get(CryptoViewModel.class);

        mViewModel = ViewModelProviders.of(getActivity()).get(CryptoViewModel.class);

        mViewModel.getTotalMarketCap().observe(this,mObserver);

    }
}
