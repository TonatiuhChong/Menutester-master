package com.example.hombr.menutester;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int REGISTRADO = 777;
    private GoogleApiClient googleApiClient;

    FirebaseAuth mAuth;
    private EditText EditTextEmail, EditTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient =new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        EditTextEmail=(EditText)findViewById(R.id.editTextEmail);
        EditTextPassword=(EditText)findViewById(R.id.editTextPassword);


        findViewById(R.id.BtnGoogle).setOnClickListener(this);
        findViewById(R.id.Registrarse).setOnClickListener(this);
        findViewById(R.id.Entrar).setOnClickListener(this);


    }

    private void userLogin() {
        String email = EditTextEmail.getText().toString().trim();
        String password = EditTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            EditTextEmail.setError("Email is required");
            EditTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EditTextEmail.setError("Please enter a valid email");
            EditTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            EditTextPassword.setError("Password is required");
            EditTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            EditTextPassword.setError("Minimum lenght of password should be 6");
            EditTextPassword.requestFocus();
            return;
        }



        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REGISTRADO){
            GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }


    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            goMainScreen();
        }else {
            Toast.makeText(this, R.string.NOTLOGIN, Toast.LENGTH_SHORT).show();
        }


    }

    private void goMainScreen() {

        Intent intent=new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.BtnGoogle:
                Intent intent= Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,REGISTRADO);
                break;
            case  R.id.Registrarse:
                finish();
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case  R.id.Entrar:
                userLogin();
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
*/

}
