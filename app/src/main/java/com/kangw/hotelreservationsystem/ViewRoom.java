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

import java.util.LinkedList;
import java.util.List;

public class ViewRoom extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference databaseReference;
    private List<Room> roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room);

        listView = findViewById(R.id.roomList);
        roomList = new LinkedList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Room");

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewRoom.this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_progress, null));
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    roomList = dataSnapshot.getValue(new GenericTypeIndicator<List<Room>>(){});
                    String[] roomArr = new String[roomList.size()];
                    for(int i = 0; i< roomList.size();i++){
                        roomArr[i] = roomList.get(i).toString();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewRoom.this, android.R.layout.simple_list_item_1, roomArr);
                    listView.setAdapter(adapter);
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
