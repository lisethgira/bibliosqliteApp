package com.example.bibliosqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class listados extends AppCompatActivity {
    ListView listausuarios;
    ArrayList <String> arrayusuarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listados);
        listausuarios = findViewById(R.id.lvusuarios);

        CargarUsuarios();


    }

    private void CargarUsuarios() {
        arrayusuarios = ListadoUsuarios();
        ArrayAdapter<String> adapterpUsuarios = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, arrayusuarios);
        listausuarios.setAdapter(adapterpUsuarios);


    }

    private ArrayList<String> ListadoUsuarios() {
        ArrayList<String> datos = new ArrayList<String>();
        basedatossqlite osql = new basedatossqlite(this, "BDBIBLIOTECA",null,1 );
        SQLiteDatabase db = osql.getReadableDatabase();
        String query = "SELECT email, nombre, rol FROM Usuario";
        Cursor cursorUsuario = db.rawQuery(query, null);

        if (cursorUsuario.moveToFirst()){
            do{
                String tipou ="Invitado";
                if (cursorUsuario.getString(2).equals("1")){
                    tipou = "Administrador";

                }
                String linea ="Email: "+ cursorUsuario.getString(0)+" -"+ " Nombre: "+ cursorUsuario.getString(1)+" -"+" Tipo de usuario: "+tipou;
                datos.add(linea);

            }while (cursorUsuario.moveToNext());
        }
        db.close();
        return datos;



    }

}
