package com.kangw.hotelreservationsystem;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
    ArrayList<RoomCategory> roomList;
    ListView listView;
    ArrayList<String> searchCriteria;
    Button btnBook;
    RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();

        searchCriteria = new ArrayList<>();
        searchCriteria = intent.getStringArrayListExtra("searchCriteria");

        listView = findViewById(R.id.listViewRoom);
        btnBook = findViewById(R.id.btnBook);

        databaseRoom = FirebaseDatabase.getInstance().getReference();

        roomList = new ArrayList<>();

        for(int i = 0; i < 4 ; i++){
            RoomCategory roomCategory = new RoomCategory();
            roomList.add(roomCategory);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.progress, null));
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        btnBook.setEnabled(false);

        databaseRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("RoomType").hasChildren()){
                    List<String> room = dataSnapshot.child("RoomType").getValue(new GenericTypeIndicator<List<String>>(){});
                    for(int i=0;i<roomList.size();i++){
                        roomList.get(i).setRoomType(room.get(i));
                    }
                }
                if(dataSnapshot.child("RoomPrice").hasChildren()){
                    List<Double> room = dataSnapshot.child("RoomPrice").getValue(new GenericTypeIndicator<List<Double>>(){});
                    for(int i=0;i<roomList.size();i++){
                        roomList.get(i).setRoomPrice(room.get(i+1).toString());
                    }
                }
                if(dataSnapshot.child("RoomDesc").hasChildren()){
                    List<String> room = dataSnapshot.child("RoomDesc").getValue(new GenericTypeIndicator<List<String>>(){});
                    for(int i=0;i<roomList.size();i++){
                        roomList.get(i).setRoomDesc(room.get(i));
                    }
                }
                if(dataSnapshot.child("Room").hasChildren()){
                    List<Room> room = dataSnapshot.child("Room").getValue(new GenericTypeIndicator<List<Room>>(){});
                    Integer[] numRoomFree = {0,0,0,0};
                    for(int i = 0; i<room.size();i++){
                        if(room.get(i).getStatus().equals("Available")){
                            switch (room.get(i).getRoomType()){
                                case "Deluxe Room":
                                    numRoomFree[0]++;
                                    break;
                                case "Premier Twin Room":
                                    numRoomFree[1]++;
                                    break;
                                case "Executive Suite Room":
                                    numRoomFree[2]++;
                                    break;
                                case "VIP Room":
                                    numRoomFree[3]++;
                                    break;
                            }
                        }
                    }
                    for(int i = 0; i < roomList.size();i++){
                        roomList.get(i).setRoomFree(numRoomFree[i].toString());

                    }
                }
                roomAdapter = new RoomAdapter(getApplicationContext(),roomList);
                listView.setAdapter(roomAdapter);
                alertDialog.dismiss();
                btnBook.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean proceed = false;
                int numZero = 0;
                for(int i = 0; i<roomAdapter.data.size();i++){
                    if(roomAdapter.data.get(i).getNumRoomSelected() != null){
                        if(!roomAdapter.data.get(i).getNumRoomSelected().equals("0")){
                            proceed = true;
                        }else{
                            numZero++;
                        }
                    }else{
                        proceed = false;
                        listView.smoothScrollToPosition(roomAdapter.getCount()-1);
                        //Toast.makeText(getApplicationContext(),"Please scroll down",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if(proceed){
                    if(searchCriteria.size()<9 ){
                        for(int i = 0; i<roomAdapter.data.size();i++){
                            searchCriteria.add(roomAdapter.data.get(i).getNumRoomSelected());
                        }
                    }else{
                        for(int i = 0; i<4;i++){
                            searchCriteria.set(i+5,roomAdapter.data.get(i).getNumRoomSelected());
                        }
                    }
                    Intent intent = new Intent(getApplicationContext(),ReservationConfirm.class);
                    intent.putExtra("searchCriteria",searchCriteria);
                    startActivity(intent);
                }else{
                    if(numZero==4){
                        Toast.makeText(getApplicationContext(),"Please select number of rooms!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}
