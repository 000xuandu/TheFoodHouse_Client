package thedark.example.com.thefoodhouse_client.ViewHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import thedark.example.com.thefoodhouse_client.Cart.CartActivity;
import thedark.example.com.thefoodhouse_client.Database.Database;
import thedark.example.com.thefoodhouse_client.Model.Food;
import thedark.example.com.thefoodhouse_client.Model.Order;
import thedark.example.com.thefoodhouse_client.R;

public class CartViewAdapter extends RecyclerView.Adapter<CartViewAdapter.MyViewHolder> {
    // This object helps you save/restore the open/close state of each view
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private List<Order> listData;
    private Context context;
    private RecyclerView rv;
    private int mPosition;


    public CartViewAdapter(List<Order> listData, Context context, RecyclerView rv) {
        this.listData = listData;
        this.context = context;
        this.rv = rv;
    }

    public CartViewAdapter() {
        // uncomment the line below if you want to open only one row at a time
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @NonNull
    @Override
    public CartViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cart_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final MyViewHolder h = (MyViewHolder) holder;
        final Order order = listData.get(position);
        // Save/restore the open/close state.
        // You need to provide a String id which uniquely defines the data object.
        viewBinderHelper.bind(holder.swipeRevealLayout, order.getID());
        h.bind(order.getID(), rv);

        holder.txt_cart_count.setText(listData.get(position).getQuantity());

        int price = Integer.parseInt(listData.get(position).getPrice());
        final int count = Integer.parseInt(listData.get(position).getQuantity());
        final int priceTotal = count * price;
        holder.txt_cart_price.setText(String.valueOf(priceTotal));

        holder.txt_cart_name.setText(listData.get(position).getProductName());

        //OnClick to delete item cart:
//        holder.cardCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//                alertDialog.setTitle("Delete a food");
//                alertDialog.setMessage("Are you sure you want to delete this dish?");
//                alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
//                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //Delete item:
//                        new Database(context).deleteItemCart(Integer.parseInt(listData.get(position).getID()));
//                        //Code: Reset Data when i want to delete one item in listData (Cart):
//                        CartViewAdapter.this.notifyItemRemoved(position);
//                        CartViewAdapter.this.notifyItemRangeChanged(position, listData.size());
//                        listData.remove(listData.get(position));
//                        CartViewAdapter.this.notifyDataSetChanged();
//                        //Call loadListFoodOrder()
//                        ((CartActivity) context).loadListFoodOrder();
//                    }
//                });
//                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                alertDialog.show();
//
//            }
//        });

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

        holder.img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int countTemp = Integer.parseInt(holder.txt_cart_count.getText().toString());
                if (countTemp == 0) {
                    holder.img_minus.setEnabled(false);
                } else {
                    holder.img_minus.setEnabled(true);
                    int countMinus = countTemp - 1;
                    holder.img_minus.setEnabled(true);
                    holder.txt_cart_count.setText(String.valueOf(countMinus));
                    int price = Integer.parseInt(listData.get(mPosition).getPrice());
                    final int count = countMinus;
                    final int priceTotal = count * price;
                    holder.txt_cart_price.setText(String.valueOf(priceTotal));
                    ((CartActivity) context).loadTotalMoneyFoodOrderUpdate(priceTotal);
                    new Database(context).updateCart(listData.get(mPosition).getID(), String.valueOf(countMinus));
                    order.setQuantity(String.valueOf(countMinus));
                    listData.set(mPosition, order);
                    Toast.makeText(context, "" + listData.get(mPosition).getQuantity(), Toast.LENGTH_SHORT).
                            show();
                }
            }
        });

        holder.img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int countTemp = Integer.parseInt(holder.txt_cart_count.getText().toString());
                if (countTemp == 0) {
                    holder.img_minus.setEnabled(false);
                } else {
                    holder.img_minus.setEnabled(true);
                }
                int countPlus = countTemp + 1;
                holder.txt_cart_count.setText(String.valueOf(countPlus));
                int price = Integer.parseInt(listData.get(mPosition).getPrice());
                final int count = countPlus;
                final int priceTotal = count * price;
                holder.txt_cart_price.setText(String.valueOf(priceTotal));
                ((CartActivity) context).loadTotalMoneyFoodOrderUpdate(priceTotal);
                new Database(context).updateCart(listData.get(mPosition).getID(), String.valueOf(countPlus));
                order.setQuantity(String.valueOf(countPlus));
                listData.set(mPosition, order);
                Toast.makeText(context, "" + listData.get(mPosition).getQuantity(), Toast.LENGTH_SHORT).
                        show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public void saveStates(Bundle outState) {
        viewBinderHelper.saveStates(outState);

    }

    public void restoreStates(Bundle inState) {
        viewBinderHelper.restoreStates(inState);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_cart_name, txt_cart_price, txt_cart_count;
        public ImageView img_cart, img_plus, img_minus;
        public CardView cardCart;
        public View viewBackground, view_foreground, deleteLayout;
        public SwipeRevealLayout swipeRevealLayout;
        //FIREBASE:
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference table_food;

        public MyViewHolder(View itemView) {
            super(itemView);
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            table_food = mFirebaseDatabase.getReference("Foods");
            txt_cart_name = (TextView) itemView.findViewById(R.id.cart_item_name);
            txt_cart_price = (TextView) itemView.findViewById(R.id.cart_item_price);
            txt_cart_count = (TextView) itemView.findViewById(R.id.cart_item_count);
            img_cart = (ImageView) itemView.findViewById(R.id.cart_item_img);
            img_plus = (ImageView) itemView.findViewById(R.id.btnPlus);
            img_minus = (ImageView) itemView.findViewById(R.id.btnMinus);
            cardCart = (CardView) itemView.findViewById(R.id.cardCart);
            viewBackground = itemView.findViewById(R.id.viewBackground);
            view_foreground = itemView.findViewById(R.id.view_foreground);
            swipeRevealLayout = itemView.findViewById(R.id.swipeRevealLayout);
            deleteLayout = itemView.findViewById(R.id.delete_layout);

        }


        public void bind(final String data, RecyclerView rv) {
            mPosition = getAdapterPosition();
            //Restore item:
            final Snackbar snackbar = Snackbar
                    .make(rv, listData.get(getAdapterPosition()).getProductName() + " " + "has just been deleted.", Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.setAction("Restore", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Database(context).addToCart(listData.get(mPosition));
                    //Call loadListFoodOrder()
                    CartViewAdapter.this.notifyDataSetChanged();
                    ((CartActivity) context).loadTotalMoneyFoodOrder();
                }
            })
                    .setActionTextColor(Color.GREEN);
            sbView.setBackgroundColor(context.getResources().getColor(R.color.backroundSnackbar));

            //Delete item:
            deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Delete a food");
                    alertDialog.setMessage("Are you sure you want to delete this dish?");
                    alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new Database(context).deleteItemCart(Integer.parseInt(listData.get(mPosition).getID()));
                            //Code: Reset Data when i want to delete one item in listData (Cart):
                            //Call loadListFoodOrder()
                            ((CartActivity) context).loadTotalMoneyFoodOrder();
                            ((CartActivity) context).checkSizeCart();
                            snackbar.show();
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
        }
    }
}


