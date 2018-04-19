package com.cristina.fao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cristina.fao.lists.ListActivity;

public class FamilyActivity extends AppCompatActivity {

    private Button listButton;
    private String admin;

    private String mFamilyKey;
    public static final String FAMILY_KEY_EXTRA = "extra_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        mFamilyKey = getIntent().getStringExtra(FAMILY_KEY_EXTRA);

        listButton = (Button) findViewById(R.id.lists_button);

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra(ListActivity.EXTRA_TEXT, mFamilyKey);
                startActivity(intent);
            }
        });
    }
}
