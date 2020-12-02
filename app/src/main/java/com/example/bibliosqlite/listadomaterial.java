package com.example.bibliosqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class listadomaterial extends AppCompatActivity {

    ListView lismaterial;
    basedatossqlite osqlite = new basedatossqlite(this, "BDMATERIAL",null,1 );
    ArrayList<String> arraymaterial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listadomaterial);

        lismaterial = findViewById(R.id.lvmaterial);

        metodolistarmaterial();


    }

    private void metodolistarmaterial() {

        Toast.makeText(getApplicationContext(), "se cargaran los materiales", Toast.LENGTH_SHORT).show();

        arraymaterial = listadomaterial2();

        ArrayAdapter<String> adapmaterial= new ArrayAdapter <String> (this, android.R.layout.simple_expandable_list_item_1, arraymaterial);

        lismaterial.setAdapter(adapmaterial);

    }

    private ArrayList <String> listadomaterial2() {


        ArrayList <String> datos = new ArrayList<String>();

        SQLiteDatabase bd = osqlite.getReadableDatabase();

        String sql = "select idmat, nombre, genero, email from material";

        Cursor cursorMaterial = bd.rawQuery(sql,null);

        if (cursorMaterial.moveToFirst())
        {
            do {

                String generomat = "lenguajes";

                if (cursorMaterial.getString(2 ).equals("1"))
                {
                    generomat = "bases de datos";
                }

                String registro = " "+cursorMaterial.getString(0) + "  " + cursorMaterial.getString(1)  + "  " + generomat;

                datos.add(registro);
            }
            while (cursorMaterial.moveToNext());
        }else {
            Toast.makeText(getApplicationContext(),"No se encontraron elementos",Toast.LENGTH_SHORT).show();
        }
        bd.close();
        return datos;
    }
}
