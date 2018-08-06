package com.kangw.hotelreservationsystem;

import android.app.AlertDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class UpdateRoom extends AppCompatActivity {

    private Spinner spinnerRoomID, spinnerRoomType, spinnerStatus;
    private EditText editTextStatus;
    private Button btnUpdate;
    private DatabaseReference databaseRoom;
    private List<Room> roomList;
    private String[] roomIDArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_room);
        //
        spinnerRoomID = findViewById(R.id.spinnerRoomID);
        spinnerRoomType = findViewById(R.id.spinnerRoomType);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnUpdate = findViewById(R.id.btnUpdate);
        roomList = new LinkedList<>();
        roomIDArr = new String[0];
        //
        databaseRoom = FirebaseDatabase.getInstance().getReference().child("Room");
        //
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRoom.this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.progress, null));
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        databaseRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    roomList = dataSnapshot.getValue(new GenericTypeIndicator<List<Room>>(){});
                    roomIDArr = new String[roomList.size()];
                    for(int i = 0; i<roomList.size();i++){
                        roomIDArr[i] = roomList.get(i).getRoomID();
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(UpdateRoom.this, R.layout.spinner_item,roomIDArr);
                    spinnerRoomID.setAdapter(arrayAdapter);
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String[] roomType = getResources().getStringArray(R.array.room_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,roomType){
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerRoomType.setAdapter(adapter);
        String[] roomStatus = getResources().getStringArray(R.array.room_status);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item,roomStatus);
        spinnerStatus.setAdapter(adapter1);

        spinnerRoomID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (roomList.get(i).getRoomType()){
                    case "Deluxe Room":
                        spinnerRoomType.setSelection(1);
                        break;
                    case "Premier Twin Room":
                        spinnerRoomType.setSelection(2);
                        break;
                    case "Executive Suite Room":
                        spinnerRoomType.setSelection(3);
                        break;
                    case "VIP Room":
                        spinnerRoomType.setSelection(4);
                        break;
                }
                switch (roomList.get(i).getStatus()){
                    case "Available":
                        spinnerStatus.setSelection(0);
                        break;
                    case "Not Available":
                        spinnerStatus.setSelection(1);
                        break;
                    case "In Use":
                        spinnerStatus.setSelection(2);
                        break;
                    case "Reserved":
                        spinnerStatus.setSelection(3);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //UPDATE BUTTON
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRoom.this);
                // Get the layout inflater
                LayoutInflater inflater = getLayoutInflater();
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.progress, null));
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                roomList.get(spinnerRoomID.getSelectedItemPosition()).setRoomType(spinnerRoomType.getSelectedItem().toString());
                roomList.get(spinnerRoomID.getSelectedItemPosition()).setStatus(spinnerStatus.getSelectedItem().toString());
                databaseRoom.setValue(roomList).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        alertDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Room Updated!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }
}
