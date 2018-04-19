package com.cristina.fao.lists;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cristina.fao.R;
import com.cristina.fao.models.ShoppingList;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.firebase.ui.database.FirebaseRecyclerAdapter;


public class ListActivity extends AppCompatActivity{

    public static final String EXTRA_TEXT = "extra_text";
    private int request_code = 1;
    private String mFamilyKey;

    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<ShoppingList, ListViewHolder> mAdapter;
    private RecyclerView mRecyclerView;

    private FloatingActionButton createList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        createList = (FloatingActionButton) findViewById(R.id.create_list);

        createList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewListActivity.class);
                intent.putExtra(NewListActivity.FAMILY_KEY_EXTRA, mFamilyKey);
                startActivityForResult(intent, request_code);
            }
        });

        mFamilyKey = getIntent().getStringExtra(EXTRA_TEXT);
        if (mFamilyKey == null) {
            throw new IllegalArgumentException("Must pass family id");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        Query listsQuery = getQuery(mDatabase, mFamilyKey);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ShoppingList>()
                .setQuery(listsQuery, ShoppingList.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<ShoppingList, ListViewHolder>(options) {

            @Override
            public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new ListViewHolder(inflater.inflate(R.layout.list_element, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(ListViewHolder viewHolder, int position, final ShoppingList model) {
                final DatabaseReference listRef = getRef(position);

                // Set click listener for the whole post view
                final String listKey = listRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getApplicationContext(), DetailListActivity.class);
                        intent.putExtra(DetailListActivity.EXTRA_LIST_KEY, listKey);
                        startActivity(intent);
                    }
                });
                Log.i("tag5", model.getName());
                viewHolder.itemView.setText(model.getName());
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }


    public Query getQuery(DatabaseReference databaseReference, String admin) {
        // All the lists
        return databaseReference.child("family-lists").child(mFamilyKey);
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

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView itemView;

        public ListViewHolder(View view) {
            super(view);
            itemView = view.findViewById(R.id.list_name);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == request_code) {
            if (resultCode == RESULT_OK) {
                String returnString = data.getStringExtra(NewListActivity.FAMILY_KEY_EXTRA);
                Toast.makeText(getApplicationContext(), returnString, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
