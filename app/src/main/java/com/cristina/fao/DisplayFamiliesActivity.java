package com.cristina.fao;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cristina.fao.lists.DetailListActivity;
import com.cristina.fao.lists.ListActivity;
import com.cristina.fao.models.Family;
import com.cristina.fao.models.ShoppingList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import static com.cristina.fao.FamilyActivity.FAMILY_KEY_EXTRA;

/**
 * o lista cu toate familiile userului curent
 */
public class DisplayFamiliesActivity extends AppCompatActivity {

    String mUserId;

    private RecyclerView mRecyclerView;

    private DatabaseReference mDatabase;

    private FirebaseRecyclerAdapter<Family, FamilyViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_families);

        mUserId = getIntent().getStringExtra("USER_KEY");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_families);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        Query listsQuery = getQuery(mDatabase, mUserId);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Family>()
                .setQuery(listsQuery, Family.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Family, FamilyViewHolder>(options) {

            @Override
            public FamilyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new FamilyViewHolder(inflater.inflate(R.layout.family_layout, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(FamilyViewHolder viewHolder, int position, final Family model) {
                final DatabaseReference listRef = getRef(position);

                final String familyKey = listRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getApplicationContext(), FamilyActivity.class);
                        intent.putExtra(FAMILY_KEY_EXTRA, familyKey);
                        startActivity(intent);
                    }
                });
                viewHolder.mName.setText(model.getName());
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }


    public Query getQuery(DatabaseReference databaseReference, String admin) {
        Log.i("tag1", admin);
        return databaseReference.child("users").child(admin).child("families");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    private static class FamilyViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;

        public FamilyViewHolder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.family_name);
        }
    }

}
