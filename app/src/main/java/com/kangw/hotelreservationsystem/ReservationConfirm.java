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
    Reservation reservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_confirm);
        Intent intent = getIntent();
        arrayList = new ArrayList<>();
        arrayList = intent.getStringArrayListExtra("searchCriteria");
        reservation = new Reservation();

        checkIn = findViewById(R.id.textViewCheckIn);
        checkOut = findViewById(R.id.textViewCheckOut);
        numRoom = findViewById(R.id.textViewNumRoom);
        numAdult = findViewById(R.id.textViewNumAdult);
        numChildren = findViewById(R.id.textViewNumChildren);
        roomSelected = findViewById(R.id.textViewRoomSelected);
        pricePerRoom = findViewById(R.id.textViewPricePerRoom);
        totalPrice = findViewById(R.id.textViewTotalPrice);

        checkIn.setText(arrayList.get(0));
        reservation.setCheckInDate(arrayList.get(0));

        checkOut.setText(arrayList.get(1));
        reservation.setCheckOutDate(arrayList.get(1));

        //numRoom.setText(arrayList.get(2));
        //receipt += "Number of Room: " + arrayList.get(2) + "\n";

        numAdult.setText(arrayList.get(2));
        reservation.setNumAdult(arrayList.get(2));

        numChildren.setText(arrayList.get(3));
        reservation.setNumChild(arrayList.get(3));


        roomType = getResources().getStringArray(R.array.room_type);
        StringBuilder roomSelectText = new StringBuilder();
        Integer numRoom = 0;

        for(int i = 0; i < 4; i++){
            Integer num = Integer.parseInt(arrayList.get(i+4));
            if(num>0){
                roomSelectText.append(arrayList.get(i+4)).append(" x ").append(roomType[i+1]).append("\n");
                numRoom += num;
            }
        }
        reservation.setRoomSelected(roomSelectText.toString());
        reservation.setNumRoom(numRoom.toString());
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
                        Integer numRoom = Integer.parseInt(arrayList.get(i+4));
                        if(numRoom>0){
                            roomPrice.append(roomType[i+1]).append(" @ RM").append(room.get(i+1).toString()).append("\n");
                            total += room.get(i+1)*numRoom;
                        }
                    }
                    pricePerRoom.setText(roomPrice);
                    totalPrice.setText("RM"+total);
                    reservation.setTotalPrice(total.toString());
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
                    fingerAuth();
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
        if (resultCode == 151) {
            storeReservation();
        } else {
            Toast.makeText(getApplicationContext(), "Payment Cancelled!", Toast.LENGTH_LONG).show();
            //textViewCard.setText("Card Selected: " + data.getStringExtra("Card"));
        }
    }

    private void fingerAuth() {
        createFingerprintManagerInstance().authenticate(new KFingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationSuccess() {
                Toast.makeText(getApplicationContext(),"Successfully authenticated",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),AddCard.class);
                startActivityForResult(i,150);
            }

            @Override
            public void onSuccessWithManualPassword(@NotNull String password) {
                Toast.makeText(getApplicationContext(),"Manual password",Toast.LENGTH_SHORT).show();
                //messageText.setText("Manual password: " + password);
            }

            @Override
            public void onFingerprintNotRecognized() {
                Toast.makeText(getApplicationContext(), "Fingerprint not recognized", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailedWithHelp(@Nullable String help) {
                Toast.makeText(getApplicationContext(),help,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFingerprintNotAvailable() {
                //messageText.setText("Fingerprint not available");
                //Toast.makeText(getApplicationContext(),"Fingerprint not available",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),AddCard.class);
                startActivityForResult(i,150);
            }

            @Override
            public void onCancelled() {
                //messageText.setText("Operation cancelled by user");
                Toast.makeText(getApplicationContext(),"Operation cancelled by user",Toast.LENGTH_SHORT).show();
            }
        }, getSupportFragmentManager());

    }

    private void storeReservation() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
        String date = sdf.format(new Date());
        String time = date.substring(9,11) + ":" + date.substring(11,13) + ":" + date.substring(12,14);
        reservation.setReservationDate(date);
        reservation.setReservationTime(time);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Reservation").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(date);
        databaseReference.setValue(reservation, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(getApplicationContext(),"Reservation completed!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Intent i = new Intent(getApplicationContext(),SuccessPaymentActivity.class);
                i.putExtra("purchase", reservation.toString());
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
