package com.justinej.suiviconso;

import android.app.DownloadManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView myDate;
    Calendar mCalendar;
    DatePicker mDatePicker;
    TextView myHour;
    Button mDatePlusBtn;
    Button mDateMoinsBtn;
    Button mHourPlusBtn;
    Button mHourMoinsBtn;

    private Spinner spinnerType, spinnerVolume;
    private FirebaseFirestore mFirestore;
    private DownloadManager.Query mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDate = (TextView) findViewById(R.id.activity_main_edit_date);
        mDatePicker = (DatePicker) findViewById(R.id.activity_main_picker_result);
        myHour = (TextView) findViewById(R.id.activity_main_edit_heure);
        mDatePlusBtn = (Button) findViewById(R.id.activity_main_date_plus);
        mDateMoinsBtn = (Button) findViewById(R.id.activity_main_date_moins);
        mHourPlusBtn = (Button) findViewById(R.id.activity_main_hour_plus);
        mHourMoinsBtn = (Button) findViewById(R.id.activity_main_hour_moins);

        mCalendar = Calendar.getInstance();
        final int mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        final int mMonth = mCalendar.get(Calendar.MONTH);
        final int mYear = mCalendar.get(Calendar.YEAR);
        final int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        final int mMinute = mCalendar.get(Calendar.MINUTE);

        // set current date into textview
        myDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(mDay).append("/")
                .append(mMonth + 1).append("/")
                .append(mYear));
        myHour.setText(new StringBuilder()
                .append(mHour).append(":")
                .append(mMinute));

        // Initialize Firestore
        initFirestore();
    }

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
        //Initialisation des spinner
        CollectionReference BoissonsRef = mFirestore.collection("Boissons");
        Spinner spinner = (Spinner) findViewById(R.id.activity_main_type_spinner);
        final List<String> subjects = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        BoissonsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("Type");
                        subjects.add(subject);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }
}
