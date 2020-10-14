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
    ArrayList<String> arraymaterial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listadomaterial);



        lismaterial = findViewById(R.id.lvmaterial);

        metodolistarmaterial(); // se crea metodo cargar usuarios


    }

    private void metodolistarmaterial() {  // se crea metodo cargar usuarios  para que al abrir el listado aparezca mensaje, no es necesario ponerlo

        Toast.makeText(getApplicationContext(), "se cargaran los usuarios", Toast.LENGTH_SHORT).show();

        arraymaterial = listadomaterial2(); // se crea metodo listadousuarios para poder crear el array que almacene los datos

        // esto pasa toda la info del arrayusuarios al adaptador adapusuarios
        ArrayAdapter<String> adapmaterial= new ArrayAdapter <String> (this, android.R.layout.simple_expandable_list_item_1, arraymaterial);

        lismaterial.setAdapter(adapmaterial); // esto pasa el arrary adapter adapusuarios y lo pasa al ListView listausuarios

    }

    private ArrayList <String> listadomaterial2() {

        ArrayList <String> datos = new ArrayList<String>();  // creamos el array con la variable datos

        basedatossqlite osql = new basedatossqlite(this,"bdbiblioteca",null,1);

        SQLiteDatabase bd = osql.getReadableDatabase();

        String sql = "select idmat, nombre, genero from material";  // aca le decimos que nos traiga todos los elementos de la tabla usuario, por eso el *

        Cursor curlistado = bd.rawQuery(sql,null);

        if (curlistado.moveToFirst())
        {
            do { // do while, es el ciclo mientras, con la exepcion de que entra al menos 1 vez al ciclo


                String generomat = "Lenguaje";  // se inicia una variable como invitado

                if (curlistado.getString(0 ).equals("1")) //se compara esta variable, si es 1 es igual a administrador, sino sigue como invitado
                {
                    generomat = "Base de datos";
                }

                // el numero que va en () es la posicion del array que deben traer, si quiero que me traiga varios datos debo hacer varias lienas
                String registro = "              "+ curlistado.getString(0) + "              " + curlistado.getString(1)  + "               " + generomat;

                datos.add(registro); // esta liena es la que añade lo que traiga de la BD en el arreglo
            }
            while (curlistado.moveToNext()); // esto lo que dice es que mientras analiza los registros del do se mueva al siguiente registro hasta el fin del archivo
        }
        bd.close(); // al moverse todos los registros con el while se cierra la base de datos
        return datos; // al recorrer la bd y almacenarlos en el arrar, cierra la BD y devuelve toda la informacion en la variable datos al arrayusuarios en la variable lostadousuarios
    }
}
