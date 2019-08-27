package muchbeer.raum.com.gcertificationproject1;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import muchbeer.raum.com.gcertificationproject1.tracking.Tracker;

public class TrackingActivity extends AppCompatActivity {

    protected Tracker mTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTracker=new Tracker(this);
    }
}
