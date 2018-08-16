package com.kangw.hotelreservationsystem;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class UpdateRoomPrice extends AppCompatActivity {

    private EditText deluxe,premier,executive,vip;
    private Button btnUpdate;
    private DatabaseReference databaseRoomPrice;
    private List<Double> roomPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_room_price);
        deluxe = findViewById(R.id.editTextDeluxe);
        premier = findViewById(R.id.editTextPremier);
        executive = findViewById(R.id.editTextExecutive);
        vip = findViewById(R.id.editTextVIP);
        btnUpdate = findViewById(R.id.btnUpdate);
        databaseRoomPrice = FirebaseDatabase.getInstance().getReference().child("RoomPrice");
        roomPrice = new LinkedList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRoomPrice.this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_progress, null));
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


        databaseRoomPrice.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    roomPrice = dataSnapshot.getValue(new GenericTypeIndicator<List<Double>>(){});
                    deluxe.setText(roomPrice.get(1).toString());
                    premier.setText(roomPrice.get(2).toString());
                    executive.setText(roomPrice.get(3).toString());
                    vip.setText(roomPrice.get(4).toString());
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRoomPrice.this);
                // Get the layout inflater
                LayoutInflater inflater = getLayoutInflater();
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.dialog_progress, null));
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                roomPrice.set(1,Double.parseDouble(deluxe.getText().toString()));
                roomPrice.set(2,Double.parseDouble(premier.getText().toString()));
                roomPrice.set(3,Double.parseDouble(executive.getText().toString()));
                roomPrice.set(4,Double.parseDouble(vip.getText().toString()));

                databaseRoomPrice.setValue(roomPrice);
                alertDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Room Price Updated!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
