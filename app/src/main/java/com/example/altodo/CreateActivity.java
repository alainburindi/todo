package com.example.altodo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.altodo.database.DbHelper;

import java.text.MessageFormat;
import java.util.Calendar;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
    private Button saveButton;
    private EditText nameEditor, dateEditor, timeEditor, descriptionEditor;
    DbHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);
        nameEditor = findViewById(R.id.task_name_editor);
        dateEditor = findViewById(R.id.task_date_editor);
        dateEditor.setOnClickListener(this);
        timeEditor = findViewById(R.id.task_time_editor);
        timeEditor.setOnClickListener(this);
        descriptionEditor = findViewById(R.id.task_description_editor);
        myDb = new DbHelper(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_button:
                String name = nameEditor.getText().toString();
                String date = dateEditor.getText().toString();
                String time = timeEditor.getText().toString();
                String description = descriptionEditor.getText().toString();
                if (!name.isEmpty() && !date.isEmpty() && !time.isEmpty() &&
                        !description.isEmpty()) {
                    myDb.addTask(name, date, time, description);
                    Toast.makeText(this, "event " + name +" saved successfully",
                            Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(this, "Make sure to fill all fields",
                            Toast.LENGTH_SHORT).show();
                break;
            case R.id.task_date_editor:
                pickDate();
                break;
            case R.id.task_time_editor:
                pickTime();
                break;
                default:
        }
    }

    private void pickTime() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        timeEditor.setText(MessageFormat.format("{0}:{1}", hourOfDay,
                                minute));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void pickDate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        dateEditor.setText(MessageFormat.format("{0}-{1}-{2}",
                                dayOfMonth, monthOfYear + 1, year));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

}
