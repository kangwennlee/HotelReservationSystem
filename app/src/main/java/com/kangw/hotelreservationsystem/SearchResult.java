package com.kangw.hotelreservationsystem;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchResult extends AppCompatActivity {

    DatabaseReference databaseRoom;
    ArrayList<RoomView> roomList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        listView = findViewById(R.id.listViewRoom);

        databaseRoom = FirebaseDatabase.getInstance().getReference();

        roomList = new ArrayList<>();

        for(int i = 0; i < 4 ; i++){
            RoomView roomView = new RoomView();
            roomList.add(roomView);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.progress, null));
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        databaseRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("RoomType").hasChildren()){
                    List<String> room = dataSnapshot.child("RoomType").getValue(new GenericTypeIndicator<List<String>>(){});
                    for(int i=0;i<room.size();i++){
                        roomList.get(i).setRoomType(room.get(i));

                    }
                }
                if(dataSnapshot.child("RoomPrice").hasChildren()){
                    List<Double> room = dataSnapshot.child("RoomPrice").getValue(new GenericTypeIndicator<List<Double>>(){});
                    for(int i=0;i<room.size()-1;i++){
                        roomList.get(i).setRoomPrice("RM "+room.get(i+1).toString());
                    }
                }
                if(dataSnapshot.child("RoomDesc").hasChildren()){
                    List<String> room = dataSnapshot.child("RoomDesc").getValue(new GenericTypeIndicator<List<String>>(){});
                    for(int i=0;i<room.size();i++){
                        roomList.get(i).setRoomDesc(room.get(i));
                    }
                }
                RoomAdapter roomAdapter = new RoomAdapter(getApplicationContext(),roomList);
                listView.setAdapter(roomAdapter);
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
