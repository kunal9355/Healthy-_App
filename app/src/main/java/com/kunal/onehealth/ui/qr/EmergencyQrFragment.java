package com.kunal.onehealth.ui.qr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kunal.onehealth.R;
import com.kunal.onehealth.utils.QrCodeGenerator;

public class EmergencyQrFragment extends Fragment {

    private EditText etName, etBloodGroup, etEmergencyContact, etAllergies;
    private Button btnGenerate;
    private ImageView imgQrCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency_qr, container, false);

        etName = view.findViewById(R.id.etName);
        etBloodGroup = view.findViewById(R.id.etBloodGroup);
        etEmergencyContact = view.findViewById(R.id.etEmergencyContact);
        etAllergies = view.findViewById(R.id.etAllergies);
        btnGenerate = view.findViewById(R.id.btnGenerate);
        imgQrCode = view.findViewById(R.id.imgQrCode);

        btnGenerate.setOnClickListener(v -> {
            String data = "Name: " + etName.getText().toString().trim() +
                    "\nBlood Group: " + etBloodGroup.getText().toString().trim() +
                    "\nEmergency Contact: " + etEmergencyContact.getText().toString().trim() +
                    "\nAllergies: " + etAllergies.getText().toString().trim();

            if (etName.getText().toString().isEmpty() || etBloodGroup.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Please enter all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Bitmap qrCode = QrCodeGenerator.generateQRCode(data, 400, 400);
            if (qrCode != null) {
                imgQrCode.setImageBitmap(qrCode);
            } else {
                Toast.makeText(getContext(), "Error generating QR", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
