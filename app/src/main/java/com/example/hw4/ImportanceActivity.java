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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ImportanceActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textViewImportantBird, textViewImportantPerson, textViewImportantZip, textViewImportantTitle;
    Button buttonFoundImportance, buttonImportantReport;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importance);

        textViewImportantPerson = findViewById(R.id.textViewImportantPerson);
        textViewImportantBird = findViewById(R.id.textViewImportantBird);
        textViewImportantZip = findViewById(R.id.textViewImportantZip);
        textViewImportantTitle = findViewById(R.id.textViewImportantTitle);
        buttonFoundImportance = findViewById(R.id.buttonFoundImportance);
        buttonImportantReport = findViewById(R.id.buttonImportantReport);
        buttonImportantReport.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        buttonFoundImportance.setVisibility(View.INVISIBLE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("bird");

        // grabbing the bird of the highest importance
        myRef.orderByChild("points").limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                buttonFoundImportance.setVisibility(View.VISIBLE);
                final String findKey = dataSnapshot.getKey();
                final Bird foundBird = dataSnapshot.getValue(Bird.class);
                String findBirdName = foundBird.birdname;
                String findPersonName = foundBird.personname;
                int findZip = foundBird.zipcode;

                buttonFoundImportance.setText("Add Importance " + foundBird.points);

                textViewImportantBird.setText(findBirdName);
                textViewImportantPerson.setText(findPersonName);
                textViewImportantZip.setText(String.valueOf(findZip));

                buttonFoundImportance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        foundBird.addImportance();
                        buttonFoundImportance.setText("Add Importance " + foundBird.points);
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
                Toast.makeText(ImportanceActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent reportIntent = new Intent(this, MainActivity.class);
        startActivity(reportIntent);
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
            Toast.makeText(this, "You're already here!", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.reportMenu) {
            Intent reportIntent = new Intent(this, MainActivity.class);
            startActivity(reportIntent);
        }
        else if (item.getItemId() == R.id.seachMenu) {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
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
}
