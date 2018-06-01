package thedark.example.com.thefoodhouse_client.Order;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import thedark.example.com.thefoodhouse_client.Common.Common;
import thedark.example.com.thefoodhouse_client.Model.OrderDetails;
import thedark.example.com.thefoodhouse_client.R;
import thedark.example.com.thefoodhouse_client.ViewHolder.OrderDetailsAdapter;

public class OrderDetailsActivity extends AppCompatActivity {
    private ArrayList<OrderDetails> orderDetails;
    private FirebaseDatabase database;
    private DatabaseReference requests;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private OrderDetailsAdapter orderDetailsAdapter;
    private String keyOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        recyclerView = (RecyclerView) findViewById(R.id.listOrdersDetail);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance();
        if (getIntent() != null) {
            keyOrder = getIntent().getStringExtra("KeyOrder");
        }
        requests = database.getReference("Requests").child(Common.currentUser.getPhone()).child(keyOrder).child("foods");
        orderDetails = new ArrayList<>();
        loadOrderDetails();
        orderDetailsAdapter = new OrderDetailsAdapter(orderDetails, getApplicationContext());
    }

    private void loadOrderDetails() {
        requests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {

                    OrderDetails orderdetail = new OrderDetails();
                    String discount = (String) messageSnapshot.child("discount").getValue();
                    String image = (String) messageSnapshot.child("img").getValue();
                    String price = (String) messageSnapshot.child("price").getValue();
                    String productId = (String) messageSnapshot.child("productId").getValue();
                    String productName = (String) messageSnapshot.child("productName").getValue();
                    String quantity = (String) messageSnapshot.child("quantity").getValue();

                    orderdetail.setDiscountDetails(discount);
                    orderdetail.setImgDetails(image);
                    orderdetail.setPriceDetails(price);
                    orderdetail.setProductNameDetails(productName);
                    orderdetail.setProductIdDetails(productId);
                    orderdetail.setQuantityDetails(quantity);

                    orderDetails.add(orderdetail);
                    recyclerView.setAdapter(orderDetailsAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
