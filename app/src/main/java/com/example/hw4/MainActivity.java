package com.example.hw4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText birdEditText, zipEditText, personEditText;
    Button searchButton, submitButton;

    //FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        birdEditText = findViewById(R.id.birdEditText);
        zipEditText = findViewById(R.id.zipEditText);
        personEditText = findViewById(R.id.personEditText);

        searchButton = findViewById(R.id.searchButton);
        submitButton = findViewById(R.id.submitButton);

        searchButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);

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
            Toast.makeText(this, "You're already here!", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.seachMenu) {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("bird");


        if (view == searchButton) {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
        }
        else if (view == submitButton) {

            String bird = birdEditText.getText().toString();
            String person = personEditText.getText().toString();
            String temp = zipEditText.getText().toString();

            if (bird.equals("")) {
                Toast.makeText(this, "Please Enter a bird name", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (temp.equals("")) {
                Toast.makeText(this, "Please Enter a zip code", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (person.equals("")) {
                Toast.makeText(this, "Please Enter a person name", Toast.LENGTH_SHORT).show();
                return;
            }


            int zip = Integer.parseInt(temp);

            Bird myBird = new Bird(bird, zip, person);

            Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show();

            myRef.push().setValue(myBird);

            //myRef.setValue("Hello, World!");

        }
    }
}
