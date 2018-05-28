package thedark.example.com.thefoodhouse_client.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import thedark.example.com.thefoodhouse_client.Model.Food;
import thedark.example.com.thefoodhouse_client.Model.OrderDetails;
import thedark.example.com.thefoodhouse_client.R;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    ArrayList<OrderDetails> orderDetails = new ArrayList<>();
    Context context;

    public OrderDetailsAdapter(ArrayList<OrderDetails> orderDetails, Context context) {
        this.orderDetails = orderDetails;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txt_cart_name.setText(orderDetails.get(position).getProductNameDetails());
        holder.txt_cart_price.setText(orderDetails.get(position).getPriceDetails());
        holder.txt_cart_count.setText(orderDetails.get(position).getQuantityDetails());

        holder.table_foods.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Food food = dataSnapshot.child(orderDetails.get(position).getProductIdDetails()).getValue(Food.class);
                Picasso.get().load(food.getImage()).into(holder.img_cart);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Cancle", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_cart_name, txt_cart_price, txt_cart_count;
        public ImageView img_cart;

        //FIREBASE:
        FirebaseDatabase database;
        DatabaseReference table_foods;

        public ViewHolder(View itemView) {
            super(itemView);
            database = FirebaseDatabase.getInstance();
            table_foods = database.getReference("Foods");

            txt_cart_name = (TextView) itemView.findViewById(R.id.cart_item_name);
            txt_cart_price = (TextView) itemView.findViewById(R.id.cart_item_price);
            txt_cart_count = (TextView) itemView.findViewById(R.id.cart_item_count);
            img_cart = (ImageView) itemView.findViewById(R.id.cart_item_img);
        }
    }
}
