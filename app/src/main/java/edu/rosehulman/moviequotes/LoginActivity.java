package edu.rosehulman.moviequotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LoginActivity extends Activity {

    private Firebase mFirebaseRef;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private String mUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase(getString(R.string.firebase_url));

        mEmailEditText = (EditText) findViewById(R.id.emailEditText);
        mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
    }


    /**
     * Utility class for authentication results
     */
    private class AuthResultHandler implements Firebase.AuthResultHandler {

        @Override
        public void onAuthenticated(AuthData authData) {
            //mAuthProgressDialog.hide();
            mUid = authData.getUid();
            Log.d("TAG", "Signed in");
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            mainIntent.putExtra("UID", mUid);
            startActivity(mainIntent);
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            //mAuthProgressDialog.hide();
            Toast.makeText(LoginActivity.this, firebaseError.toString(), Toast.LENGTH_LONG).show();
        }
    }


    public void createUser(View v) {

        final String email = mEmailEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();

        mFirebaseRef.createUser(email, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                mFirebaseRef.authWithPassword(email, password, new AuthResultHandler());
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Toast.makeText(LoginActivity.this, firebaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void loginUser(View v) {
        final String email = mEmailEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();
        mFirebaseRef.authWithPassword(email, password, new AuthResultHandler());
        Log.d("TAG", "login called");
    }


}
