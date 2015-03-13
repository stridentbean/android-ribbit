package ind.habanero.realestatecalculator;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import Utilities.Utility;
import butterknife.ButterKnife;
import butterknife.InjectView;
import database.PropertyDataSource;
import models.Property;


public class PropertyInfoActivity extends ActionBarActivity {

    public static final String TAG = PropertyInfoActivity.class.getSimpleName();

    private Property mProperty;
    String[] bedroomSpinnerValues;
    String[] bathroomSpinnerValues;

    @InjectView(R.id.bedroomSpinner)Spinner mBedroomSpinner;
    @InjectView(R.id.bathroomSpinner)Spinner mBathroomSpinner;
    @InjectView(R.id.askingPriceText)EditText mAskingPriceText;
    @InjectView(R.id.nextButton)Button mNextButton;
    @InjectView(R.id.addressLine1Text) EditText mAddressLine1Text;
    @InjectView(R.id.addressLine2Text) EditText mAddressLine2Text;
    @InjectView(R.id.cityText) EditText mCityText;
    @InjectView(R.id.zipText) EditText mZipText;
    @InjectView(R.id.squareFootageText) EditText mSquareFootage;
    @InjectView(R.id.yearBuiltText) EditText mYearBuilt;
    @InjectView(R.id.adomText) EditText mADOM;
    @InjectView(R.id.stateAutoText) AutoCompleteTextView mStateAutoText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_info);
        ButterKnife.inject(this);
        setTitle(getResources().getString(R.string.title_activity_property_info));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.ic_launcher);

        String[] states = getResources().
                getStringArray(R.array.list_of_states);
        ArrayAdapter stateAdapter = new ArrayAdapter
                (this,android.R.layout.simple_list_item_1,states);
        mStateAutoText.setAdapter(stateAdapter);

        bedroomSpinnerValues = getResources().getStringArray(R.array.bedroom_values);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bedroomSpinnerValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBedroomSpinner.setAdapter(adapter);

        bathroomSpinnerValues = getResources().getStringArray(R.array.bathroom_values);
        ArrayAdapter<String> adapterDouble = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bathroomSpinnerValues);
        adapterDouble.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBathroomSpinner.setAdapter(adapterDouble);

        ArrayList<Property> properties = getIntent().getParcelableArrayListExtra("property");
        if(properties != null) {
            mProperty = properties.get(0);
            retrieveData();
        }

        TextWatcher currencyTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mAskingPriceText.removeTextChangedListener(this);
                Utility.formatCurrencyTextField(mAskingPriceText, editable);
                mAskingPriceText.addTextChangedListener(this);
            }
        };

        mAskingPriceText.addTextChangedListener(currencyTextWatcher);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidForm(true)) {
                    persistData();
                    Intent i = new Intent(getApplicationContext(), RepairEstimateActivity.class);
                    ArrayList<Property> toSend = new ArrayList<Property>();
                    toSend.add(mProperty);
                    i.putParcelableArrayListExtra("property", toSend);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

    }

    /**
     *
     * @param displayErrors
     * Displays errors on form if true
     * @return
     * Returns true if the form is valid
     */

    private boolean isValidForm(boolean displayErrors) {
        boolean isValid = true;

        if(mAddressLine1Text.getText().toString().trim().equals("")){
            if(displayErrors) {
                mAddressLine1Text.setError(getResources().getString(R.string.required_field_text));
            }
            isValid = false;
        } else {
            mAddressLine1Text.setError(null);
        }
        if(mCityText.getText().toString().trim().equals("")){
            if(displayErrors) {
                mCityText.setError(getResources().getString(R.string.required_field_text));
            }
            isValid = false;
        } else {
            mCityText.setError(null);
        }

        String[] states = getApplicationContext().getResources().getStringArray(R.array.list_of_states);
        if(Utility.findIndexOf(mStateAutoText.getText().toString().trim(), states) == -1){
            mStateAutoText.setError(getResources().getString(R.string.required_full_state));
            isValid = false;
        } else {
            mStateAutoText.setError(null);
        }

        if(mZipText.getText().toString().trim().equals("")){
            if(displayErrors) {
                mZipText.setError(getResources().getString(R.string.required_field_text));
            }
            isValid = false;
        } else {
            mZipText.setError(null);
        }
        if(!mADOM.getText().toString().isEmpty() && Integer.valueOf(mADOM.getText().toString()).intValue() < getResources().getInteger(R.integer.minimum_dom)) {
            if(displayErrors) {
                mADOM.setError(getResources().getString(R.string.minimum_dom_warning));
            }
            isValid = false;

        } else {
            mADOM.setError(null);
        }

        return isValid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        persistData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        persistData();
    }

    private void retrieveData() {
        mAddressLine1Text.setText(mProperty.getAddressLine1());
        mAddressLine2Text.setText(mProperty.getAddressLine2());
        mCityText.setText(mProperty.getCity());
        mStateAutoText.setText(mProperty.getState());
        mZipText.setText(mProperty.getZip());
        Utility.formatCurrencyTextField(mAskingPriceText, mProperty.getAskingPrice() + "");
        mBedroomSpinner.setSelection(getIndexOfValues(bedroomSpinnerValues, mProperty.getBedrooms()));
        mBathroomSpinner.setSelection(getIndexOfValues(bathroomSpinnerValues, mProperty.getBathrooms()));
        mSquareFootage.setText(mProperty.getSquareFootage() + "");
        mYearBuilt.setText(mProperty.getYearBuilt() + "");
        mADOM.setText(mProperty.getADOM() + "");

    }

    /**
     *
     * @param list
     * The list of values to search against
     * @param value
     * The value that is being searched for
     * @return
     * The index of the value if found, otherwise returns -1
     */
    private int getIndexOfValues(String[] list, String value) {
        for (int i = 0; i< list.length; i++) {
            if(value.equals(list[i])) {
                return i;
            }
        }
        return -1;
    }

    private void persistData() {
        if(isValidForm(false)) {
            PropertyDataSource ds = new PropertyDataSource(this);

            Log.i(TAG, "bedroom: " + mBedroomSpinner.getSelectedItem().toString());
            Log.i(TAG, "bathroom: " + mBathroomSpinner.getSelectedItem().toString());

            if (mProperty == null) {
                mProperty = new Property(mAddressLine1Text.getText().toString(),
                        mAddressLine2Text.getText().toString(),
                        mCityText.getText().toString(),
                        mStateAutoText.getText().toString(),
                        mZipText.getText().toString(),
                        Utility.getIntValueFromCurrencyTextField(mAskingPriceText),
                        mBedroomSpinner.getSelectedItem().toString(),
                        mBathroomSpinner.getSelectedItem().toString(),
                        Utility.getIntValueFromCurrencyTextField(mSquareFootage),
                        Utility.getIntValueFromCurrencyTextField(mYearBuilt),
                        Utility.getIntValueFromCurrencyTextField(mADOM));

            } else {
                mProperty.setAddressLine1(mAddressLine1Text.getText().toString());
                mProperty.setAddressLine2(mAddressLine2Text.getText().toString());
                mProperty.setCity(mCityText.getText().toString());
                mProperty.setState(mStateAutoText.getText().toString());
                mProperty.setZip(mZipText.getText().toString());
                mProperty.setAskingPrice(Utility.getIntValueFromCurrencyTextField(mAskingPriceText));
                mProperty.setBedrooms(mBedroomSpinner.getSelectedItem().toString());
                mProperty.setBathrooms(mBathroomSpinner.getSelectedItem().toString());
                mProperty.setSquareFootage(Utility.getIntValueFromCurrencyTextField(mSquareFootage));
                mProperty.setYearBuilt(Utility.getIntValueFromCurrencyTextField(mYearBuilt));
                mProperty.setADOM(Utility.getIntValueFromCurrencyTextField(mADOM));
            }

            ds.create(mProperty);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
