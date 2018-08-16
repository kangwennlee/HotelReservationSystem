package com.kangw.hotelreservationsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class AddRoom extends AppCompatActivity {

    private EditText editTextRoomPrice, editTextRoomQty,editTextRoomID;
    private Spinner spinnerRoomType, spinnerRoomStatus;
    private Button addBtn, resetBtn;
    private DatabaseReference databaseRoom, databaseRoomPrice;
    private List<Room> roomList;
    private List<Double> roomPrice;
    private int roomListSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        roomList = new LinkedList<>();
        roomPrice = new LinkedList<>();
        //View binding
        editTextRoomPrice = findViewById(R.id.editTextRoomPrice);
        editTextRoomQty = findViewById(R.id.editTextRoomQuantity);
        editTextRoomID = findViewById(R.id.editTextRoomID);
        spinnerRoomType = findViewById(R.id.spinnerRoomType);
        spinnerRoomStatus = findViewById(R.id.spinnerRoomStatus);
        addBtn = findViewById(R.id.addBtn);
        resetBtn = findViewById(R.id.resetBtn);
        //Firebase Intialization
        databaseRoom = FirebaseDatabase.getInstance().getReference().child("Room");
        databaseRoomPrice = FirebaseDatabase.getInstance().getReference().child("RoomPrice");
        //Initialize Room ID
        databaseRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomListSize = 0;
                if(dataSnapshot.hasChildren()){
                    roomList = dataSnapshot.getValue(new GenericTypeIndicator<List<Room>>(){});
                    roomListSize= roomList.size();
                }
                editTextRoomID.setText("R"+(roomListSize+1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Initialize Room Type Spinner
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
        //Initialize Room Price
        databaseRoomPrice.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    roomPrice = dataSnapshot.getValue(new GenericTypeIndicator<List<Double>>(){});
                    spinnerRoomType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            editTextRoomPrice.setText(roomPrice.get(i).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Initialize Room Quantity
        final CharSequence[] roomQty = new CharSequence[10];
        for(int i = 0; i < roomQty.length;i++){
            roomQty[i] = ""+ (i+1);
        }
        editTextRoomQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddRoom.this);
                builder.setTitle("Select number of rooms")
                        .setItems(roomQty, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                editTextRoomQty.setText(roomQty[which]);
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        //Initialize Status
        String[] roomStatus = getResources().getStringArray(R.array.room_status);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item,roomStatus);
        spinnerRoomStatus.setAdapter(adapter1);
        //Initialize Add Button
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextRoomQty.getText().toString().isEmpty() && spinnerRoomType.getSelectedItemPosition()!=0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddRoom.this);
                    // Get the layout inflater
                    LayoutInflater inflater = getLayoutInflater();
                    // Inflate and set the layout for the dialog
                    // Pass null as the parent view because its going in the dialog layout
                    builder.setView(inflater.inflate(R.layout.dialog_progress, null));
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    int numRoom = Integer.parseInt(editTextRoomQty.getText().toString());
                    for(int i = roomListSize; i< roomListSize+numRoom ;i++){
                        Room room = new Room("R"+(i+1),spinnerRoomType.getSelectedItem().toString(),spinnerRoomStatus.getSelectedItem().toString());
                        roomList.add(room);
                    }
                    databaseRoom.setValue(roomList).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            alertDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Room Added!",Toast.LENGTH_SHORT).show();
                            //finish();
                        }
                    });
                }else{

                }
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerRoomType.setSelection(0);
                editTextRoomQty.setText("1");
            }
        });
    }
}
