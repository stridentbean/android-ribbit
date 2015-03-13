package ind.habanero.realestatecalculator;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseUser;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import database.PropertyDataSource;
import database.StorageUtility;
import models.Property;


public class EstimatePropertyActivity extends ActionBarActivity {

    @InjectView(R.id.estimatePropertyButton) Button mEstimateButton;
    @InjectView(R.id.propertiesListView)
    ListView mProperties;

    public static final String TAG = EstimatePropertyActivity.class.getSimpleName();

    private ArrayList<Property> properties;
    PropertyListAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_property);
        ButterKnife.inject(this);
        setTitle(getResources().getString(R.string.title_activity_estimate_property));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.ic_launcher);

        //login
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser == null) {
            navigateToLogin();
        } else {
            Log.i(TAG, currentUser.getEmail());

            //basicInformation
            String basicInfo = StorageUtility.read(getString(R.string.basic_information_location), getApplicationContext());
            Log.i(TAG, "BasicInfo:" + basicInfo + "|END");
            if(basicInfo == "") {
                //if there is no basic info, this is the users first time. Send them to basic information page
                Intent i = new Intent(getApplicationContext(), BasicInformationActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                startActivity(i);

            } else {

                retrieveData();
                adapter = new PropertyListAdapter(this, R.layout.property_list_item, properties);
                //ListView atomPaysListView = (ListView)findViewById(R.id.EnterPays_atomPaysList);
                mProperties.setAdapter(adapter);
                Log.i(TAG, properties.size() + "");

                mEstimateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), PropertyInfoActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });
            }
        }


    }

    private void navigateToLogin() {
        Intent i = new Intent(EstimatePropertyActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void retrieveData() {
        PropertyDataSource ds = new PropertyDataSource(this);
        properties = ds.readProperties();
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        //disable for this page
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_estimate_property, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_basic_information:
                Intent i = new Intent(getApplicationContext(), BasicInformationActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                break;
            /*
            case R.id.action_logout:
                ParseUser currentUser = ParseUser.getCurrentUser();
                currentUser.logOut();
                navigateToLogin();
                break;
            */
        }

        return super.onOptionsItemSelected(item);
    }

    public void clickProperty(View v) {
        PropertyListAdapter.AtomHolder item = (PropertyListAdapter.AtomHolder)v.getTag();
        Log.d(TAG, "Addy " + item.toString());


    }
}
