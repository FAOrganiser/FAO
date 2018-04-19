package com.cristina.fao.lists;

import android.content.Context;
import android.speech.tts.TextToSpeech;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DetailListActivity extends AppCompatActivity {

    public static final String EXTRA_LIST_KEY = "list_key";

    private DatabaseReference mListReference;
    private DatabaseReference mIngredientsReference;
    private ValueEventListener mListListener;
    private RecyclerView mContentsRecycler;
    private ContentsAdapter mContentsAdapter;

    private String mPostKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);

        mPostKey = getIntent().getStringExtra(EXTRA_LIST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_LIST_KEY");
        }

        // Initialize Database
        mListReference = FirebaseDatabase.getInstance().getReference()
                .child("family-lists").child(mPostKey);

        mContentsRecycler = (RecyclerView) findViewById(R.id.recycler);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mContentsRecycler.setLayoutManager(layoutManager);

        mContentsRecycler.setHasFixedSize(true);

        mContentsRecycler.setAdapter(mContentsAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        /*ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                ShoppingList list = dataSnapshot.getValue(ShoppingList.class);
                mContentsAdapter.mIngredientsName = list.getmIngredients().keySet();
                mContentsAdapter.mIngredientQuantity = (ArrayList<String>) list.getmIngredients().values();
                // TODO: setare elemente de UI cu datele luate
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mListReference.addValueEventListener(postListener);*/

        // Keep copy of post listener so we can remove it when app stops
        //mListListener = postListener;

        mContentsAdapter = new ContentsAdapter(this, mListReference);
        mContentsRecycler.setAdapter(mContentsAdapter);

    }

    private static class ContentsViewHolder extends RecyclerView.ViewHolder {

        public TextView mIngrName;
        public TextView mIngrQuantity;

        public ContentsViewHolder(View itemView) {
            super(itemView);

            mIngrName = (TextView) itemView.findViewById(R.id.textViewName);
            mIngrQuantity = (TextView) itemView.findViewById(R.id.textViewQuantity);
        }
    }

    private static class ContentsAdapter extends RecyclerView.Adapter<ContentsViewHolder> {

            private Context mContext;
            private DatabaseReference mDatabaseReference;
            private ChildEventListener mChildEventListener;

            private List<String> mIngredientsName = new ArrayList<>();
            private List<String> mIngredientsQuantity = new ArrayList<>();

            public ContentsAdapter(final Context context, DatabaseReference ref) {
                mContext = context;
                mDatabaseReference = ref;

                // Create child event listener
                // [START child_event_listener_recycler]
                ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.d("tag", "onChildAdded:" + dataSnapshot.getKey());

                        // A new comment has been added, add it to the displayed list
                        ShoppingList.Ingredient ingr = dataSnapshot.getValue(ShoppingList.Ingredient.class);

                        // [START_EXCLUDE]
                        // Update RecyclerView
                        mIngredientsName.add(ingr.getIngredient());
                        mIngredientsQuantity.add(ingr.getQuantity());
                        notifyItemInserted(mIngredientsName.size() - 1);
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.d("tag", "onChildChanged:" + dataSnapshot.getKey());

                        // A comment has changed, use the key to determine if we are displaying this
                        // comment and if so displayed the changed comment.
                        ShoppingList.Ingredient newIngr = dataSnapshot.getValue(ShoppingList.Ingredient.class);

                        // [START_EXCLUDE]
                        int ingrIndex = mIngredientsName.indexOf(newIngr.getIngredient());
                        if (ingrIndex > -1) {
                            // Replace with the new data
                            mIngredientsName.set(ingrIndex, newIngr.getIngredient());
                            mIngredientsQuantity.set(ingrIndex, newIngr.getQuantity());

                            // Update the RecyclerView
                            notifyItemChanged(ingrIndex);
                        } else {
                            Log.w("tag", "onChildChanged:unknown_child:" + ingrIndex);
                        }
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        /*Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                        // A comment has changed, use the key to determine if we are displaying this
                        // comment and if so remove it.
                        String commentKey = dataSnapshot.getKey();

                        // [START_EXCLUDE]
                        int commentIndex = mCommentIds.indexOf(commentKey);
                        if (commentIndex > -1) {
                            // Remove data from the list
                            mCommentIds.remove(commentIndex);
                            mComments.remove(commentIndex);

                            // Update the RecyclerView
                            notifyItemRemoved(commentIndex);
                        } else {
                            Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
                        }*/
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.d("tag", "onChildMoved:" + dataSnapshot.getKey());

                        // A comment has changed position, use the key to determine if we are
                        // displaying this comment and if so move it.
                        /*Comment movedComment = dataSnapshot.getValue(Comment.class);
                        String commentKey = dataSnapshot.getKey();*/

                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("tag", "postComments:onCancelled", databaseError.toException());
                        Toast.makeText(mContext, "Failed to load comments.",
                                Toast.LENGTH_SHORT).show();
                    }
                };
                ref.addChildEventListener(childEventListener);
                // [END child_event_listener_recycler]

                // Store reference to listener so it can be removed on app stop
                mChildEventListener = childEventListener;
            }

            @Override
            public ContentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View view = inflater.inflate(R.layout.ingredient, parent, false);
                return new ContentsViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ContentsViewHolder holder, int position) {
                String name = mIngredientsName.get(position);
                String quantity = mIngredientsQuantity.get(position);
                holder.mIngrQuantity.setText(name);
                holder.mIngrQuantity.setText(quantity);
            }

            @Override
            public int getItemCount() {
                return mIngredientsName.size();
            }

            public void cleanupListener() {
                if (mChildEventListener != null) {
                    mDatabaseReference.removeEventListener(mChildEventListener);
                }
            }

        }
}
