package com.mariyan.eshop;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;
    public static List updatedUsers = new ArrayList<>();
//bratineshaha
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openLoginUser();
            }
        });

        registerButton = findViewById(R.id.RegisterButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterUser();
            }
        });

        TakeUsersFromSQL();
        TakeProductsFromSQL();
    }
    protected void onDestroy() {
        super.onDestroy();
        if(!updatedUsers.isEmpty())
        {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(new File(getFilesDir().getPath() + "/" + "usersOpit1.db"), null);
            ContentValues cv = new ContentValues();
            Integer j;
            for(int i = 0; i< updatedUsers.size(); i++)
            {
                j= (Integer) updatedUsers.get(i);
                cv.put("username",User.list.get(j).getUsername());
                cv.put("password",User.list.get(j).getPassword());
                cv.put("cash",  User.list.get(j).getCash().toString());
                cv.put("address", User.list.get(j).getAddress());
                db.update("usersOpit1", cv, "ID=?", new String[]{(++j).toString()});
            }
        }
        if(!User.list.isEmpty()) {
            User.list.clear();
        }
        if(!Product.list.isEmpty()){
            Product.list.clear();
        }
    }
    private void openRegisterUser() {
        Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
    }
    private void openLoginUser() {
        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }
    public void TakeUsersFromSQL() {
        String q="";

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "usersOpit1.db", null);
        createTableIfNotExistsUsers(q,db);

        q = "SELECT * FROM usersOpit1";
        Cursor c = db.rawQuery(q, null);
        while (c.moveToNext()) {
            Integer id = c.getInt(c.getColumnIndex("ID"));
            String username = c.getString(c.getColumnIndex("username"));
            String password = c.getString(c.getColumnIndex("password"));
            Double cash = c.getDouble(c.getColumnIndex("cash"));
            BigDecimal cash2 = BigDecimal.valueOf(cash);
            String address = c.getString(c.getColumnIndex("address"));
            User user = new User(id, username, password, cash2,address);
            User.list.add(user);
        }
        c.close();
        db.close();
    }
    public void TakeProductsFromSQL() {
        String q="";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "productsOpit1.db", null);
        createTableIfNotExistsProducts(q,db);
        q = "SELECT * FROM productsOpit1";
        Cursor c = db.rawQuery(q, null);
        while (c.moveToNext()) {
            Integer id = c.getInt(c.getColumnIndex("ID"));
            String name = c.getString(c.getColumnIndex("name"));
            Double cost = c.getDouble(c.getColumnIndex("cost"));
            BigDecimal cost2 = BigDecimal.valueOf(cost);
            Product product = new Product(id, name, cost2);
            Product.list.add(product);
        }
        c.close();
        db.close();
    }
    public void createTableIfNotExistsUsers(String q, SQLiteDatabase db) {
        q = "CREATE TABLE if not exists usersOpit1(";
        q += "ID integer primary key AUTOINCREMENT, ";
        q += "username text unique not null, ";
        q += "password text not null, ";
        q += "cash double not null, ";
        q += "address text not null)";
        db.execSQL(q);
    }
    public void createTableIfNotExistsProducts(String q, SQLiteDatabase db) {
        q = "CREATE TABLE if not exists productsOpit1(";
        q += "ID integer primary key AUTOINCREMENT, ";
        q += "name text unique unique not null, ";
        q += "cost text not null)";
        db.execSQL(q);

    }
}
