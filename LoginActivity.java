package com.example.adammoyer.androiduiandlogin_adammoyer;

import android.content.Intent;
import android.service.autofill.UserData;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<UserProfile> userProfiles;
    private PersistenceUserProfile persistenceUserProfile;
    private FirebaseAuth mAuth;

    private EditText usernameEditText = null;
    private EditText passwordEditText = null;
    private Button loginButton = null;
    private Button signupButton = null;
    private TextView orTextView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        usernameEditText = (EditText) findViewById(R.id.editText);
        passwordEditText = (EditText) findViewById(R.id.editText2);
        loginButton = (Button) findViewById(R.id.button);
        signupButton = (Button) findViewById(R.id.button2);
        orTextView = (TextView) findViewById(R.id.textView2);

        loginButton.setOnClickListener(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        persistenceUserProfile = new PersistenceUserProfile(this);
        userProfiles = persistenceUserProfile.getDataFromDB();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button: validateInput(); break;
            case R.id.button2: signUp(); break;
        }
    }

    private void validateInput() {

        UserProfile userProfile = null;

        if(userProfiles != null && !userProfiles.isEmpty()){

            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            for (UserProfile up : userProfiles){
                if(up.getUsername().equals(username) ) {
                    userProfile = up;
                    break;
                }
            }
            if(userProfile == null){
                Toast.makeText(getApplicationContext(), "User Not Found", Toast.LENGTH_LONG).show();
            }else {
                if(!userProfile.getPassword().equals(password)){
                    Toast.makeText(getApplicationContext(), "Password is Incorrect", Toast.LENGTH_LONG).show();
                }else{
                    loginSuccess(userProfile);
                }
            }
        }
    }

    private void signUp(){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void signIn(String email, String password){

        mAuth = FirebaseAuth.getInstance();

        // Tries to sign in a user with the given email address and password.
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // If sign is sucessful, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfile userProfile = null;

                            for (UserProfile up : userProfiles){
                                if(up.getEmail().equals(user.getEmail()) ) {
                                    userProfile = up;
                                    break;
                                }
                            }

                            loginSuccess(userProfile);
                        } else {
                            validateInput();
                        }
                    }
                });
    }

    private void loginSuccess(UserProfile userInfo) {
        Intent intent = new Intent(LoginActivity.this, LoginSuccessActivity.class);

        String fullname = userInfo.getFirstName() + " " + userInfo.getLastName();
        intent.putExtra("FULL_NAME",fullname);
        intent.putExtra("USERNAME", userInfo.getUsername());

        startActivity(intent);
    }
}
