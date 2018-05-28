package thedark.example.com.thefoodhouse_client.Food;

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
import com.squareup.picasso.Picasso;

import thedark.example.com.thefoodhouse_client.Interface.ItemClickListener;
import thedark.example.com.thefoodhouse_client.Model.Food;
import thedark.example.com.thefoodhouse_client.R;
import thedark.example.com.thefoodhouse_client.ViewHolder.FoodViewHolder;

public class FoodListActivity extends AppCompatActivity {

    RecyclerView recycler_food;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    private String categoryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Init Firebase:
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");

        recycler_food = (RecyclerView) findViewById(R.id.recycler_foods);
        recycler_food.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_food.setLayoutManager(layoutManager);

        //Get Intent (CategoryID) From Home.java:
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryID");
        }

        if (!categoryId.isEmpty()) {
            loadFoodList(categoryId);
        } else {
            Toast.makeText(this, "CategoryID is empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFoodList(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                //like: SELECT * FROM Foods WHERE MenuID = categoryID;
                foodList.orderByChild("MenuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, final Food model, final int position) {
                viewHolder.txtFoodName.setText(model.getName());
                Picasso.get()
                        .load(model.getImage())
                        .into(viewHolder.imageView);

                Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int positon, boolean isLongClick) {
                        //Start Activity FoodDetails
                        Intent moveToFoodDetails = new Intent(getApplicationContext(), FoodDetailsActivity.class);
                        moveToFoodDetails.putExtra("FoodId", adapter.getRef(position).getKey());
                        // Send FoodId to FoodDetailsActivity
                        moveToFoodDetails.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(moveToFoodDetails);
                    }
                });
            }
        };
        recycler_food.setAdapter(adapter);
    }
}
