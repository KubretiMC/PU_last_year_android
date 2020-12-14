package com.mariyan.eshop;
import android.app.Notification;
import android.content.ContentValues;
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
import java.io.File;
import java.math.BigDecimal;

public class EditActivity extends AppCompatActivity {

    private TextView productName;
    private TextView productCost;
    private Button edit;
    private String name;
    private Integer productRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        name = getIntent().getStringExtra("productName");
        productRow = Integer.valueOf(getIntent().getIntExtra("productRow", 0));

        productName = findViewById(R.id.productNamePlainText);
        productCost = findViewById(R.id.productCostPlainText);

        productName.setText(name);

        edit = findViewById(R.id.editButton);
        edit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                editProduct();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void editProduct() {
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
            try {
                Integer id = Integer.valueOf(Product.list.size()) + 1;
                BigDecimal cost2 = BigDecimal.valueOf(cost);
                Product product = new Product(id, name, cost2);
                Product.list.set(productRow, product);

                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(new File(getFilesDir().getPath() + "/" + "productsOpit1.db"), null);
                ContentValues cv = new ContentValues();
                Toast.makeText(getApplicationContext(), "Product updated successful!", Toast.LENGTH_LONG).show();
                Notification notify = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Product created successful")
                        .setContentText(name)
                        .build();
                notify.flags |= Notification.FLAG_AUTO_CANCEL;

                cv.put("name", Product.list.get(productRow).getName());
                cv.put("cost", Product.list.get(productRow).getCost().toString());
                db.update("productsOpit1", cv, "name=?", new String[]{name});


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