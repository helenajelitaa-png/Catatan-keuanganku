package com.helena.keuanganku;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {
    EditText etNama, etBatas;
    SwitchCompat swGelap;
    Button btnSimpan;
    PrefsManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pm = new PrefsManager(this);
        etNama = findViewById(R.id.etNama);
        etBatas = findViewById(R.id.etBatas);
        swGelap = findViewById(R.id.swGelap);
        btnSimpan = findViewById(R.id.btnSimpan);

        etNama.setText(pm.getNama());
        swGelap.setChecked(pm.isModeGelap());
        if (pm.getBatas() > 0) etBatas.setText(String.valueOf(pm.getBatas()));

        btnSimpan.setOnClickListener(v -> {
            long batas = 0;
            String teks = etBatas.getText().toString().trim();
            if (!teks.isEmpty()) {
                try {
                    batas = Long.parseLong(teks);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Batas tidak valid", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            pm.simpanNama(etNama.getText().toString().trim());
            pm.simpanModeGelap(swGelap.isChecked());
            pm.simpanBatas(batas);
            Toast.makeText(this, "Pengaturan tersimpan", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
