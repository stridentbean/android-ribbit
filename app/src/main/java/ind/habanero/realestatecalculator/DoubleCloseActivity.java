package ind.habanero.realestatecalculator;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import Utilities.PDFUtility;
import Utilities.Utility;
import butterknife.ButterKnife;
import butterknife.InjectView;
import database.PropertyDataSource;
import database.StorageUtility;
import models.BasicInformation;
import models.Property;


public class DoubleCloseActivity extends ActionBarActivity {
    private static final int EMAIL_FINISHED = 1;

    @InjectView(R.id.homeButton)
    Button mHomeButton;
    @InjectView(R.id.repairsView)
    TextView mRepairsView;
    @InjectView(R.id.multPercText) TextView mPercView;
    @InjectView(R.id.multValueText) TextView mPercText;
    @InjectView(R.id.arvText)
    EditText mARVText;
    @InjectView(R.id.profitText) EditText mYourProfitText;
    @InjectView(R.id.repairText) TextView mRepairText;
    @InjectView(R.id.maoText) TextView mMAOText;
    @InjectView(R.id.resaleText)TextView mResaleText;
    @InjectView(R.id.moneyCostsText) EditText mMoneyCostsText;
    @InjectView(R.id.transferCostsText) EditText mTransferCostsText;
    @InjectView(R.id.closingCostsText) EditText mClosingCostsText;
    @InjectView(R.id.emailDoubleCloseButton) Button mDoubleCloseButton;
    private Property mProperty;
    public static final String TAG = DoubleCloseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_close);
        ButterKnife.inject(this);
        setTitle(getResources().getString(R.string.title_activity_double_close));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.ic_launcher);

        ArrayList<Property> properties = getIntent().getParcelableArrayListExtra("property");
        mProperty = properties.get(0);
        retrieveData();

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                persistData();
                Intent i = new Intent(getApplicationContext(), EstimatePropertyActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        mDoubleCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidForm()) {
                    persistData();

                    //get basic info
                    Gson gson = new Gson();
                    String json = StorageUtility.read(getString(R.string.basic_information_location), getApplicationContext());
                    BasicInformation basicInformation = gson.fromJson(json, BasicInformation.class);

                    //setup email
                    Intent mailer = PDFUtility.getDoubleCloseEmailIntent(basicInformation, mProperty, getApplicationContext());
                    startActivityForResult(mailer, EMAIL_FINISHED);
                    overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
                }
            }
        });

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
                mARVText.removeTextChangedListener(this);
                Utility.formatCurrencyTextField(mARVText, editable);
                mARVText.addTextChangedListener(this);

                Utility.formatCurrencyTextField(mMoneyCostsText, Utility.getIntValueFromCurrencyTextField(mARVText) * 0.001 + "");
                Utility.formatCurrencyTextField(mTransferCostsText, "15000");
                Utility.formatCurrencyTextField(mClosingCostsText, Utility.getIntValueFromCurrencyTextField(mARVText) * 0.003 + "");

                refreshCalculations();
            }
        };
        mARVText.addTextChangedListener(currencyTextWatcher);

        TextWatcher mYourProfitTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mYourProfitText.removeTextChangedListener(this);
                Utility.formatCurrencyTextField(mYourProfitText, editable);
                mYourProfitText.addTextChangedListener(this);

                refreshCalculations();
            }
        };
        mYourProfitText.addTextChangedListener(mYourProfitTextWatcher);

        TextWatcher moneyCostsTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mMoneyCostsText.removeTextChangedListener(this);

                Utility.formatCurrencyTextField(mMoneyCostsText, editable);
                if(mMoneyCostsText.getText().toString().equals("$0.00")) {
                    //Allow the user to see the hint text
                    mMoneyCostsText.setText("");
                }
                mMoneyCostsText.addTextChangedListener(this);

                refreshCalculations();
            }
        };
        mMoneyCostsText.addTextChangedListener(moneyCostsTextWatcher);

        TextWatcher transferCostsTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTransferCostsText.removeTextChangedListener(this);
                Utility.formatCurrencyTextField(mTransferCostsText, editable);

                if(mTransferCostsText.getText().toString().equals("$0.00")) {
                    //Allow the user to see the hint text
                    mTransferCostsText.setText("");
                }
                mTransferCostsText.addTextChangedListener(this);

                refreshCalculations();
            }
        };
        mTransferCostsText.addTextChangedListener(transferCostsTextWatcher);

        TextWatcher closingCostsTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mClosingCostsText.removeTextChangedListener(this);
                Utility.formatCurrencyTextField(mClosingCostsText, editable);
                if(mClosingCostsText.getText().toString().equals("$0.00")) {
                    //Allow the user to see the hint text
                    mClosingCostsText.setText("");
                }
                mClosingCostsText.addTextChangedListener(this);

                refreshCalculations();
            }
        };
        mClosingCostsText.addTextChangedListener(closingCostsTextWatcher);


    }

    private boolean isValidForm() {
        boolean isValid = true;

        if(mARVText.getText().toString().trim().equals("")){
            mARVText.setError(getResources().getString(R.string.required_field_text));
            isValid = false;
        } else {
            mARVText.setError(null);
        }
        if(mYourProfitText.getText().toString().trim().equals("")){
            mYourProfitText.setError(getResources().getString(R.string.required_field_text));
            isValid = false;
        } else {
            mYourProfitText.setError(null);
        }
        if(mMoneyCostsText.getText().toString().trim().equals("")){
            mMoneyCostsText.setError(getResources().getString(R.string.required_field_text));
            isValid = false;
        } else {
            mMoneyCostsText.setError(null);
        }
        if(mTransferCostsText.getText().toString().trim().equals("")){
            mTransferCostsText.setError(getResources().getString(R.string.required_field_text));
            isValid = false;
        } else {
            mTransferCostsText.setError(null);
        }
        if(mClosingCostsText.getText().toString().trim().equals("")){
            mClosingCostsText.setError(getResources().getString(R.string.required_field_text));
            isValid = false;
        } else {
            mClosingCostsText.setError(null);
        }

        return isValid;
    }

    private void retrieveData() {
        mPercView.setText(Utility.getMultiplierText(mProperty.getADOM()));
        if(mProperty.hasARV()) {
            Utility.formatCurrencyTextField(mARVText, mProperty.getARV());
        }
        if(mProperty.hasProfit()) {
            Utility.formatCurrencyTextField(mYourProfitText, mProperty.getProfit());
        }
        if(mProperty.hasMoneyCosts()) {
            Utility.formatCurrencyTextField(mMoneyCostsText, mProperty.getMoneyCosts());
        }
        if(mProperty.hasTransferTaxes()) {
            Utility.formatCurrencyTextField(mTransferCostsText, mProperty.getTransferTaxes());
        }
        if(mProperty.hasClosingCosts()) {
            Utility.formatCurrencyTextField(mClosingCostsText, mProperty.getClosingCosts());
        }
        Utility.formatCurrencyTextField(mRepairText, mProperty.getRepairEstimate());

        refreshCalculations();
    }

    private void refreshCalculations() {

        int multValue = 0;
        int yourProfit = 0;
        int repairs = mProperty.getRepairEstimate();
        int moneyCosts =0;
        int transferCosts=0;
        int closingCosts=0;
        if(!mARVText.getText().toString().isEmpty()) {
            multValue = Utility.getMultiplierValue(mProperty.getADOM(), Utility.getIntValueFromCurrencyTextField(mARVText));
        }
        if(!mYourProfitText.getText().toString().isEmpty()) {
            yourProfit = Utility.getIntValueFromCurrencyTextField(mYourProfitText);
        }
        if(!mMoneyCostsText.getText().toString().isEmpty()) {
            moneyCosts = Utility.getIntValueFromCurrencyTextField(mMoneyCostsText);
        }
        if(!mTransferCostsText.getText().toString().isEmpty()) {
            transferCosts = Utility.getIntValueFromCurrencyTextField(mTransferCostsText);
        }
        if(!mClosingCostsText.getText().toString().isEmpty()) {
            closingCosts = Utility.getIntValueFromCurrencyTextField(mClosingCostsText);
        }

        Utility.formatCurrencyTextField(mRepairText, repairs + "");
        Utility.formatCurrencyTextField(mPercText, multValue + "");

        int rawMAO = multValue - repairs - yourProfit - moneyCosts - transferCosts - closingCosts;
        int rawResale = rawMAO + yourProfit;

        Utility.formatCurrencyTextField(mMAOText, rawMAO + "");
        Utility.formatCurrencyTextField(mResaleText, rawResale + "");

        Log.i(TAG, "rawMultValue: " + multValue);
        Log.i(TAG, "rawMAO: " +rawMAO);
        Log.i(TAG, "rawResale: " +rawResale);
        Log.i(TAG, "RefreshCalculation");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == EMAIL_FINISHED) {

        }
    }

    @Override
    public void onBackPressed() {
        persistData();
        Intent i = new Intent(getApplicationContext(), RepairEstimateActivity.class);
        ArrayList<Property> toSend = new ArrayList<Property>();
        toSend.add(mProperty);
        i.putParcelableArrayListExtra("property", toSend);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void persistData() {
        mProperty.setARV(Utility.getIntValueFromCurrencyTextField(mARVText));
        mProperty.setProfit(Utility.getIntValueFromCurrencyTextField(mYourProfitText));
        mProperty.setMoneyCosts(Utility.getIntValueFromCurrencyTextField(mMoneyCostsText));
        mProperty.setTransferTaxes(Utility.getIntValueFromCurrencyTextField(mTransferCostsText));
        mProperty.setClosingCosts(Utility.getIntValueFromCurrencyTextField(mClosingCostsText));

        PropertyDataSource ds = new PropertyDataSource(this);
        ds.create(mProperty);
    }
}
