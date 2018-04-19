package com.cristina.fao.lists;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cristina.fao.R;
import com.cristina.fao.models.ShoppingList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewListActivity extends AppCompatActivity {

    public static final String FAMILY_KEY_EXTRA = "extra";
    private String mFamilyKey;

    private EditText mNameEditText;
    private EditText mIngredientEditText;
    private EditText mQuantityEditText;
    private Button mNextIngrButton;
    private Button mDoneButton;

    private String mListName;
    private String mIngrList;
    private String mQuantityList;

    private HashMap<String, String> mIngredients = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        mFamilyKey = getIntent().getStringExtra(FAMILY_KEY_EXTRA);

        mNameEditText = (EditText) findViewById(R.id.list_name);
        mIngredientEditText = (EditText) findViewById(R.id.list_ingredient);
        mQuantityEditText = (EditText) findViewById(R.id.list_ingr_quantity);
        mNextIngrButton = (Button) findViewById(R.id.next_ingr_button);
        mDoneButton = (Button) findViewById(R.id.done_list);

        mNextIngrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mNameEditText.getVisibility() == View.VISIBLE) {
                    mListName = mNameEditText.getText().toString();
                    mNameEditText.setVisibility(View.INVISIBLE);
                }
                mIngrList = mIngredientEditText.getText().toString();
                mIngredientEditText.setText("");
                mQuantityList = mQuantityEditText.getText().toString();
                mQuantityEditText.setText("");
                if (mListName.matches("")) {
                    Toast.makeText(getApplicationContext(),
                            "You must introduce a name for the list", Toast.LENGTH_SHORT).show();
                } else if (mIngrList.matches("")) {
                    Toast.makeText(getApplicationContext(),
                            "You must introduce an ingredient", Toast.LENGTH_SHORT).show();
                } else if (mQuantityList.matches("")) {
                    Toast.makeText(getApplicationContext(),
                            "You must introduce the quantity", Toast.LENGTH_SHORT).show();
                } else {
                    mIngredients.put(mIngrList, mQuantityList);
                }
            }
        });

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                ShoppingList shoppingList = new ShoppingList(mListName, mIngredients);
                Map<String, Object> shoppingListMap = shoppingList.toMap();

                String listKey = databaseReference.child("lists").push().getKey();
                Map<String, Object> updates = new HashMap<>();
                updates.put("/lists/" + listKey, shoppingListMap);
                updates.put("/family-lists/" + mFamilyKey + "/" + listKey, shoppingListMap);

                databaseReference.updateChildren(updates);

                Intent intent = new Intent();
                intent.putExtra(FAMILY_KEY_EXTRA, "List created successfully");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
