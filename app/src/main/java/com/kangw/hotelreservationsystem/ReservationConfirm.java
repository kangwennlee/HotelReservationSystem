package com.kangw.hotelreservationsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ReservationConfirm extends AppCompatActivity {

    Button btnLogin;
    TextView checkIn,checkOut,numRoom,numAdult,numChildren,roomSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_confirm);
        Intent intent = getIntent();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList = intent.getStringArrayListExtra("searchCriteria");

        checkIn = findViewById(R.id.textViewCheckIn);
        checkOut = findViewById(R.id.textViewCheckOut);
        numRoom = findViewById(R.id.textViewNumRoom);
        numAdult = findViewById(R.id.textViewNumAdult);
        numChildren = findViewById(R.id.textViewNumChildren);
        roomSelected = findViewById(R.id.textViewRoomSelected);

        checkIn.setText(arrayList.get(0));
        checkOut.setText(arrayList.get(1));
        numRoom.setText(arrayList.get(2));
        numAdult.setText(arrayList.get(3));
        numChildren.setText(arrayList.get(4));
        String[] roomType = getResources().getStringArray(R.array.room_type);
        StringBuilder roomSelectText = new StringBuilder();
        for(int i = 0; i < 4; i++){
            roomSelectText.append(roomType[i+1]).append(arrayList.get(i+5)).append("\n");
        }
        roomSelected.setText(roomSelectText);

        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            btnLogin.setEnabled(false);
            btnLogin.setText("Logged in");
        }
    }
}
