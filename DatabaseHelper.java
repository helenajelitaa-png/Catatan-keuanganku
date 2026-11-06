package com.helena.keuanganku;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAMA = "keuangan.db";
    private static final int DB_VERSI = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAMA, null, DB_VERSI);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE transaksi (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "keterangan TEXT, nominal INTEGER, jenis TEXT, tanggal TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int lama, int baru) {
        db.execSQL("DROP TABLE IF EXISTS transaksi");
        onCreate(db);
    }

    public void tambah(String keterangan, long nominal, String jenis, String tanggal) {
        ContentValues cv = new ContentValues();
        cv.put("keterangan", keterangan);
        cv.put("nominal", nominal);
        cv.put("jenis", jenis);
        cv.put("tanggal", tanggal);
        getWritableDatabase().insert("transaksi", null, cv);
    }

    public void hapus(int id) {
        getWritableDatabase().delete("transaksi", "id=?", new String[]{String.valueOf(id)});
    }

    public List<Transaksi> semua() {
        List<Transaksi> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id, keterangan, nominal, jenis, tanggal FROM transaksi ORDER BY id DESC", null);
        while (c.moveToNext()) {
            list.add(new Transaksi(c.getInt(0), c.getString(1), c.getLong(2),
                    c.getString(3), c.getString(4)));
        }
        c.close();
        return list;
    }

    public long total(String jenis) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT COALESCE(SUM(nominal), 0) FROM transaksi WHERE jenis=?", new String[]{jenis});
        long hasil = 0;
        if (c.moveToFirst()) hasil = c.getLong(0);
        c.close();
        return hasil;
    }
}
