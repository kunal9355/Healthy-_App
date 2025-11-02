package com.kunal.onehealth.ui.home;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kunal.onehealth.R;
import com.kunal.onehealth.ui.medicine.MedicineActivity;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnMedicine = view.findViewById(R.id.btnMedicineReminder);
        Button btnQr = view.findViewById(R.id.btnQrPage);

        // Medicine Reminder button → open MedicineActivity
        btnMedicine.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MedicineActivity.class);
            startActivity(intent);
        });

        // QR Page button → navigate to QR Fragment
        btnQr.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.qrFragment)
        );

        return view;
    }
}
