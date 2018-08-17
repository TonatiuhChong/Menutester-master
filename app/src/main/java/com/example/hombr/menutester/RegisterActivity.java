package com.example.hombr.menutester;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int CHOOSE_IMAGE =123 ;
    private FirebaseAuth mAuth;
    private EditText usuario, password,password2,email;
    private ProgressDialog progressDialog;
    private ImageView foto;
    private Uri uriProfileImage;
    String profileImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        usuario = (EditText) findViewById(R.id.Rusuario);
        email = (EditText) findViewById(R.id.REmail);
        password = (EditText) findViewById(R.id.Password);
        password2 = (EditText) findViewById(R.id.Password2);
        foto= (ImageView)findViewById(R.id.Foto);
        findViewById(R.id.Foto).setOnClickListener(this);
        findViewById(R.id.Registrar).setOnClickListener(this);
        findViewById(R.id.Login).setOnClickListener(this);
        findViewById(R.id.RefPassword).setOnClickListener(this);
        findViewById(R.id.RefPassword2).setOnClickListener(this);


        progressDialog = new ProgressDialog(this);

    }

    private void registerUser(){
        final String Email=email.getText().toString().trim();
        final String User=usuario.getText().toString().trim();
        final String Password= password.getText().toString().trim();
        String Password2= password2.getText().toString().trim();
        //****PONER VALIDACIONES DE EMAIL Y CONTRASEÑAS

        if(TextUtils.isEmpty(Email)){
            email.setError("Please enter a valid email");
            email.requestFocus();
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }

        if (User.isEmpty()) {
            usuario.setError("Escriba su usuario");
            usuario.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(Password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        if (Password.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if (Password.length() < 6) {
            password.setError("Minimum lenght of password should be 6");
            password.requestFocus();
            return;
        }

        //creating a new user
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //checking if success
                        if(task.isSuccessful()){
                            CUser user = new CUser(
                                    User,
                                    Email,
                                    Password
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Registro completo", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                    } else {
                                        //display a failure message
                                        Toast.makeText(RegisterActivity.this, "Que peddooooo", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else{
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                        progressDialog.dismiss();
                    }
                });

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Registrar:
                registerUser();
                break;
            case  R.id.Login:
                finish();
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.Foto:
                showImageChooser();
                break;
            case R.id.RefPassword:
                AlertDialog.Builder pass= new AlertDialog.Builder(this);
                pass.setTitle("Introduce algo");

                final EditText entrada= new EditText(this);
                pass.setView(entrada);
                pass.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });
                break;

        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                foto.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");

        if (uriProfileImage != null) {

            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}

