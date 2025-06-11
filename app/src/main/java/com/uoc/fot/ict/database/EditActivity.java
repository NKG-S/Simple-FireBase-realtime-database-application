package com.uoc.fot.ict.database;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditActivity extends AppCompatActivity {

    private EditText editTitle, editDescription;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        saveButton = findViewById(R.id.saveButton);

        // Fetch data from the previous activity
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        editTitle.setText(title);
        editDescription.setText(description);

        // Save changes to Firebase
        saveButton.setOnClickListener(v -> {
            EditData();
        });
    }

    private void EditData(){
        String newTitle = editTitle.getText().toString();
        String newDescription = editDescription.getText().toString();

        if (TextUtils.isEmpty(newTitle) || TextUtils.isEmpty(newDescription)) {
            Toast.makeText(EditActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reference to Firebase
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("data");

        // Updating the title and description in Firebase
        dataRef.child("title").setValue(newTitle);
        dataRef.child("description").setValue(newDescription);

        // Show success message
        Toast.makeText(EditActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();

        // Pass the updated data back to MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedTitle", newTitle);
        resultIntent.putExtra("updatedDescription", newDescription);
        setResult(RESULT_OK, resultIntent);

        // Close the activity
        finish();
    }
}
