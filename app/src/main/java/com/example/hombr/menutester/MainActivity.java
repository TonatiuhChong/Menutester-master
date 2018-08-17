package com.example.hombr.menutester;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.OnConnectionFailedListener {

    private TextView Usuario, Email;
    private ImageView Fusuario;
    private GoogleApiClient googleApiClient;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;
    private NavigationView navigationView;
    private android.support.v7.widget.Toolbar mToolbar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //******Login mAuth
        mAuth = FirebaseAuth.getInstance();
            View hView =  navigationView.getHeaderView(0);
            Usuario = (TextView) hView.findViewById(R.id.TxtUsuario);
            Email = (TextView) hView.findViewById(R.id.TxtEmail);
            Fusuario=(ImageView) hView.findViewById(R.id.Fusuario);


        //********************

        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient =new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
                //noinspection SimplifiableIfStatement
        if (id==R.id.Comentarios){
            AlertDialog.Builder comentarios= new AlertDialog.Builder(this);
            comentarios.setTitle("Sugerencias");
            final EditText ComenCorreo = new EditText( this);
            final EditText ComenTexto= new EditText(this);
            comentarios.setView(ComenCorreo);
            comentarios.setView(ComenTexto);
            comentarios.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Aqui se pondra la rutina para el envio de correo", Toast.LENGTH_SHORT).show();

                }
            });
            comentarios.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

        }
        if (id==R.id.Ayuda){
            Toast.makeText(this, "Esta pagina esta en desarrollo", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.CerrarSesion) {
            FirebaseAuth.getInstance().signOut();
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()){
                        goLogInScreen();
                    }else {
                        Toast.makeText(MainActivity.this, R.string.NOCERRO, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fm= getSupportFragmentManager();
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            fm.beginTransaction().replace(R.id.escenario, new ReconocimientoFragment()).commit();
        } else if (id == R.id.nav_gallery) {
            fm.beginTransaction().replace(R.id.escenario, new ControlFragment()).commit();
        } else if (id == R.id.nav_slideshow) {
            fm.beginTransaction().replace(R.id.escenario, new PermisosFragment()).commit();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()){
                        goLogInScreen();
                    }else {
                        Toast.makeText(MainActivity.this, R.string.NOCERRO, Toast.LENGTH_SHORT).show();
                    }}
            });

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result= opr.get();
            handleSignInResult(result);
        }else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }


    }
    private void handleSignInResult(GoogleSignInResult result) {
        final FirebaseUser userF = mAuth.getCurrentUser();
        if(result.isSuccess()){
            GoogleSignInAccount account= result.getSignInAccount();
            //PONER AQUI A DONDE VA A PONER EL TEXTO
            if (userF !=null) {
                Usuario.setText("Averiguar Solucion");
                Email.setText(userF.getEmail());

            }else {
                Usuario.setText(account.getDisplayName());
                Email.setText(account.getEmail());
                Glide.with(this).load(account.getPhotoUrl()).into(Fusuario);
            }
        }else {
            goLogInScreen();

        }
    }

    private void goLogInScreen() {
        Intent intent= new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
