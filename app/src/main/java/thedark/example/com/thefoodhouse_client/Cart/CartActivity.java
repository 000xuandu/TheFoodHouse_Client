package thedark.example.com.thefoodhouse_client.Cart;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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
    RelativeLayout rootLayout;

    List<Order> cart = new ArrayList<>();
    CartViewAdapter cartViewAdapter;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    EditText edtAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase:
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //Init:
        recyclerView = findViewById(R.id.listCart);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        txtTotal = findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);
        rootLayout = findViewById(R.id.rootLayout);

        getSizeCart();
        checkSizeCart();

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getSizeCart();
                if (cart.size() == 0) {
                    Toast.makeText(CartActivity.this, "My cart is empty", Toast.LENGTH_SHORT).show();
                } else {
                    showAlertDialog();
                }
            }
        });
        loadTotalMoneyFoodOrder();
    }

    public List<Order> getSizeCart() {
        return cart = new Database(CartActivity.this).getCarts(Common.currentUser.getPhone());
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
        alertDialog.setTitle("One more step");
        alertDialog.setMessage("Enter your address");

        edtAddress = new EditText(CartActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(lp);
        edtAddress.setHint("Your address");
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //get Address:
        edtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CartActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                        .setCountry("VN")
                        .build();
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            .build(CartActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });


        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Create new Request:
                if (edtAddress.getText().toString().equals("")) {
                    Toast.makeText(CartActivity.this, "Please enter your address", Toast.LENGTH_SHORT).show();
                } else {
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
                    Toast.makeText(CartActivity.this, "Thank you, Order successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                edtAddress.setText(place.getAddress());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void loadTotalMoneyFoodOrder() {
        getSizeCart();
        cartViewAdapter = new CartViewAdapter(cart, this, recyclerView);
        //Calculate total price:
        int total = 0;
        for (Order order : cart) {
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
            Locale locale = new Locale("en", "US");
            NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
            txtTotal.setText(numberFormat.format(total));
        }
        cartViewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(cartViewAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSizeCart();
    }

    public void checkSizeCart() {
        getSizeCart();
        if (cart.size() == 0) {
            txtTotal.setText("0");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (cartViewAdapter != null) {
            cartViewAdapter.saveStates(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (cartViewAdapter != null) {
            cartViewAdapter.restoreStates(savedInstanceState);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity(3);
    }
}
