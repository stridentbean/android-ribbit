package ind.habanero.realestatecalculator;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SignUpActivity extends ActionBarActivity {

    public static final String TAG = SignUpActivity.class.getSimpleName();
    @InjectView(R.id.emailText)    EditText mEmailText;
    @InjectView(R.id.passwordText) EditText mPasswordText;
    @InjectView(R.id.confirmPasswordText) EditText mConfirmPasswordText;
    @InjectView(R.id.signUpButton) Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        setTitle(getResources().getString(R.string.title_activity_register));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.ic_launcher);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmailText.getText().toString().trim();
                String password = mPasswordText.getText().toString().trim();

                if (isValidForm()) {
                    ParseUser user = new ParseUser();
                    user.setUsername(email);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.i(TAG, "sign up success");
                                Intent intent = new Intent(SignUpActivity.this, BasicInformationActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setMessage(e.getMessage())
                                        .setTitle(R.string.register_error_title)
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                Log.i(TAG, e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean isValidForm() {
        boolean isValid = true;

        if (mEmailText.getText().toString().trim().isEmpty()) {
            mEmailText.setError(getResources().getString(R.string.required_field_text));
            isValid = false;
        } else {
            mEmailText.setError(null);
        }
        if (mPasswordText.getText().toString().isEmpty()) {
            mPasswordText.setError(getResources().getString(R.string.required_field_text));
            isValid = false;
        } else {
            mPasswordText.setError(null);
        }
        if (!mPasswordText.getText().toString().equals(mConfirmPasswordText.getText().toString())) {
            mPasswordText.setError(getResources().getString(R.string.error_confirm_password));
            mConfirmPasswordText.setError(getResources().getString(R.string.error_confirm_password));
            isValid = false;
        } else {
            mPasswordText.setError(null);
        }
        return isValid;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
