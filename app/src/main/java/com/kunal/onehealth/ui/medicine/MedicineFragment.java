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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kunal.onehealth.R;
import com.kunal.onehealth.data.model.Medicine;
import com.kunal.onehealth.data.repository.MedicineRepository;
import com.kunal.onehealth.receiver.MedicineReminderReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MedicineFragment extends Fragment {

    private EditText etName, etDosage, etTime;
    private Button btnAdd;
    private ListView listView;
    private MedicineRepository repo;
    private ArrayAdapter<String> adapter;
    private List<String> medicineNames = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_medicine, container, false);

        etName = view.findViewById(R.id.etName);
        etDosage = view.findViewById(R.id.etDosage);
        etTime = view.findViewById(R.id.etTime);
        btnAdd = view.findViewById(R.id.btnAdd);
        listView = view.findViewById(R.id.listView);

        repo = new MedicineRepository(requireContext());
        loadMedicines();

        // Disable manual typing — open TimePickerDialog
        etTime.setFocusable(false);
        etTime.setClickable(true);
        etTime.setOnClickListener(v -> showTimePicker());

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String dosage = etDosage.getText().toString().trim();
            String time = etTime.getText().toString().trim();

            if (name.isEmpty() || dosage.isEmpty() || time.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            repo.insert(new Medicine(name, dosage, time));
            scheduleReminder(name, time);

            Toast.makeText(requireContext(), "Medicine Added ✅", Toast.LENGTH_SHORT).show();

            etName.setText("");
            etDosage.setText("");
            etTime.setText("");

            loadMedicines();
        });

        return view;
    }

    // 🔹 Show 12-hour format time picker
    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, selectedHour, selectedMinute) -> {
                    String formattedTime = formatTime(selectedHour, selectedMinute);
                    etTime.setText(formattedTime);
                },
                hour,
                minute,
                false // 12-hour AM/PM
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
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, medicineNames);
        listView.setAdapter(adapter);
    }

    // 🔹 Schedule reminder for selected medicine
    private void scheduleReminder(String medicineName, String timeStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date date = sdf.parse(timeStr);

            if (date == null) {
                Toast.makeText(requireContext(), "Invalid time format", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar calendar = Calendar.getInstance();
            Calendar current = Calendar.getInstance();

            calendar.setTime(date);
            calendar.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH));

            if (calendar.before(current)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            long triggerTime = calendar.getTimeInMillis();

            Intent intent = new Intent(requireContext(), MedicineReminderReceiver.class);
            intent.putExtra("medicineName", medicineName);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    requireContext(),
                    (int) System.currentTimeMillis(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                } else {
                    try {
                        Intent settingsIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                        settingsIntent.setData(Uri.parse("package:" + requireContext().getPackageName()));
                        startActivity(settingsIntent);
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Please allow exact alarms manually in settings.", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }

            Toast.makeText(requireContext(), "Reminder set for " + medicineName, Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Invalid time format", Toast.LENGTH_SHORT).show();
        }
    }
}
