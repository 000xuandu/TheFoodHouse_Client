package thedark.example.com.thefoodhouse_client;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import mehdi.sakout.fancybuttons.FancyButton;
import thedark.example.com.thefoodhouse_client.Authentication.SignInActivity;
import thedark.example.com.thefoodhouse_client.Authentication.SignUpActivity;

public class MainActivity extends AppCompatActivity {

    FancyButton btnSignIn, btnSignUp;
    TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtSlogan = findViewById(R.id.txtSlogan);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        txtSlogan.setTypeface(face);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToSignUp = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(moveToSignUp);
                finishActivity(1);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToSignIn = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(moveToSignIn);
                finishActivity(1);
            }
        });
    }


}
