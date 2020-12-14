package com.mariyan.eshop;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private Button add;
    private Button edit;
    private Button delete;

    private TextView res;
    private EditText totalCostText;
    private Integer productRow =-1;

    public static List updatedProducts = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        String name = User.list.get(0).getUsername();
        BigDecimal cash = User.list.get(0).getCash();

        TextView tv1 = (TextView)findViewById(R.id.usernameTextView);
        tv1.setText("Welcome "+name+"! You have "+cash+" $");

        totalCostText=findViewById(R.id.totalCostPlainText);

        ShowAllProducts();
        final ListView productsList=ShowAllProducts();

        productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                productRow =position;
            }
        });
        add =findViewById(R.id.AddButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openAddActivity();
            }
        });

        edit =findViewById(R.id.EditButton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productRow == -1) {
                    Toast.makeText(getApplicationContext(), "No product chosen!", Toast.LENGTH_LONG).show();
                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Empty field!")
                            .setContentText("No product chosen!")
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                } else {
                    openEditActivity();
                }
            }
        });

        delete = findViewById(R.id.DeleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productRow == -1) {
                    Toast.makeText(getApplicationContext(), "No product chosen!", Toast.LENGTH_LONG).show();
                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Empty field!")
                            .setContentText("No product chosen!")
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                } else {
                    String productToDelete=Product.list.get(productRow).getName();

                    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "productsOpit1.db", null);
                    db.delete("productsOpit1", "name=?", new String[]{String.valueOf(productToDelete)});
                    Toast.makeText(getApplicationContext(), "Product deleted successful!", Toast.LENGTH_LONG).show();
                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Empty field!")
                            .setContentText("Product deleted successful!")
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ListView ShowAllProducts() {
        res = findViewById(R.id.result);
        final ListView simpleList = findViewById(R.id.simpleListView);
        ArrayList<String> listResults = new ArrayList<>();

        for(int i = 0; i<Product.list.size(); i++) {
            listResults.add(Product.list.get(i).getName()+" Cost: "+Product.list.get(i).getCost());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_list_view,
                R.id.result,
                listResults
        );
        simpleList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        return simpleList;
    }

    private void openAddActivity() {
        Intent intent=new Intent(getApplicationContext(),AddActivity.class);
        startActivityForResult(intent,1);
    }
    private void openEditActivity() {
        Intent intent=new Intent(getApplicationContext(),EditActivity.class);
        intent.putExtra("productName", Product.list.get(productRow).getName());
        intent.putExtra("productRow", productRow);
        startActivityForResult(intent,2);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onResume() {
        super.onResume();
        ShowAllProducts();
    }

    protected void onDestroy() {
        super.onDestroy();
        if(!updatedProducts.isEmpty())
        {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(new File(getFilesDir().getPath() + "/" + "productsOpit1.db"), null);
            ContentValues cv = new ContentValues();
            Integer j;
            for(int i = 0; i< updatedProducts.size(); i++)
            {
                j= (Integer) updatedProducts.get(i);
                cv.put("name",Product.list.get(j).getName());
                cv.put("cost",Product.list.get(j).getCost().toString());
                db.update("productsOpit1", cv, "ID=?", new String[]{(++j).toString()});
            }
        }
    }
}
