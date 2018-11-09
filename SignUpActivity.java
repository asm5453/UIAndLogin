package com.example.adammoyer.androiduiandlogin_adammoyer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private PersistenceUserProfile persistenceUserProfile;
    private EditText firstNameEditText = null;
    private EditText lastNameEditText = null;
    private EditText userNameEditText = null;
    private EditText birthdayEditText = null;
    private EditText phoneNumberEditText = null;
    private EditText emailEditText = null;
    private EditText passwordEditText = null;
    private Button backButton = null;
    private Button confirmButton = null;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        firstNameEditText = (EditText) findViewById(R.id.editText3);
        lastNameEditText = (EditText) findViewById(R.id.editText4);
        userNameEditText = (EditText) findViewById(R.id.editText5);
        birthdayEditText = (EditText) findViewById(R.id.editText6);
        phoneNumberEditText = (EditText) findViewById(R.id.editText7);
        emailEditText = (EditText) findViewById(R.id.editText8);
        passwordEditText = (EditText) findViewById(R.id.editText9);
        backButton = (Button) findViewById(R.id.button4);
        confirmButton = (Button) findViewById(R.id.button3);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 signUp(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        persistenceUserProfile = new PersistenceUserProfile(this);
    }

    private void signUpLocally() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String username = userNameEditText.getText().toString();
        String birthday = birthdayEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        UserProfile user = new UserProfile(firstName, lastName,
                username, birthday, phoneNumber, email, password);

        persistenceUserProfile.insert(user);
    }

    private void signUp(String email, String password){

        /*
         * The entry point of the Firebase Authentication SDK.
         * Obtain an instance of this class by calling getInstance()
         */
        //mAuth = FirebaseAuth.getInstance();


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    signUpLocally();
                    Toast.makeText(getApplicationContext(), "User Successfully Created", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    // TODO
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmailAndPassword:failure", task.getException());
                    Toast.makeText(SignUpActivity.this, "Authentication failed "+task.getException().getMessage() ,
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
