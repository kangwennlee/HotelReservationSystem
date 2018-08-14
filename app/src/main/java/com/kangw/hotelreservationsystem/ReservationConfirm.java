package com.kangw.hotelreservationsystem;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.jesusm.kfingerprintmanager.KFingerprintManager;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReservationConfirm extends AppCompatActivity {

    private static final String KEY = "KEY";

    Button btnLogin,btnProceed;
    TextView checkIn,checkOut,numRoom,numAdult,numChildren,roomSelected,pricePerRoom,totalPrice;
    String[] roomType;
    ArrayList<String> arrayList;
    Double total;
    String receipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_confirm);
        Intent intent = getIntent();
        arrayList = new ArrayList<>();
        arrayList = intent.getStringArrayListExtra("searchCriteria");
        receipt = "";

        checkIn = findViewById(R.id.textViewCheckIn);
        checkOut = findViewById(R.id.textViewCheckOut);
        numRoom = findViewById(R.id.textViewNumRoom);
        numAdult = findViewById(R.id.textViewNumAdult);
        numChildren = findViewById(R.id.textViewNumChildren);
        roomSelected = findViewById(R.id.textViewRoomSelected);
        pricePerRoom = findViewById(R.id.textViewPricePerRoom);
        totalPrice = findViewById(R.id.textViewTotalPrice);

        checkIn.setText(arrayList.get(0));
        receipt += "Check In Date: " + arrayList.get(0) + "\n";
        checkOut.setText(arrayList.get(1));
        receipt += "Check Out Date: " + arrayList.get(1) + "\n";
        numRoom.setText(arrayList.get(2));
        receipt += "Number of Room: " + arrayList.get(2) + "\n";
        numAdult.setText(arrayList.get(3));
        receipt += "Number of Adult: " + arrayList.get(3) + "\n";
        numChildren.setText(arrayList.get(4));
        receipt += "Number of Children: " + arrayList.get(4) + "\n";

        roomType = getResources().getStringArray(R.array.room_type);
        StringBuilder roomSelectText = new StringBuilder();
        receipt += "Room Selected: " + "\n";

        for(int i = 0; i < 4; i++){
            if(Integer.parseInt(arrayList.get(i+5))>0){
                roomSelectText.append(arrayList.get(i+5)).append(" x ").append(roomType[i+1]).append("\n");
            }
        }
        receipt += roomSelectText.toString();
        roomSelected.setText(roomSelectText);

        total = 0.0;
        final StringBuilder roomPrice = new StringBuilder();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("RoomPrice");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    List<Double> room = dataSnapshot.getValue(new GenericTypeIndicator<List<Double>>(){});
                    for(int i = 0; i < 4; i++){
                        Integer numRoom = Integer.parseInt(arrayList.get(i+5));
                        if(numRoom>0){
                            roomPrice.append(roomType[i+1]).append(" @ RM").append(room.get(i+1).toString()).append("\n");
                            total += room.get(i+1)*numRoom;
                        }
                    }
                    pricePerRoom.setText(roomPrice);
                    totalPrice.setText("RM"+total);
                    receipt += "Total Price: RM" + total + "\n";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });

        btnProceed = findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    Intent i = new Intent(getApplicationContext(),AddCard.class);
                    startActivityForResult(i,150);
                }else{
                    Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(i);
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 151) {
            Toast.makeText(getApplicationContext(), "Payment Cancelled!", Toast.LENGTH_LONG).show();
        } else {
            fingerAuth();
            //textViewCard.setText("Card Selected: " + data.getStringExtra("Card"));
        }
    }

    private void fingerAuth() {
        createFingerprintManagerInstance().authenticate(new KFingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationSuccess() {
                //messageText.setText("Successfully authenticated");
                storeReservation();
            }

            @Override
            public void onSuccessWithManualPassword(@NotNull String password) {
                //messageText.setText("Manual password: " + password);
            }

            @Override
            public void onFingerprintNotRecognized() {
                Toast.makeText(getApplicationContext(), "Fingerprint not recognized", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailedWithHelp(@Nullable String help) {
                //messageText.setText(help);
            }

            @Override
            public void onFingerprintNotAvailable() {
                //messageText.setText("Fingerprint not available");
            }

            @Override
            public void onCancelled() {
                //messageText.setText("Operation cancelled by user");
            }
        }, getSupportFragmentManager());

    }

    private void storeReservation() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
        String date = sdf.format(new Date());
        String time = date.substring(9,11) + ":" + date.substring(11,13) + ":" + date.substring(12,14);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Reservation");
        databaseReference.setValue(receipt, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Intent i = new Intent(getApplicationContext(),SuccessPaymentActivity.class);
                i.putExtra("purchase", receipt);
                startActivity(i);
                finish();
            }
        });
    }

    private KFingerprintManager createFingerprintManagerInstance() {
        KFingerprintManager fingerprintManager = new KFingerprintManager(this, KEY);
        //fingerprintManager.setAuthenticationDialogStyle(dialogTheme);
        return fingerprintManager;
    }
}
