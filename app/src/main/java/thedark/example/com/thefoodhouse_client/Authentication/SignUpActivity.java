package thedark.example.com.thefoodhouse_client.Authentication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import thedark.example.com.thefoodhouse_client.Model.User;
import thedark.example.com.thefoodhouse_client.R;

public class SignUpActivity extends AppCompatActivity {

    EditText edtName, edtPhone, edtPassword, edtEmail;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName = (MaterialEditText) findViewById(R.id.edtName);
        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        edtEmail = (MaterialEditText) findViewById(R.id.edtEmail);
        btnSignUp = findViewById(R.id.btnSignUp);

        //Init Firebase:
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_users = database.getReference("Users");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Check if already user phone
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Phone number already register", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            mDialog.dismiss();
                            User user = new User(
                                    edtName.getText().toString(),
                                    edtPassword.getText().toString(),
                                    edtEmail.getText().toString());
                            table_users.child(edtPhone.getText().toString()).setValue(user);
                            Toast.makeText(SignUpActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
