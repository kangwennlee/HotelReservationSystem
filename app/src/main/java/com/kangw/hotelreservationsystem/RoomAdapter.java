package com.kangw.hotelreservationsystem;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class RoomAdapter extends ArrayAdapter<RoomCategory> {

    ArrayList<RoomCategory> data;

    public RoomAdapter(@NonNull Context context, @NonNull ArrayList<RoomCategory> roomList) {
        super(context, R.layout.room_category_item, roomList);
        this.data = roomList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.room_category_item, parent, false);
        }

        final ImageView roomPic = convertView.findViewById(R.id.imageViewRoomPic);
        TextView roomType = convertView.findViewById(R.id.textViewRoomType);
        TextView roomDescription = convertView.findViewById(R.id.textViewRoomChar);
        TextView roomPrice = convertView.findViewById(R.id.textViewRoomPrice);
        TextView roomFree = convertView.findViewById(R.id.textViewNumRoom);
        Button btnBook = convertView.findViewById(R.id.btnBook);

        final RoomCategory roomCategory = data.get(position);
        FirebaseStorage.getInstance().getReference().child("roomPic").child(String.valueOf(position)+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                BitmapDownloaderTask task = new BitmapDownloaderTask(roomPic);
                task.execute(uri.toString());
            }
        });
        roomType.setText(roomCategory.getRoomType());
        roomDescription.setText(roomCategory.getRoomDesc());
        roomPrice.setText(roomCategory.getRoomPrice());
        roomFree.setText(roomCategory.getRoomFree());

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),ReservationConfirm.class);
                i.putExtra("searchCriteria",roomCategory.getSearchCriteria());
                i.putExtra("roomSelected",position);
                getContext().startActivity(i);
            }
        });

        //return super.getView(position, convertView, parent);
        return convertView;
    }
}
