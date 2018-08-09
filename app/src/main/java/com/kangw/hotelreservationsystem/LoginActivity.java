package com.kangw.hotelreservationsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final String GOOGLE_PRIVACY_POLICY_URL = "https://www.google.com/policies/privacy/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        //if the user is signed in, launch homepage, else launch the sign in (AuthUI) Activity
        if (auth.getCurrentUser() != null) {
            finish();
        } else {
            signIn();
        }
    }

    public void signIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.logo)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build()
                                        //new AuthUI.IdpConfig.FacebookBuilder().build(),
                                        //new AuthUI.IdpConfig.GitHubBuilder().build()
                                        //new AuthUI.IdpConfig.TwitterBuilder().build()
                                )
                        )
                        .setTosAndPrivacyPolicyUrls(GOOGLE_TOS_URL,GOOGLE_PRIVACY_POLICY_URL)
                        .setIsSmartLockEnabled(false,true)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == RESULT_OK) {
                finish();
                //updateUserDetails();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(getApplicationContext(), R.string.sign_in_cancelled, Toast.LENGTH_LONG).show();
                    finish();
                    super.onBackPressed();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(getApplicationContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(getApplicationContext(), R.string.unknown_error, Toast.LENGTH_LONG).show();
                    return;
                }
            }

            Toast.makeText(getApplicationContext(), R.string.unknown_sign_in_response, Toast.LENGTH_LONG).show();
        }
    }
}
