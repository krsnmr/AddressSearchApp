package com.example.addresssearchapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "/storage/emulated/0/AisBrigade/BaltMapSdk/Address.sqlite";
    public static final String TABLE_NAME = "";
    public static final String COL_NAME_1 = "addres_name";
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        return;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        return;
    }

    /*
    * Получить из БД адреса
    * */
    public List<Address> getAddrList(String addrStr) {
        File file = new File(DATABASE_NAME);
        List<Address> list = new ArrayList<>();
        if (file.exists() && !file.isDirectory()) {
            Log.d(TAG, "showData:  file.exists");
        }

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT addr_id, addr_name " +
                "FROM mv_telros_address_view " +
                "WHERE addr_name_low like ? " +
                "ORDER BY prefiks_name, addr_whole_nmb, addr_build  " +
                "LIMIT 10";

        String[] ar = addrStr.split("\\s|\\t|,|;|\\.|\\?|!|-|:|@|\\[|\\]|\\(|\\)|\\{|\\}|_|\\*|/");
        List<String> addrList = Arrays.asList(ar);
        String addrSearch = JoinStringList(addrList,"%");


        Cursor data = db.rawQuery(query, new String[]{"%" + addrSearch + "%"});
        while (data.moveToNext()) {
            int id = data.getInt(0);
            String name = data.getString(1);
            Log.d(TAG, "getAddrList: id-" + id + ";name-" + name);
            Address adr = new Address(id, name);
            list.add(adr);
        }
        return list;
    }


    /*
    Соединить элементы в строку
     */
    public static String JoinStringList(List<String> list, String txt) {
        StringBuilder sb = new StringBuilder();
        int count = list.size();
        for (int i = 0; i < count; i++) {
            sb.append(list.get(i));
            if (i < (count-1)) sb.append(txt);
        }
        return sb.toString();
    }
}
