package com.mariyan.eshop;
import android.app.Notification;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.math.BigDecimal;

public class RegisterActivity extends AppCompatActivity {
    private TextView username;
    private TextView password;
    private TextView confirmPass;
    private TextView address;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = findViewById(R.id.RegisterButton);
        register.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerUser() {
        username = findViewById(R.id.usernamePlainText);
        password = findViewById(R.id.passwordPlainText);
        confirmPass = findViewById(R.id.confirmPasswordPlainText);
        address = findViewById(R.id.addressPlainText);

        String name = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String conPass = confirmPass.getText().toString().trim();
        String addr = address.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Username should not be empty!", Toast.LENGTH_LONG).show();
            Notification notify = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Empty field!")
                    .setContentText(name)
                    .build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
        } else if(pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Password should not be empty!", Toast.LENGTH_LONG).show();
            Notification notify = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Empty field!")
                    .setContentText(name)
                    .build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
        } else if(addr.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Address should not be empty!", Toast.LENGTH_LONG).show();
            Notification notify = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Empty field!")
                    .setContentText(name)
                    .build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
        } else if(!conPass.equals(pass)) {
            Toast.makeText(getApplicationContext(), "Passwords don't match!", Toast.LENGTH_LONG).show();
            Notification notify = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Passwords don't match!")
                    .setContentText(name)
                    .build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
        } else {
            boolean flag = false;
            for (User user : User.list) {
                if (user.getUsername().equals(name)) {
                    flag = true;
                    break;
                }
            }
            if (flag == true) {
                Toast.makeText(getApplicationContext(), "Username taken!", Toast.LENGTH_LONG).show();
                Notification notify = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Username!")
                        .setContentText(name)
                        .build();
                notify.flags |= Notification.FLAG_AUTO_CANCEL;
            } else {
                try {
                    Integer id = Integer.valueOf(User.list.size()) + 1;
                    User user = new User(id, name, pass, BigDecimal.valueOf(200.0),addr);
                    User.list.add(user);
                    String q = "";
                    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "usersOpit1.db", null);
                    q = "INSERT INTO usersOpit1(username,password,cash,address) VALUES(?,?,?,?);";
                    db.execSQL(q, new Object[]{name,pass,100,addr});
                    db.close();

                    Toast.makeText(getApplicationContext(), "User created successful!", Toast.LENGTH_LONG).show();
                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle("User created successful")
                            .setContentText(name)
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    finish();
                } catch (SQLiteException e) {
                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Error while working with database!")
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                } catch (Exception e) {
                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Error while working with database!")
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                }
            }
        }
    }
}