package thedark.example.com.thefoodhouse_client.ViewHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import thedark.example.com.thefoodhouse_client.Cart.CartActivity;
import thedark.example.com.thefoodhouse_client.Database.Database;
import thedark.example.com.thefoodhouse_client.Interface.ItemClickListener;
import thedark.example.com.thefoodhouse_client.Model.Food;
import thedark.example.com.thefoodhouse_client.Model.Order;
import thedark.example.com.thefoodhouse_client.R;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txt_cart_name, txt_cart_price, txt_cart_count;
    public ImageView img_cart;
    public CardView cardCart;

    //FIREBASE:
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference table_food;
    private ItemClickListener itemClickListener;

    public CartViewHolder(View itemView) {
        super(itemView);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        table_food = mFirebaseDatabase.getReference("Foods");

        txt_cart_name = (TextView) itemView.findViewById(R.id.cart_item_name);
        txt_cart_price = (TextView) itemView.findViewById(R.id.cart_item_price);
        txt_cart_count = (TextView) itemView.findViewById(R.id.cart_item_count);
        img_cart = (ImageView) itemView.findViewById(R.id.cart_item_img);
        cardCart = (CardView) itemView.findViewById(R.id.cardCart);
    }


    @Override
    public void onClick(View view) {

    }
}

public class CartViewAdapter extends RecyclerView.Adapter<CartViewHolder> {
    private List<Order> listData;
    private Context context;

    public CartViewAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txt_cart_count.setText(listData.get(position).getQuantity());

        Locale locale = new Locale("en", "US");
        final NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        int count = Integer.parseInt(listData.get(position).getQuantity());
        int price = Integer.parseInt(listData.get(position).getPrice());
        final int priceTotal = count * price;
        holder.txt_cart_price.setText(numberFormat.format(priceTotal));
        holder.txt_cart_name.setText(listData.get(position).getProductName());

        holder.cardCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Delete a food");
                alertDialog.setMessage("Are you sure you want to delete this dish?");
                alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Delete item:
                        new Database(context).deleteItemCart(Integer.parseInt(listData.get(position).getID()));
                        //Code: Reset Data when i want to delete one item in listData (Cart):
                        CartViewAdapter.this.notifyItemRemoved(position);
                        CartViewAdapter.this.notifyItemRangeChanged(position, listData.size());
                        listData.remove(listData.get(position));
                        CartViewAdapter.this.notifyDataSetChanged();
                        //Call loadListFoodOrder()
                        ((CartActivity)context).loadListFoodOrder();
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
        });

        //Set Image
        holder.table_food.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Food food = dataSnapshot.child(listData.get(position).getProductId()).getValue(Food.class);
                Picasso.get().load(food.getImage()).into(holder.img_cart);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
