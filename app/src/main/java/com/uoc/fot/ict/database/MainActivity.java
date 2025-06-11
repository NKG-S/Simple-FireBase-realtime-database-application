package com.uoc.fot.ict.database;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView titleOut, descriptionOut, errors;
    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleOut = findViewById(R.id.titleOut);
        descriptionOut = findViewById(R.id.descriptionOut);
        errors = findViewById(R.id.errors);
        editButton = findViewById(R.id.editButton);

        fetchTitleAndDescription();

        // Open EditActivity when editButton is clicked
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);

            intent.putExtra("title", titleOut.getText().toString());
            intent.putExtra("description", descriptionOut.getText().toString());

            startActivityForResult(intent, 1); // Request code 1
        });
    }

    private void fetchTitleAndDescription() {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("data");
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String title = snapshot.child("title").getValue(String.class);
                String description = snapshot.child("description").getValue(String.class);

                titleOut.setText(title);
                descriptionOut.setText(description);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                errors.setText(error.toException().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Get the updated data from EditActivity
            String updatedTitle = data.getStringExtra("updatedTitle");
            String updatedDescription = data.getStringExtra("updatedDescription");

            // Update the UI with the new data
            titleOut.setText(updatedTitle);
            descriptionOut.setText(updatedDescription);
        }
    }
}
