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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LoginActivity extends ActionBarActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();
    @InjectView(R.id.emailText)    EditText mEmailText;
    @InjectView(R.id.passwordText) EditText mPasswordText;
    @InjectView(R.id.signInButton) Button mSignInButton;
    @InjectView(R.id.signUpButton) Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        setTitle(getResources().getString(R.string.title_activity_login));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.ic_launcher);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmailText.getText().toString().trim();
                String password = mPasswordText.getText().toString().trim();

                if(isValidForm()) {
                    ParseUser user = new ParseUser();
                    user.setUsername(email);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.logInInBackground(email, password, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (e == null) {
                                Log.i(TAG, "sign up success");
                                Intent intent = new Intent(LoginActivity.this, BasicInformationActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage(e.getMessage())
                                        .setTitle(R.string.login_error_title)
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

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
        return isValid;
    }
}
