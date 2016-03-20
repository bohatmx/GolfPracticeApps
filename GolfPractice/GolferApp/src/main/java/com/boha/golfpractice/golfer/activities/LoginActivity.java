package com.boha.golfpractice.golfer.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.boha.golfpractice.golfer.R;
import com.boha.golfpractice.library.activities.MonApp;
import com.boha.golfpractice.library.dto.PlayerDTO;
import com.boha.golfpractice.library.dto.RequestDTO;
import com.boha.golfpractice.library.dto.ResponseDTO;
import com.boha.golfpractice.library.util.OKHttpException;
import com.boha.golfpractice.library.util.OKUtil;
import com.boha.golfpractice.library.util.SharedUtil;
import com.boha.golfpractice.library.util.SnappyGeneral;
import com.boha.golfpractice.library.util.Util;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mFirstName, mLastName;
    private View mProgressView;
    private View mLoginFormView;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
                btnSignIn = (Button) findViewById(R.id.btnSignIn);


        btnSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        checkSignedIn();
    }

    private void checkSignedIn() {
        if (SharedUtil.getPlayer(getApplicationContext()) != null) {
            startMain();
        }
    }

    private void startMain() {
        Intent m = new Intent(getApplicationContext(), PlayerMainActivity.class);
        startActivity(m);
    }

    private void register() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(mFirstName.getText().toString())) {
            mFirstName.setError("Please enter first name");
            focusView = mFirstName;
            cancel = true;
        }
        if (TextUtils.isEmpty(mLastName.getText().toString())) {
            mLastName.setError("Please enter last name");
            focusView = mLastName;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            OKUtil okUtil = new OKUtil();
            RequestDTO w = new RequestDTO(RequestDTO.REGISTER_PLAYER);
            PlayerDTO p = new PlayerDTO();
            p.setFirstName(mFirstName.getText().toString());
            p.setLastName(mLastName.getText().toString());
            p.setEmail(mEmailView.getText().toString());
            p.setPin(mPasswordView.getText().toString());
            w.setPlayer(p);

            try {
                okUtil.sendGETRequest(getApplicationContext(), w, this,new OKUtil.OKListener() {
                    @Override
                    public void onResponse(ResponseDTO response) {
                        showProgress(false);
                        PlayerDTO p = response.getPlayerList().get(0);
                        SharedUtil.savePlayer(getApplicationContext(), p);
                        startMain();
                    }

                    @Override
                    public void onError(String message) {
                        showProgress(false);
                        Util.showToast(getApplicationContext(),message);
                    }
                });
            } catch (OKHttpException e) {
                e.printStackTrace();
            }
        }
    }

    private void signIn() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            OKUtil okUtil = new OKUtil();
            RequestDTO w = new RequestDTO(RequestDTO.SIGN_IN_PLAYER);
            w.setEmail(mEmailView.getText().toString());
            w.setPassword(mPasswordView.getText().toString());
            try {
                okUtil.sendGETRequest(getApplicationContext(), w, this,new OKUtil.OKListener() {
                    @Override
                    public void onResponse(ResponseDTO response) {
                        showProgress(false);
                        PlayerDTO p = response.getPlayerList().get(0);
                        SharedUtil.savePlayer(getApplicationContext(), p);
                        SnappyGeneral.addClubs((MonApp)getApplication(),response.getClubList(),null);
                        SnappyGeneral.addShotShapes((MonApp)getApplication(),response.getShotShapeList(),null);
                        startMain();
                    }

                    @Override
                    public void onError(String message) {
                        showProgress(false);
                        Util.showErrorToast(getApplicationContext(), message);
                    }
                });
            } catch (OKHttpException e) {
                e.printStackTrace();
            }
        }
    }

    private void populateAutoComplete() {
        addEmailsToAutoComplete();
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    static final int REQUEST_READ_CONTACTS = 989;

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        if (show)
            mProgressView.setVisibility(View.VISIBLE);
        else
            mProgressView.setVisibility(View.GONE);
    }

    List<String> mList = new ArrayList<>();
    AccountManager accountManager;

    private void addEmailsToAutoComplete() {
        accountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType("com.google");
        if (accounts.length > 0) {
            for (Account a : accounts) {
                mList.add(a.name);
            }
        }
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, mList);

        mEmailView.setAdapter(adapter);
    }


}

