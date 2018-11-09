package com.example.adammoyer.androiduiandlogin_adammoyer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginSuccessActivity extends AppCompatActivity {
    private Button viewAllUsersButton = null;
    private ArrayList<UserProfile> userProfiles;
    private PersistenceUserProfile persistenceUserProfile;
    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        welcomeTextView = (TextView) findViewById(R.id.textView2);

        Intent intent = getIntent();
        String fullName = intent.getStringExtra("FULL_NAME");
        Toast.makeText(getApplicationContext(), fullName, Toast.LENGTH_LONG).show();
        String userName = intent.getStringExtra("USERNAME");
        Toast.makeText(getApplicationContext(), userName, Toast.LENGTH_LONG).show();

        welcomeTextView.setText("Welcome " + fullName + "! (" + userName + ")");

        viewAllUsersButton = (Button) findViewById(R.id.button5);

        viewAllUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginSuccessActivity.this, ViewAllUsers.class);
                startActivity(intent);
            }
        });
    }
}
