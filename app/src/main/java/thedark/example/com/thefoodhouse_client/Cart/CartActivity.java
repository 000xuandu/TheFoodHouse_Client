package thedark.example.com.thefoodhouse_client.Cart;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import thedark.example.com.thefoodhouse_client.Common.Common;
import thedark.example.com.thefoodhouse_client.Database.Database;
import thedark.example.com.thefoodhouse_client.Model.Order;
import thedark.example.com.thefoodhouse_client.Model.Request;
import thedark.example.com.thefoodhouse_client.R;
import thedark.example.com.thefoodhouse_client.ViewHolder.CartViewAdapter;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotal;
    Button btnPlace;

    List<Order> cart = new ArrayList<>();
    CartViewAdapter cartViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase:
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //Init:
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotal = findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.size() == 0) {
                    btnPlace.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(CartActivity.this, "My cart is empty", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    showAlertDialog();
                }
            }
        });
        loadListFoodOrder();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
        alertDialog.setTitle("One more step");
        alertDialog.setMessage("Enter your address");

        final EditText edtAddress = new EditText(CartActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Create new Request:
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtAddress.getText().toString(),
                        txtTotal.getText().toString(),
                        cart
                );
                //Submit Firebase:
                //We will using System.CurrentMilli to key
                requests.child(Common.currentUser.getPhone())
                        .child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);
                //Delete Cart:
                new Database(getApplicationContext()).cleanCart();
                Toast.makeText(CartActivity.this, "Thank you, Order Place", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void loadListFoodOrder() {
        cart = new Database(getApplicationContext()).getCarts();
        cartViewAdapter = new CartViewAdapter(cart, this);
        cartViewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(cartViewAdapter);

        if (cart.size() == 0) {
            txtTotal.setText("0$");
        }

        //Calculate total price:
        int total = 0;
        for (Order order : cart) {
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
            Locale locale = new Locale("en", "US");
            NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
            txtTotal.setText(numberFormat.format(total) + " $");
        }
    }
}
