package com.helena.keuanganku;

import java.text.NumberFormat;
import java.util.Locale;

public class Transaksi {
    public int id;
    public String keterangan;
    public long nominal;
    public String jenis; // "MASUK" atau "KELUAR"
    public String tanggal;

    public Transaksi(int id, String keterangan, long nominal, String jenis, String tanggal) {
        this.id = id;
        this.keterangan = keterangan;
        this.nominal = nominal;
        this.jenis = jenis;
        this.tanggal = tanggal;
    }

    public static String rupiah(long angka) {
        return "Rp " + NumberFormat.getInstance(new Locale("id", "ID")).format(angka);
    }

    @Override
    public String toString() {
        String tanda = jenis.equals("MASUK") ? "+ " : "- ";
        return tanda + rupiah(nominal) + "\n" + keterangan + " (" + tanggal + ")";
    }
}
