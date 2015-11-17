package ca.binder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * Created by SheldonCOMP4980 on 11/5/2015.
 */
public class MainActivity extends Activity {

    final String LOG_HEADER = "MYLOG###";

    private final int FIRST_LAUNCH_REQUEST = 12345;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        maybeLaunchFirstLaunchLaunch();
    }

    /**
     * Check to see if it is the first launch
     */
    private void maybeLaunchFirstLaunchLaunch() {

        //Check to see if the ProfileCreationActivity has ever completed
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = sharedPreferences.getBoolean(getString(R.string.previously_started), false);

        if (!previouslyStarted) {
            // first launch / profile creation
            Intent intent = new Intent(this, ProfileCreationActivity.class);
            startActivityForResult(intent, FIRST_LAUNCH_REQUEST);
        } else {
            // start viewing suggestions
            startActivity(new Intent(this, SuggestionViewActivity.class));
            finish();
        }

    }

    /**
     *
     * @param requestCode   - Code used to identify where the startActivityForResult is called from
     * @param resultCode    - Identifies if the activity completed (RESULT_OK, _CANCELLED, etc)
     * @param data          - Data passed back from the activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FIRST_LAUNCH_REQUEST && data != null){   //Handle ProfileCreationActivity result
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor edit = sharedPreferences.edit();

            //Update PREVIOUSLY_STARTED field in SharedPrefs, ProfileCreationActivity should not start anymore
            edit.putBoolean(getString(R.string.previously_started), data.getBooleanExtra("finished", false));
            edit.commit();
            Intent intent = new Intent(this, SuggestionViewActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //Ensure back does not navigate back to other pages, not necessary for functionality
    @Override
    public void onBackPressed() {
    }
}
