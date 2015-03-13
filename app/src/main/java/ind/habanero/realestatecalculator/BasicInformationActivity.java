package ind.habanero.realestatecalculator;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import Utilities.Utility;
import butterknife.ButterKnife;
import butterknife.InjectView;
import database.ParseCloudStorage;
import database.StorageUtility;
import models.BasicInformation;


public class BasicInformationActivity extends ActionBarActivity {
    public static final String TAG = BasicInformationActivity.class.getSimpleName();

    @InjectView(R.id.saveButton) Button mSaveButton;
    @InjectView(R.id.firstNameText) TextView mFirstNameText;
    @InjectView(R.id.lastNameText) TextView mLastNameText;
    @InjectView(R.id.LLCText) TextView mLLCText;
    @InjectView(R.id.phoneText) TextView mPhoneText;
    @InjectView(R.id.addressLine1Text) TextView mAddressLine1Text;
    @InjectView(R.id.addressLine2Text) TextView mAddressLine2Text;
    @InjectView(R.id.cityText) TextView mCityText;
    @InjectView(R.id.zipText) TextView mZipText;
    @InjectView(R.id.emailText) TextView mEmailText;
    @InjectView(R.id.stateAutoText) AutoCompleteTextView mStateAutoCompleteView;
    private BasicInformation basicInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_information);
        ButterKnife.inject(this);
        setTitle(getResources().getString(R.string.title_activity_basic_information));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.ic_launcher);

        retrieveData();

        //set phone format
        mPhoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidForm()) {
                    persistData();
                    Intent i = new Intent(getApplicationContext(), EstimatePropertyActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        String[] states = getResources().
                getStringArray(R.array.list_of_states);
        ArrayAdapter adapter = new ArrayAdapter
                (this,android.R.layout.simple_list_item_1,states);
        mStateAutoCompleteView.setAdapter(adapter);
    }

    private void retrieveData() {
        Gson gson = new Gson();
        String json = StorageUtility.read(getString(R.string.basic_information_location), getApplicationContext());

        basicInformation = gson.fromJson(json, BasicInformation.class);
        if(basicInformation != null) {
            Log.i(TAG, basicInformation.toString());

            mFirstNameText.setText(basicInformation.getFirstName());
            mLastNameText.setText(basicInformation.getLastName());
            mLLCText.setText(basicInformation.getLLC());
            mPhoneText.setText(basicInformation.getPhone());
            mEmailText.setText(basicInformation.getEmail());
            mAddressLine1Text.setText(basicInformation.getAddressLine1());
            mAddressLine2Text.setText(basicInformation.getAddressLine2());
            mCityText.setText(basicInformation.getCity());
            mStateAutoCompleteView.setText(basicInformation.getState());
            mZipText.setText(basicInformation.getZip()+ "");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void persistData() {

        if(isValidForm()){
            if(basicInformation!=null) {
                //this will maintain the ID
                basicInformation.setFirstName(mFirstNameText.getText().toString());
                basicInformation.setLastName(mLastNameText.getText().toString());
                basicInformation.setLLC(mLLCText.getText().toString());
                basicInformation.setPhone(mPhoneText.getText().toString());
                basicInformation.setEmail(mEmailText.getText().toString());
                basicInformation.setAddressLine1(mAddressLine1Text.getText().toString());
                basicInformation.setAddressLine2(mAddressLine2Text.getText().toString());
                basicInformation.setCity(mCityText.getText().toString());
                basicInformation.setState(mStateAutoCompleteView.getText().toString());
                basicInformation.setZip(Integer.valueOf(mZipText.getText().toString()).intValue());
            }else {
                basicInformation = new BasicInformation(mFirstNameText.getText().toString(),
                        mLastNameText.getText().toString(),
                        mLLCText.getText().toString(),
                        mPhoneText.getText().toString(),
                        mEmailText.getText().toString(),
                        mAddressLine1Text.getText().toString(),
                        mAddressLine2Text.getText().toString(),
                        mCityText.getText().toString(),
                        mStateAutoCompleteView.getText().toString(),
                        Integer.valueOf(mZipText.getText().toString()).intValue());
            }

            //store to cloud and set the ID
            ParseCloudStorage cloudStorage = new ParseCloudStorage(this);
            cloudStorage.store(basicInformation);

            //store to local this probably won't have the ID right now, but it will be taken care of in ParseCloudStorage
            Gson gson = new Gson();
            StorageUtility.save(gson.toJson(basicInformation), getString(R.string.basic_information_location), getApplicationContext());
        }
    }

    private boolean isValidForm() {
        boolean isValid = true;

        if(mFirstNameText.getText().toString().trim().equals("")){
            mFirstNameText.setError(getResources().getString(R.string.required_field_text));
            isValid = false;
        } else {
            mFirstNameText.setError(null);
        }
        if(mLastNameText.getText().toString().trim().equals("")){
            mLastNameText.setError(getResources().getString(R.string.required_field_text));
            isValid = false;
        } else {
            mLastNameText.setError(null);
        }

        //make sure the phone is properly filled in
        String phoneText = mPhoneText.getText().toString().trim();
        int digitCount = 0;
        for(char c :phoneText.toCharArray()) {
            if(Character.isDigit(c)) {
                digitCount++;
            }
        }

        if(digitCount < 9){
            mPhoneText.setError(getResources().getString(R.string.required_phone_area_code));
            isValid = false;
        } else {
            mPhoneText.setError(null);
        }

        if(mAddressLine1Text.getText().toString().trim().equals("")){
            mAddressLine1Text.setError(getResources().getString(R.string.required_field_text));
            isValid = false;
        } else {
            mAddressLine1Text.setError(null);
        }
        if(mCityText.getText().toString().trim().equals("")){
            mCityText.setError(getResources().getString(R.string.required_field_text));
            isValid = false;
        } else {
            mCityText.setError(null);
        }

        String[] states = getApplicationContext().getResources().getStringArray(R.array.list_of_states);
        if(Utility.findIndexOf(mStateAutoCompleteView.getText().toString().trim(), states) == -1){
            mStateAutoCompleteView.setError(getResources().getString(R.string.required_full_state));
            isValid = false;
        } else {
            mStateAutoCompleteView.setError(null);
        }

        if(mZipText.getText().toString().trim().equals("")){
            mZipText.setError(getResources().getString(R.string.required_field_text));
            isValid = false;
        } else {
            mZipText.setError(null);
        }
        if(mEmailText.getText().toString().trim().equals("")){
            mEmailText.setError(getResources().getString(R.string.required_field_text));
            isValid = false;
        } else {
            mEmailText.setError(null);
        }

        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
