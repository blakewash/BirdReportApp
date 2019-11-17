package com.example.hw4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    EditText zipEditText2;
    TextView birdTextView, personTextView;
    Button searchButton2, reportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        zipEditText2 = findViewById(R.id.zipEditText2);
        birdTextView = findViewById(R.id.birdTextView);
        personTextView = findViewById(R.id.personTextView);
        searchButton2 = findViewById(R.id.searchButton2);
        reportButton = findViewById(R.id.reportButton);

        searchButton2.setOnClickListener(this);
        reportButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.reportMenu) {
            Intent reportIntent = new Intent(this, MainActivity.class);
            startActivity(reportIntent);
        }
        else if (item.getItemId() == R.id.seachMenu) {

            Toast.makeText(this, "You're already here!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("bird");

        if (view == searchButton2) {

            int findZip =  Integer.parseInt(  zipEditText2.getText().toString());
            myRef.orderByChild("zipcode").equalTo(findZip).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                   // String findKey = dataSnapshot.getKey();
                    Bird foundBird = dataSnapshot.getValue(Bird.class);
                    String findBirdName = foundBird.birdname;
                    String findPersonName = foundBird.personname;

                    birdTextView.setText(findBirdName);
                    personTextView.setText(findPersonName);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else if (view.equals(reportButton)) {
            Intent reportIntent = new Intent(this, MainActivity.class);
            startActivity(reportIntent);
        }
    }
}
