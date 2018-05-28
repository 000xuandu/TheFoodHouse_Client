package thedark.example.com.thefoodhouse_client;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import thedark.example.com.thefoodhouse_client.Authentication.SignInActivity;
import thedark.example.com.thefoodhouse_client.Authentication.SignUpActivity;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnSignUp;
    TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtSlogan = findViewById(R.id.txtSlogan);

        Typeface face  = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        txtSlogan.setTypeface(face);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToSignUp = new Intent(getApplicationContext(), SignUpActivity.class);
                moveToSignUp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(moveToSignUp);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToSignIn = new Intent(getApplicationContext(), SignInActivity.class);
                moveToSignIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(moveToSignIn);
            }
        });
    }


}
