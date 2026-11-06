package com.helena.keuanganku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView tvSapaan, tvSaldo, tvPeringatan;
    EditText etKeterangan, etNominal;
    RadioButton rbMasuk, rbKeluar;
    Button btnTambah, btnPengaturan;
    ListView lvTransaksi;

    PrefsManager pm;
    DatabaseHelper db;
    ArrayAdapter<Transaksi> adapter;
    List<Transaksi> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pm = new PrefsManager(this);
        db = new DatabaseHelper(this);

        tvSapaan = findViewById(R.id.tvSapaan);
        tvSaldo = findViewById(R.id.tvSaldo);
        tvPeringatan = findViewById(R.id.tvPeringatan);
        etKeterangan = findViewById(R.id.etKeterangan);
        etNominal = findViewById(R.id.etNominal);
        rbMasuk = findViewById(R.id.rbMasuk);
        rbKeluar = findViewById(R.id.rbKeluar);
        btnTambah = findViewById(R.id.btnTambah);
        btnPengaturan = findViewById(R.id.btnPengaturan);
        lvTransaksi = findViewById(R.id.lvTransaksi);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        lvTransaksi.setAdapter(adapter);

        btnTambah.setOnClickListener(v -> tambahTransaksi());

        btnPengaturan.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SettingsActivity.class)));

        lvTransaksi.setOnItemLongClickListener((parent, view, position, id) -> {
            Transaksi t = data.get(position);
            new AlertDialog.Builder(this)
                    .setTitle("Hapus transaksi?")
                    .setMessage(t.keterangan)
                    .setPositiveButton("Hapus", (d, w) -> {
                        db.hapus(t.id);
                        muatData();
                    })
                    .setNegativeButton("Batal", null)
                    .show();
            return true;
        });
    }

    // Pengaturan dibaca dari SharedPreferences setiap layar tampil lagi
    @Override
    protected void onResume() {
        super.onResume();

        AppCompatDelegate.setDefaultNightMode(pm.isModeGelap()
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO);

        String nama = pm.getNama();
        tvSapaan.setText(nama.isEmpty() ? "Halo!" : "Halo, " + nama + "!");

        muatData();
    }

    private void tambahTransaksi() {
        String ket = etKeterangan.getText().toString().trim();
        String nom = etNominal.getText().toString().trim();

        if (ket.isEmpty() || nom.isEmpty()) {
            Toast.makeText(this, "Keterangan dan nominal wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        long nominal;
        try {
            nominal = Long.parseLong(nom);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Nominal tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        String jenis = rbMasuk.isChecked() ? "MASUK" : "KELUAR";
        String tanggal = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID")).format(new Date());

        db.tambah(ket, nominal, jenis, tanggal);
        etKeterangan.setText("");
        etNominal.setText("");
        muatData();
    }

    private void muatData() {
        data.clear();
        data.addAll(db.semua());
        adapter.notifyDataSetChanged();

        long masuk = db.total("MASUK");
        long keluar = db.total("KELUAR");
        tvSaldo.setText("Saldo: " + Transaksi.rupiah(masuk - keluar));

        long batas = pm.getBatas();
        if (batas > 0 && keluar > batas) {
            tvPeringatan.setText("Pengeluaran melewati batas " + Transaksi.rupiah(batas));
            tvPeringatan.setVisibility(TextView.VISIBLE);
        } else {
            tvPeringatan.setVisibility(TextView.GONE);
        }
    }
}
