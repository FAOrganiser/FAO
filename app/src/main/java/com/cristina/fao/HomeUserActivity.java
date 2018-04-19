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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/*
TODO: interfata pentru home-ul utilizatorului
    in principal butonul de creare familie
 */
public class HomeUserActivity extends AppCompatActivity {

    Button mCreateFamilyButton;
    Button mOpenFamilyButton;
    DatabaseReference mDatabase;

    private String mFamilyKey;
    private boolean mHasFamily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mCreateFamilyButton = (Button) findViewById(R.id.create_family);
        mOpenFamilyButton = (Button) findViewById(R.id.open_family_button);

        //hasFamily();
        /*if (mHasFamily) {
            Log.i("TAG", "are familie");
            mOpenFamilyButton.setVisibility(View.VISIBLE);
        }*/

        mCreateFamilyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFamily(getUid());
            }
        });

        mOpenFamilyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFamily(getUid());
            }
        });
    }

    /**
     * open family screen
     * put extra is current user id
     * @param uid
     */
    public void openFamily(String uid) {

        Intent intent = new Intent(getApplicationContext(), FamilyActivity.class);
        intent.putExtra(FamilyActivity.FAMILY_KEY_EXTRA, mFamilyKey);
        startActivity(intent);

    }

    /**
     * create family and then open the family screen
     * @param uid
     */
    public void createFamily(String uid) {

        Toast.makeText(getApplicationContext(), "Creating family...", Toast.LENGTH_SHORT)
                .show();
        // create family
        String familyKey = mDatabase.child("families").push().getKey();
        mFamilyKey = familyKey;
        // update family admin
        mDatabase.child("families").child(familyKey).child("admin").setValue(uid);
        // update user family id
        mDatabase.child("users").child(uid).child("family_id").setValue(familyKey);

        mHasFamily = true;
        mOpenFamilyButton.setVisibility(View.VISIBLE);
        openFamily(uid);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getUid() == null) Log.i("tag2", "user invalid");
        else Log.i("tag3", getUid());
        DatabaseReference query = mDatabase.child("users").child(getUid()).child("family_id");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String familyId = (String) dataSnapshot.getValue();
                if (familyId == null) {
                    Log.i("TGA1", "nu are familie");
                } else {
                    mOpenFamilyButton.setVisibility(View.VISIBLE);
                    mFamilyKey = familyId;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        query.addValueEventListener(postListener);
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

}
