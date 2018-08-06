package com.kangw.hotelreservationsystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

public class AddRoom extends AppCompatActivity {

    EditText editTextRoomPrice, editTextRoomQuantity;
    Spinner spinnerRoomType;
    Button addBtn, resetBtn;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        //View binding
        editTextRoomPrice = findViewById(R.id.editTextRoomPrice);
        editTextRoomQuantity = findViewById(R.id.editTextRoomQuantity);
        spinnerRoomType = findViewById(R.id.spinnerRoomType);
        addBtn = findViewById(R.id.addBtn);
        resetBtn = findViewById(R.id.resetBtn);
        //Firebase Intialization
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Room");
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
        spinnerRoomType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        editTextRoomPrice.setText("00.00");
                        break;
                    case 1:
                        editTextRoomPrice.setText("500.00");
                        break;
                    case 2:
                        editTextRoomPrice.setText("650.00");
                        break;
                    case 3:
                        editTextRoomPrice.setText("800.00");
                        break;
                    case 4:
                        editTextRoomPrice.setText("1000.00");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Initialize Room Quantity
        final CharSequence[] roomQty = new CharSequence[10];
        for(int i = 0; i < roomQty.length;i++){
            roomQty[i] = ""+ (i+1);
        }
        editTextRoomQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddRoom.this);
                builder.setTitle("Select number of rooms")
                        .setItems(roomQty, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                editTextRoomQuantity.setText(roomQty[which]);
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        //Initialize Add Button
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddRoom.this);
                // Get the layout inflater
                LayoutInflater inflater = getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.progress, null));
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                List<Room> roomList = new LinkedList<>();
                int numRoom = Integer.getInteger(editTextRoomQuantity.getText().toString());
                for(int i = 0; i< numRoom ;i++){
                    Room room = new Room("R"+(i+1),spinnerRoomType.getSelectedItem().toString(),Double.parseDouble(editTextRoomPrice.getText().toString()),"Free");
                    roomList.add(room);
                }
                mDatabase.setValue(roomList);
                alertDialog.hide();
            }
        });
    }
}
