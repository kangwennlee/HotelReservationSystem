package com.kangw.hotelreservationsystem;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomepageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomepageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomepageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText checkIn, checkOut;
    private Spinner room,adult,children;
    private Button btnSearch;
    private ImageButton btnLogin,btnReservation;

    Calendar myCalendar,myCalendar2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomepageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomepageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomepageFragment newInstance(String param1, String param2) {
        HomepageFragment fragment = new HomepageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_homepage, container, false);
        checkIn = v.findViewById(R.id.homepageCheckIn);
        checkOut = v.findViewById(R.id.homepageCheckOut);
        //room = v.findViewById(R.id.spinnerNumRoom);
        adult = v.findViewById(R.id.spinnerNumAdult);
        children = v.findViewById(R.id.spinnerNumChildren);
        btnSearch = v.findViewById(R.id.searchButton);
        btnLogin = v.findViewById(R.id.imageButtonLogin);
        btnReservation = v.findViewById(R.id.imageButtonReservation);

        myCalendar = Calendar.getInstance();
        myCalendar2 = Calendar.getInstance();
        myCalendar2.set(Calendar.DAY_OF_YEAR,myCalendar2.get(Calendar.DAY_OF_YEAR)+1);
        updateLabel();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); // minus 1 because the updateLabel function will add one
                updateLabel();
            }
        };
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                Calendar calendar = Calendar.getInstance();
                //calendar.add(Calendar.DAY_OF_YEAR,1);
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR));
                //mDatePicker.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                mDatePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());
                mDatePicker.show();
            }
        });
        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth); // minus 1 because the updateLabel function will add one
                updateLabel();
            }
        };
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), date2, myCalendar2.get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH), myCalendar2.get(Calendar.DAY_OF_MONTH));
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                Calendar calendar = Calendar.getInstance();
                //calendar.add(Calendar.DAY_OF_YEAR,1);
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                calendar.set(Calendar.DAY_OF_YEAR, myCalendar.get(Calendar.DAY_OF_YEAR)+1);
                //mDatePicker.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                mDatePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());
                mDatePicker.show();
            }
        });

        Integer[] numRoom = {1,2,3,4,5};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(),R.layout.spinner_item,numRoom);
        //room.setAdapter(adapter);
        adult.setAdapter(adapter);
        Integer[] numChild = {0,1,2,3};
        ArrayAdapter<Integer> adapter1 = new ArrayAdapter<Integer>(getContext(),R.layout.spinner_item,numChild);
        children.setAdapter(adapter1);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),SearchResult.class);
                ArrayList<String> stringArrayList = new ArrayList<>();
                stringArrayList.add(checkIn.getText().toString());
                stringArrayList.add(checkOut.getText().toString());
                //stringArrayList.add(room.getSelectedItem().toString());
                stringArrayList.add(adult.getSelectedItem().toString());
                stringArrayList.add(children.getSelectedItem().toString());
                i.putExtra("searchCriteria",stringArrayList);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        btnReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    Intent i = new Intent(getContext(),PurchaseHistory2.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(getContext(), LoginActivity.class);
                    startActivity(i);
                }
            }
        });

        return v;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        //myCalendar.add(Calendar.DATE, 1); // add one day because today date can't make deal
        checkIn.setText(sdf.format(myCalendar.getTime()));
        if(myCalendar2.getTime().compareTo(myCalendar.getTime())<1){
            myCalendar2.set(Calendar.DAY_OF_YEAR,myCalendar.get(Calendar.DAY_OF_YEAR)+1);
        }
        checkOut.setText(sdf.format(myCalendar2.getTime()));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
