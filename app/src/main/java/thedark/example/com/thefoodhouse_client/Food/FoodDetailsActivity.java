package thedark.example.com.thefoodhouse_client.Food;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import thedark.example.com.thefoodhouse_client.Database.Database;
import thedark.example.com.thefoodhouse_client.Model.Food;
import thedark.example.com.thefoodhouse_client.Model.Order;
import thedark.example.com.thefoodhouse_client.R;

public class FoodDetailsActivity extends AppCompatActivity {

    TextView food_name, food_description, food_price;
    ImageView food_img;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    FirebaseDatabase database;
    DatabaseReference foods;
    Food currentFood;
    private String FoodId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Firebase:
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        //Init View:
        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        food_name = (TextView) findViewById(R.id.food_name);
        food_description = (TextView) findViewById(R.id.food_description);
        food_price = (TextView) findViewById(R.id.food_price);
        food_img = (ImageView) findViewById(R.id.img_food);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);


        //Get FoodId:
        if (getIntent() != null) {
            FoodId = getIntent().getStringExtra("FoodId");
        }
        if (FoodId != null) {
            getDetailsFood(FoodId);
        }


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getApplicationContext()).addToCart(new Order(
                        "", //Id tự tăng
                        FoodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));
                Toast.makeText(FoodDetailsActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDetailsFood(String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                //Set Img Food:
                Picasso.get()
                        .load(currentFood.getImage())
                        .into(food_img);
                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescription());
                food_price.setText(currentFood.getPrice());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
