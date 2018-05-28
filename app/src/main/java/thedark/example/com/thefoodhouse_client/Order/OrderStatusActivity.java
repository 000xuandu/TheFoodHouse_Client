package thedark.example.com.thefoodhouse_client.Order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import thedark.example.com.thefoodhouse_client.Common.Common;
import thedark.example.com.thefoodhouse_client.Interface.ItemClickListener;
import thedark.example.com.thefoodhouse_client.Model.Request;
import thedark.example.com.thefoodhouse_client.R;
import thedark.example.com.thefoodhouse_client.ViewHolder.OrderViewHolder;

public class OrderStatusActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference requests;
    private FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Firebase:
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //init:
        recyclerView = (RecyclerView) findViewById(R.id.recycler_orderview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());
    }

    private void loadOrders(final String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_item,
                OrderViewHolder.class,
                requests.child(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(OrderStatusActivity.this, adapter.getRef(position).getKey() + " OK", Toast.LENGTH_SHORT).show();
//                        Get CategoryID and send Activity Foods List:
                        Intent moveToOrderDetails = new Intent(OrderStatusActivity.this, OrderDetailsActivity.class);
                        moveToOrderDetails.putExtra("KeyOrder", adapter.getRef(position).getKey());
                        startActivity(moveToOrderDetails);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private String convertCodeToStatus(String status) {
        switch (status) {
            case "0":
                return "Place";
            case "1":
                return "On my way";
            default:
                return "Shipped";
        }
    }
}
