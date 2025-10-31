package com.kunal.onehealth.ui.medicine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.kunal.onehealth.R;
import com.kunal.onehealth.data.model.Medicine;
import com.kunal.onehealth.data.repository.MedicineRepository;
import com.kunal.onehealth.receiver.MedicineReminderReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import android.widget.ArrayAdapter;

public class MedicineActivity extends AppCompatActivity {

    private EditText etName, etDosage, etTime;
    private Button btnAdd;
    private ListView listView;
    private MedicineRepository repo;
    private ArrayAdapter<String> adapter;
    private List<String> medicineNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        etName = findViewById(R.id.etName);
        etDosage = findViewById(R.id.etDosage);
        etTime = findViewById(R.id.etTime);
        btnAdd = findViewById(R.id.btnAdd);
        listView = findViewById(R.id.listView);

        repo = new MedicineRepository(this);
        loadMedicines();

        // 👇 Disable manual typing & open TimePickerDialog on click
        etTime.setFocusable(false);
        etTime.setClickable(true);
        etTime.setOnClickListener(v -> showTimePicker());

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String dosage = etDosage.getText().toString().trim();
            String time = etTime.getText().toString().trim();

            if (name.isEmpty() || dosage.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            repo.insert(new Medicine(name, dosage, time));
            scheduleReminder(name, time);

            Toast.makeText(this, "Medicine Added ✅", Toast.LENGTH_SHORT).show();

            etName.setText("");
            etDosage.setText("");
            etTime.setText("");

            loadMedicines();
        });
    }

    // 🔹 TimePickerDialog function (Digital AM/PM)
    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    String formattedTime = formatTime(selectedHour, selectedMinute);
                    etTime.setText(formattedTime);
                },
                hour,
                minute,
                false // false = 12-hour format (AM/PM)
        );

        timePickerDialog.show();
    }

    private String formatTime(int hourOfDay, int minute) {
        String amPm;
        if (hourOfDay >= 12) {
            amPm = "PM";
            if (hourOfDay > 12) hourOfDay -= 12;
        } else {
            amPm = "AM";
            if (hourOfDay == 0) hourOfDay = 12;
        }
        return String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minute, amPm);
    }

    private void loadMedicines() {
        List<Medicine> list = repo.getAll();
        medicineNames.clear();
        for (Medicine m : list) {
            medicineNames.add(m.getName() + " (" + m.getDosage() + ") at " + m.getTime());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicineNames);
        listView.setAdapter(adapter);
    }

    // 🔹 Schedule reminder using selected digital time
    private void scheduleReminder(String medicineName, String timeStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date date = sdf.parse(timeStr);

            if (date == null) {
                Toast.makeText(this, "Invalid time format", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar calendar = Calendar.getInstance();
            Calendar current = Calendar.getInstance();

            calendar.setTime(date);
            calendar.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH));

            // If time already passed today → schedule for tomorrow
            if (calendar.before(current)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            long triggerTime = calendar.getTimeInMillis();

            Intent intent = new Intent(this, MedicineReminderReceiver.class);
            intent.putExtra("medicineName", medicineName);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    (int) System.currentTimeMillis(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                } else {
                    try {
                        Intent settingsIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                        settingsIntent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(settingsIntent);
                    } catch (Exception e) {
                        Toast.makeText(this, "Please allow exact alarms manually in settings.", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }

            Toast.makeText(this, "Reminder set for " + medicineName, Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid time format", Toast.LENGTH_SHORT).show();
        }
    }
}
