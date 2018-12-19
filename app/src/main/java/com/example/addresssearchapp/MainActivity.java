package com.example.addresssearchapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView textView;
    private Address selectedAddress;
    private static final String TAG = "MainActivity";
    Button btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
        // очистить строку поиска
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
            }
        });

        // Событие выбора из списка найденного адреса
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Address oAddress = (Address) adapterView.getItemAtPosition(pos);
                Log.d(TAG, "onItemClick: id: " + oAddress.getId() + "; name: " + oAddress.getAddrName() + " ");
            }
        });

        getExternalStoragePermission();
    }

    /* Инициализация поля поиска
     * */
    private void InitAutoComplTextViev() {
        Log.d(TAG, "InitAutoComplTextViev: ");
        textView.setThreshold(1);
        List<Address> list1 = new ArrayList<>();
        AddressListAdapter adapter1 = new AddressListAdapter(MainActivity.this, R.id.autocomplete_country, list1);
        textView.setAdapter(adapter1);

    }

    // Для запроса привилегий на доступ к файлу Address.sqlite
    public static final String EXTERNAL_STORAGE_PERM = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    boolean mLocationPermissionGranted = false;
    private static final int EXTERNAL_STORAGE_PERM_CODE = 1234;
    /**
     * Запросить полномочия на запись - WRITE_EXTERNAL_STORAGE инициализпровать AutoCompleteTextView
     */
    public void getExternalStoragePermission() {
        Log.d(TAG, "getExternalStoragePermission: getting External Storage permisssions");
        String[] permissions = {EXTERNAL_STORAGE_PERM};//{FINE_LOCATION, COURSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), EXTERNAL_STORAGE_PERM)
                == PackageManager.PERMISSION_GRANTED) {
            // полномочия уже есть, инициализировать
            mLocationPermissionGranted = true;
            InitAutoComplTextViev();
        } else {
            // если ещё нет, то запросить
            ActivityCompat.requestPermissions(this, permissions, EXTERNAL_STORAGE_PERM_CODE);
        }
    }

    // событие происходит при вызове запроса полномочий - ActivityCompat.requestPermissions...
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case EXTERNAL_STORAGE_PERM_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionGranted = true;
                    // если получили полномочия, то загрузить данные
                    InitAutoComplTextViev();
                }
            }
        }
    }
}
