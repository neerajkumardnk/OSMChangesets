package com.example.reach.osmchangesets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SuperMain extends AppCompatActivity {
    TextView textViewTitle;
    EditText editTextOsmID;
    Button buttonGetChangesets;
    String OsmID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_main);
        textViewTitle = findViewById(R.id.textViewTitle);
        editTextOsmID = findViewById(R.id.editTextOsmID);
        buttonGetChangesets = findViewById(R.id.buttonGetChangesets);
        buttonGetChangesets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OsmID = editTextOsmID.getText().toString().trim();
                Intent intent2 = new Intent(SuperMain.this, MainActivity.class);
                intent2.putExtra("osmid", OsmID);
                startActivity(intent2);

            }
        });


    }

}
