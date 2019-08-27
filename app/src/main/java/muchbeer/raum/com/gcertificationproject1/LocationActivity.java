package muchbeer.raum.com.gcertificationproject1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import muchbeer.raum.com.gcertificationproject1.location.MyLocationManager;

public class LocationActivity extends TrackingActivity {

    private static final String TAG = LocationActivity.class.getSimpleName();
    private final static int PERMISSION_REQUEST_LOCATION = 1234;
    private MyLocationManager myLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myLocationManager=new MyLocationManager(this,mTracker);
        checkLocationPermission();
        getLifecycle().addObserver(myLocationManager);
    }

    private void checkLocationPermission() {
        Log.d(TAG, "checkLocationPermission() called");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Please give me location permissions", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_LOCATION);
            }
        }
        else
            myLocationManager.buildGoogleApiClient();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult() called with: requestCode = [" + requestCode + "], permissions = [" + permissions + "], grantResults = [" + grantResults + "]");
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        myLocationManager.buildGoogleApiClient();
                    }
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_LOCATION);
                }
                return;
            }
        }
    }
}
