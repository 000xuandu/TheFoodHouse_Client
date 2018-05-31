package thedark.example.com.thefoodhouse_client.Authentication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import mehdi.sakout.fancybuttons.FancyButton;
import thedark.example.com.thefoodhouse_client.Common.Common;
import thedark.example.com.thefoodhouse_client.Home;
import thedark.example.com.thefoodhouse_client.MainActivity;
import thedark.example.com.thefoodhouse_client.Model.User;
import thedark.example.com.thefoodhouse_client.R;

public class SignInActivity extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    FancyButton btnSignIn;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        //Get data from sharedPreferences to username and password:
        edtPhone.setText(sharedPreferences.getString("phone", ""));
        edtPassword.setText(sharedPreferences.getString("password", ""));


        //Init Firebase:
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_users = database.getReference("Users");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Check null information:
                        if (edtPhone.getText().toString().equals("") && edtPassword.getText().toString().equals("")) {
                            mDialog.dismiss();

                            Toast.makeText(SignInActivity.this, "Please enter full information", Toast.LENGTH_SHORT).show();
                        } else {
                            //Check if user not exist in databas:
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                //Get User Information:
                                User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                user.setPhone(edtPhone.getText().toString());
                                if (user.getPassword().equals(edtPassword.getText().toString())) {
                                    mDialog.dismiss();
                                    Intent moveToHome = new Intent(getApplicationContext(), Home.class);
                                    Common.currentUser = user;
                                    startActivity(moveToHome);
                                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("phone", edtPhone.getText().toString());
                                    editor.putString("password", edtPassword.getText().toString());
                                    editor.putBoolean("logout", true); // if user logout is false
                                    editor.apply();
                                    finish();
                                } else {
                                    Toast.makeText(SignInActivity.this, "Wrong phone number or password", Toast.LENGTH_SHORT).show();
                                    mDialog.dismiss();
                                }
                            } else {
                                Toast.makeText(SignInActivity.this, "Phone number doesn't exist", Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityForResult(new Intent(this, MainActivity.class), 1);
    }
}
