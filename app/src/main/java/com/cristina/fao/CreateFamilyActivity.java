package com.cristina.fao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cristina.fao.models.Family;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateFamilyActivity extends AppCompatActivity {

    private String mUserId;

    private EditText mEnterNameEditText;
    private EditText mEnterMemberEditText;

    private Button mNextStep;
    private Button mDone;

    private String familyName;
    private List<String> familyUserName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_family);

        mUserId = getIntent().getStringExtra("USER_KEY");

        mEnterNameEditText = (EditText) findViewById(R.id.enter_family_name);
        mEnterMemberEditText = (EditText) findViewById(R.id.enter_member_username);

        mNextStep = (Button) findViewById(R.id.next_step);
        mDone = (Button) findViewById(R.id.enter_done);

        mNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEnterNameEditText.getVisibility() == View.VISIBLE) {
                    familyName = mEnterNameEditText.getText().toString();
                    mEnterNameEditText.setVisibility(View.INVISIBLE);
                    mEnterMemberEditText.setVisibility(View.VISIBLE);
                } else {
                    familyUserName.add(mEnterMemberEditText.getText().toString());
                    mEnterMemberEditText.setText("");
                }
            }
        });

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEnterNameEditText.getVisibility() == View.VISIBLE) {
                    familyName = mEnterNameEditText.getText().toString();
                } else if (!mEnterMemberEditText.getText().toString().equals("")) {
                    familyUserName.add(mEnterMemberEditText.getText().toString());
                }

                Family family = new Family(mUserId, familyName, familyUserName);
                Map<String, Object> map = family.familyToMap();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                String familyKey = databaseReference.child("families").push().getKey();

                Map<String, Object> updates = new HashMap<>();
                updates.put("/families/" + familyKey, map);
                updates.put("users/" + mUserId + "/families/" + familyKey, map);

                databaseReference.updateChildren(updates);

                /*databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(mUserId)
                        .child("families");
                databaseReference.push().setValue(familyKey);*/
            }
        });
    }
}
