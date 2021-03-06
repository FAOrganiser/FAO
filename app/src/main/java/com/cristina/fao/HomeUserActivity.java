package com.cristina.fao;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
TODO: interfata pentru home-ul utilizatorului
    in principal butonul de creare familie
 */
public class HomeUserActivity extends AppCompatActivity {

    Button mCreateFamilyButton;
    Button mSeeFamiliesButton;

    DatabaseReference mDatabase;

    private String mUserKey;

    private TextView mUserNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        mUserKey = getUid();

        mUserNameTextView = (TextView) findViewById(R.id.userName);
        mUserNameTextView.setText(getUserName());

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mCreateFamilyButton = (Button) findViewById(R.id.create_family);
        mSeeFamiliesButton = (Button) findViewById(R.id.see_families);

        mCreateFamilyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFamily(getUid());
            }
        });

        mSeeFamiliesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DisplayFamiliesActivity.class);
                intent.putExtra("USER_KEY", mUserKey);
                Log.i("tag2", mUserKey);
                startActivity(intent);
            }
        });
    }

    /**
     * open the create family screen
     * @param uid
     */
    public void createFamily(String uid) {

        Intent intent = new Intent(getApplicationContext(), CreateFamilyActivity.class);
        intent.putExtra("USER_KEY", uid);
        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        Log.i("menu", "menu is created");
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.edit_profile:
                editProfile();
                return true;
            case R.id.sign_out:
                signOut();
                return true;
            case R.id.delete_account:
                deleteAccount();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public static Intent createIntent(Activity parent, IdpResponse response) {
        Intent intent = new Intent(parent, HomeUserActivity.class);
        intent.putExtra("RESPONSE", response.toString());
        return intent;
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(HomeUserActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }

    public  void editProfile() {

    }

    public void deleteAccount() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Deletion succeeded
                        } else {
                            // Deletion failed
                        }
                    }
                });
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getUserName() {
        //return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        return "";
    }

}
 /*Toast.makeText(getApplicationContext(), "Creating family...", Toast.LENGTH_SHORT)
                .show();
        // create family
        String familyKey = mDatabase.child("families").push().getKey();
        mFamilyKey = familyKey;
        // update family admin
        mDatabase.child("families").child(familyKey).child("admin").setValue(uid);
        // update user family id
        mDatabase.child("users").child(uid).child("family_id").setValue(familyKey);*/