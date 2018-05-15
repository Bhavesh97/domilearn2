package com.domilearn2.me.domilearn2;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    EditText editTextName;

    Button buttonAddArtist;


    ListView listViewA;
    List<Artist> artistList;

    DatabaseReference databaseArtists;

    @Override
    protected void onStart() {
        super.onStart();
        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                artistList.clear();


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Artist artist = postSnapshot.getValue(Artist.class);

                    artistList.add(artist);
                }


                ArtistList artistAdapter = new ArtistList(MainActivity.this, artistList);

                listViewA.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");


        editTextName = (EditText) findViewById(R.id.editTextName);


        buttonAddArtist = (Button) findViewById(R.id.buttonAddArtist);


        listViewA = (ListView)findViewById(R.id.listViewA);
        artistList = new ArrayList<>();

        buttonAddArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 addArtist();
            }
        });

        listViewA.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist artist = artistList.get(i);
                showUpdateDialog(artist.getArtistId(), artist.getArtistName());
                return true;
            }
        });
    }

    private void showUpdateDialog(final String artistId, String artistName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);

        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.button2);


        dialogBuilder.setTitle(artistName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();

                if (!TextUtils.isEmpty(name)) {
                    updateArtist(artistId, name);
                    b.dismiss();
                }
            }
        });



    }

    private boolean updateArtist(String id, String name) {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("artists").child(id);


        Artist artist = new Artist(id, name);
        dR.setValue(artist);
        Toast.makeText(getApplicationContext(), "Name Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private void addArtist() {

        String name = editTextName.getText().toString().trim();


        if (!TextUtils.isEmpty(name)) {


            String id = databaseArtists.push().getKey();


            Artist artist = new Artist(id, name);


            databaseArtists.child(id).setValue(artist);
            editTextName.setText("");
            Toast.makeText(this, "Name added", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
}