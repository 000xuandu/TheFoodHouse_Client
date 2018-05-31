package thedark.example.com.thefoodhouse_client.Food;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    ArrayList<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

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

        //Search
        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter Your Food");
        loadSuggest(); //write function loadSuggest from Firebase:
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //When user type their text, we will change suggest list:
                List<String> suggest = new ArrayList<>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When SearchBar is close
                //Restore original adapter
                if (!enabled) {
                    recycler_food.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //When search finish
                //Show result of search adapter
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("Name").equalTo(text.toString()) //Compare name;
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, final int position) {
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
                        moveToFoodDetails.putExtra("FoodId", searchAdapter.getRef(position).getKey());
                        // Send FoodId to FoodDetailsActivity
                        moveToFoodDetails.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(moveToFoodDetails);
                    }
                });
            }
        };
        recycler_food.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        foodList.orderByChild("MenuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Food item = postSnapshot.getValue(Food.class);
                            suggestList.add(item.getName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
                        startActivity(moveToFoodDetails);
                    }
                });
            }
        };
        recycler_food.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity(5);
    }
}
