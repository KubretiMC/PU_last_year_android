package com.mariyan.eshop;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private Integer userID;
    private TextView username;
    private TextView password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.LoginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loginUser() {
        username = findViewById(R.id.usernamePlainText);
        password = findViewById(R.id.passwordPlainText);

        String name = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Username should not be empty!", Toast.LENGTH_LONG).show();
            Notification notify = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Empty field!")
                    .setContentText(name)
                    .build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
        } else if (pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Password should not be empty!", Toast.LENGTH_LONG).show();
            Notification notify = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Empty field!")
                    .setContentText(name)
                    .build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
        } else {
            boolean flag = false;
            for (User user : User.list) {
                if (user.getUsername().equals(name) && user.getPassword().equals(pass)) {
                    userID=user.getId();
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                Toast.makeText(getApplicationContext(), "Username or password wrong!", Toast.LENGTH_LONG).show();
                Notification notify = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Username!")
                        .setContentText(name)
                        .build();
                notify.flags |= Notification.FLAG_AUTO_CANCEL;
            } else {
                openHeroActivity(userID);
            }
        }
    }
    private void openHeroActivity(Integer userID) {
        if(userID==1) {
            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(), UserActivity.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }

    }
}