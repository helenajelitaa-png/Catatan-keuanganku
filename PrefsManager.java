package com.helena.keuanganku;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsManager {
    private static final String NAMA_FILE = "pengaturan_keuanganku";
    private static final String KEY_NAMA = "nama_pengguna";
    private static final String KEY_GELAP = "mode_gelap";
    private static final String KEY_BATAS = "batas_pengeluaran";

    private final SharedPreferences prefs;

    public PrefsManager(Context context) {
        prefs = context.getSharedPreferences(NAMA_FILE, Context.MODE_PRIVATE);
    }

    public void simpanNama(String nama) {
        prefs.edit().putString(KEY_NAMA, nama).apply();
    }

    public String getNama() {
        return prefs.getString(KEY_NAMA, "");
    }

    public void simpanModeGelap(boolean aktif) {
        prefs.edit().putBoolean(KEY_GELAP, aktif).apply();
    }

    public boolean isModeGelap() {
        return prefs.getBoolean(KEY_GELAP, false);
    }

    public void simpanBatas(long batas) {
        prefs.edit().putLong(KEY_BATAS, batas).apply();
    }

    public long getBatas() {
        return prefs.getLong(KEY_BATAS, 0);
    }
}
