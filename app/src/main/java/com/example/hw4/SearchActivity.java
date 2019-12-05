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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    EditText zipEditText2;
    TextView birdTextView, personTextView;
    Button searchButton2, reportButton, buttonImportance;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        zipEditText2 = findViewById(R.id.zipEditText2);
        birdTextView = findViewById(R.id.birdTextView);
        personTextView = findViewById(R.id.personTextView);
        searchButton2 = findViewById(R.id.searchButton2);
        reportButton = findViewById(R.id.reportButton);
        buttonImportance = findViewById(R.id.buttonImportance);

        searchButton2.setOnClickListener(this);
        reportButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        buttonImportance.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.birdMenu) {
            Intent birdIntent = new Intent(this, ImportanceActivity.class);
            startActivity(birdIntent);
        }
        if (item.getItemId() == R.id.reportMenu) {
            Intent reportIntent = new Intent(this, MainActivity.class);
            startActivity(reportIntent);
        }
        else if (item.getItemId() == R.id.seachMenu) {

            Toast.makeText(this, "You're already here!", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.logOutMenu) {
            mAuth.signOut();
            Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
            Intent logOutIntent = new Intent(this, LoginActivity.class);
            logOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            logOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logOutIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("bird");

        if (view == searchButton2) {

            String temp = zipEditText2.getText().toString();
            if (temp.length() < 1) {
                Toast.makeText(this, "Please enter a zip code", Toast.LENGTH_SHORT).show();
                return;
            }

            final Integer findZip =  Integer.parseInt(temp);

            //checking to make sure zip code is 5 digits
            if (findZip > 99999 || findZip < 10000) {
                Toast.makeText(this, "Please Enter a valid zip code", Toast.LENGTH_SHORT).show();
                return;
            }

            // Checking to see if a bird at this zip code exists
            myRef.orderByChild("zipcode").equalTo(findZip).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        Toast.makeText(SearchActivity.this, "No bird at zip code: " + findZip, Toast.LENGTH_SHORT).show();
                        buttonImportance.setVisibility(View.INVISIBLE);
                        birdTextView.setText("");
                        personTextView.setText("");
                        return;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SearchActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
            });

            // grabbing the bird at the zip code (there is a more optimal way to do all of this...)
            myRef.orderByChild("zipcode").equalTo(findZip).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    buttonImportance.setVisibility(View.VISIBLE);

                    final String findKey = dataSnapshot.getKey();
                    final Bird foundBird = dataSnapshot.getValue(Bird.class);
                    String findBirdName = foundBird.birdname;
                    String findPersonName = foundBird.personname;

                    buttonImportance.setText("Add Importance " + foundBird.points);

                    birdTextView.setText(findBirdName);
                    personTextView.setText(findPersonName);

                    buttonImportance.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            foundBird.addImportance();
                            buttonImportance.setText("Add Importance " + foundBird.points);
                            myRef.child(findKey).setValue(foundBird);
                        }
                    });
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
                    Toast.makeText(SearchActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else if (view.equals(reportButton)) {
            Intent reportIntent = new Intent(this, MainActivity.class);
            startActivity(reportIntent);
        }
    }
}
