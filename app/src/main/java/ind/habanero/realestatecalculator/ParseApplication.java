package ind.habanero.realestatecalculator;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseUser;

public class ParseApplication extends Application {

    public static final String TAG = ParseApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        Log.i(TAG, "Application Call");
        // Initialize Crash Reporting.
        ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, getApplicationContext().getResources().getString(R.string.application_id), getApplicationContext().getResources().getString(R.string.client_key));
    }
}
