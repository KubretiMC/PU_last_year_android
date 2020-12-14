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

public class AddActivity extends AppCompatActivity {

    private TextView productName;
    private TextView productCost;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        add = findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addProduct() {
        productName = findViewById(R.id.productNamePlainText);
        productCost = findViewById(R.id.productCostPlainText);

        String name = productName.getText().toString().trim();
        Double cost = 0.0;
        if (productCost.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Cost should not be empty!", Toast.LENGTH_LONG).show();
            Notification notify = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Empty field!")
                    .setContentText(name)
                    .build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
        } else {
            cost = Double.valueOf(productCost.getText().toString());
        }
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Name should not be empty!", Toast.LENGTH_LONG).show();
            Notification notify = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Empty field!")
                    .setContentText(name)
                    .build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
        } else {
            boolean flag = false;
            for (Product product : Product.list) {
                if (product.getName().equals(name)) {
                    flag = true;
                    break;
                }
            }
            if (flag == true) {
                Toast.makeText(getApplicationContext(), "Product already exists!", Toast.LENGTH_LONG).show();
                Notification notify = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Product already exists!!")
                        .setContentText(name)
                        .build();
                notify.flags |= Notification.FLAG_AUTO_CANCEL;
            } else {

                try {
                    Integer id = Integer.valueOf(Product.list.size()) + 1;
                    BigDecimal cost2 = BigDecimal.valueOf(cost);
                    Product product = new Product(id, name, cost2);
                    Product.list.add(product);

                    String q = "";
                    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "productsOpit1.db", null);

                    //long count = DatabaseUtils.queryNumEntries(db, "geroiOpit1");
                    //int count2 = Integer.valueOf((int) count);
                    q = "INSERT INTO productsOpit1(name,cost) VALUES(?,?);";
                    db.execSQL(q, new Object[]{name, cost2});
                    db.close();

                    Toast.makeText(getApplicationContext(), "Product created successful!", Toast.LENGTH_LONG).show();
                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Product created successful")
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