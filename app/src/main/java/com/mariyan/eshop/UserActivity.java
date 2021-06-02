package com.mariyan.eshop;
import android.app.Notification;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
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

public class UserActivity extends AppCompatActivity {
    private Button add;
    private Button remove;
    private Button buy;

    private TextView res;
    private TextView tv1;
    private EditText totalCostText;
    private BigDecimal totalCost = new BigDecimal(0);

    private Integer productID=-1;
    private Integer userID;
    private String name;
    private String password;
    private BigDecimal cash=new BigDecimal(0);
    private String address;

    public static List updatedProducts = new ArrayList<>();
    ArrayList<Integer> cardProducts = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userID=Integer.valueOf(getIntent().getIntExtra("userID",0))-1;

        name = User.list.get(userID).getUsername();
        password = User.list.get(userID).getPassword();
        cash = User.list.get(userID).getCash();
        address = User.list.get(userID).getAddress();

        tv1 = findViewById(R.id.usernameTextView);
        tv1.setText("Welcome "+name+"! You have "+cash+" $");

        totalCostText=findViewById(R.id.totalCostPlainText);

        ShowAllProducts();
        final ListView productsList=ShowAllProducts();

        productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                productID=position;
            }
        });

        add=findViewById(R.id.AddToCardButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productID == -1) {
                    Toast.makeText(getApplicationContext(), "No product chosen!", Toast.LENGTH_LONG).show();
                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Empty field!")
                            .setContentText("No product chosen!")
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                }
                else {
                    totalCost = totalCost.add(Product.list.get(productID).getCost());
                    totalCostText.setText(String.valueOf(totalCost));
                    cardProducts.add(productID);
                } }
        });

        remove =findViewById(R.id.RemoveFromCardButton);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productID == -1) {
                    Toast.makeText(getApplicationContext(), "No product chosen!", Toast.LENGTH_LONG).show();
                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Empty field!")
                            .setContentText("No product chosen!")
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                }
                else {
                    if(cardProducts.contains(productID)) {
                        BigDecimal cost = Product.list.get(productID).getCost();
                        totalCost=totalCost.subtract(cost);
                        totalCostText.setText(String.valueOf(totalCost));
                        cardProducts.remove(Integer.valueOf(productID));
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "You don't have this product in your card!", Toast.LENGTH_LONG).show();
                        Notification notify = new Notification.Builder(getApplicationContext())
                                .setContentTitle("Empty field!")
                                .setContentText("No product chosen!")
                                .build();
                        notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    }
                } }});

        buy = findViewById(R.id.BuyButton);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalCost.compareTo(BigDecimal.ZERO) == 0)
                {
                    Toast.makeText(getApplicationContext(), "You didn't choose any products!!", Toast.LENGTH_LONG).show();
                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Empty field!")
                            .setContentText("No product chosen!")
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                }else if(totalCost.compareTo(cash)>0)
                {
                    Toast.makeText(getApplicationContext(), "Not enough money!!", Toast.LENGTH_LONG).show();
                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Empty field!")
                            .setContentText("No product chosen!")
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                }
                else{
                    cash = cash.subtract(totalCost);
                    tv1.setText("Welcome "+name+"! You have "+cash+" $");
                    totalCost = new BigDecimal(0);
                    totalCostText.setText("0");
                    cardProducts.clear();

                    Toast.makeText(getApplicationContext(), "Order made!", Toast.LENGTH_LONG).show();
                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Empty field!")
                            .setContentText("No product chosen!")
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;

                    Integer id = Integer.valueOf(User.list.size()) + 1;
                    User user = new User(id, name, password, cash,address);
                    User.list.set(userID, user);

                    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(new File(getFilesDir().getPath() + "/" + "usersOpit1.db"), null);
                    ContentValues cv = new ContentValues();
                    cv.put("username", User.list.get(userID).getUsername());
                    cv.put("password", User.list.get(userID).getPassword());
                    cv.put("cash", User.list.get(userID).getCash().toString());
                    cv.put("address", User.list.get(userID).getAddress());
                    db.update("usersOpit1", cv, "username=?", new String[]{name});
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
        return simpleList;
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

    public void updateProductsSQL(String q, SQLiteDatabase db) {
        long count =  DatabaseUtils.queryNumEntries(db, "productsOpit1");
        int count2 = Integer.valueOf((int) count);
        if(Product.list.size()>count) {
            for(int i=count2;i<Product.list.size();i++) {
                q = "INSERT INTO productsOpit1(name,cost) VALUES(?,?);";
                db.execSQL(q, new Object[]{Product.list.get(i).getName(), Product.list.get(i).getCost()});
            }
        }
        db.close();
    }

}
