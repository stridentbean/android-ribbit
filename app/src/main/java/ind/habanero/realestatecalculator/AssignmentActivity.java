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

import java.text.NumberFormat;
import java.util.ArrayList;

import Utilities.PDFUtility;
import Utilities.Utility;
import butterknife.ButterKnife;
import butterknife.InjectView;
import database.PropertyDataSource;
import database.StorageUtility;
import models.BasicInformation;
import models.Property;


public class AssignmentActivity extends ActionBarActivity {

    private static final int EMAIL_FINISHED = 1;
    @InjectView(R.id.homeButton)
    Button mHomeButton;
    @InjectView(R.id.repairsView) TextView mRepairsView;
    @InjectView(R.id.multPercText) TextView mPercView;
    @InjectView(R.id.multValueText) TextView mPercText;
    @InjectView(R.id.arvText) EditText mARVText;
    @InjectView(R.id.profitText) EditText mYourProfitText;
    @InjectView(R.id.repairText) TextView mRepairText;
    @InjectView(R.id.maoText) TextView mMAOText;
    @InjectView(R.id.resaleText)TextView mResaleText;
    @InjectView(R.id.emailAssignmentButton) Button mEmailButton;
    private Property mProperty;
    public static final String TAG = AssignmentActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        ButterKnife.inject(this);
        setTitle(getResources().getString(R.string.title_activity_assignment));
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

        mEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidForm()) {
                    persistData();

                    //get basic info
                    Gson gson = new Gson();
                    String json = StorageUtility.read(getString(R.string.basic_information_location), getApplicationContext());
                    BasicInformation basicInformation = gson.fromJson(json, BasicInformation.class);

                    Intent mailer = PDFUtility.getAssignmentEmailIntent(basicInformation, mProperty, getApplicationContext());

                    //String PACKAGE_NAME = AssignmentActivity.class.getPackage().getName();
                    //Uri uri = Uri.fromFile(file);

                    //grant permission for app with package "packegeName", eg. before starting other app via intent
                    //context.grantUriPermission(, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    //setup email
                /*
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{basicInformation.getEmail()});
                intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_assignment_subject));
                intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.email_message));
                intent.putExtra(Intent.EXTRA_STREAM, uri)*/
                    //Intent mailer = Intent.createChooser(intent, "Send email via...");
                    startActivityForResult(mailer, EMAIL_FINISHED);
                    overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);

                    //revoke permisions
                    // context.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

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

        return isValid;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == EMAIL_FINISHED) {

        }
    }

    private void retrieveData() {
        mPercView.setText(Utility.getMultiplierText(mProperty.getADOM()));
        if(mProperty.hasARV()) {
            Utility.formatCurrencyTextField(mARVText, mProperty.getARV());
        }
        if(mProperty.hasProfit()) {
            Utility.formatCurrencyTextField(mYourProfitText, mProperty.getProfit());
        }

        Utility.formatCurrencyTextField(mRepairText, mProperty.getRepairEstimate());
        refreshCalculations();
    }

    private void refreshCalculations() {

        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

        int rawMultValue = 0;
        int rawYourProfit = 0;
        int rawRepairs = mProperty.getRepairEstimate();
        if(!mARVText.getText().toString().isEmpty()) {
            rawMultValue = Utility.getMultiplierValue(mProperty.getADOM(), Utility.getIntValueFromCurrencyTextField(mARVText));
        }
        if(!mYourProfitText.getText().toString().isEmpty()) {
            rawYourProfit = Utility.getIntValueFromCurrencyTextField(mYourProfitText);
        }

        Utility.formatCurrencyTextField(mRepairText, rawRepairs + "");
        Utility.formatCurrencyTextField(mPercText, rawMultValue + "");

        int rawMAO = rawMultValue - rawRepairs - rawYourProfit;
        int rawResale = rawMAO + rawYourProfit;

        Utility.formatCurrencyTextField(mMAOText, rawMAO + "");
        Utility.formatCurrencyTextField(mResaleText, rawResale + "");

        Log.i(TAG, "rawMultValue: " + rawMultValue);
        Log.i(TAG, "rawMAO: " +rawMAO);
        Log.i(TAG, "rawResale: " +rawResale);
        Log.i(TAG, "RefreshCalculation");
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

        PropertyDataSource ds = new PropertyDataSource(this);
        ds.create(mProperty);
    }
}
