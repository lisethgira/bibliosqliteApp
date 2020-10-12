package com.example.bibliosqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText email,pass;
    Button iniciarsesion;
    TextView registrarse;

    basedatossqlite osql = new basedatossqlite(this, "bdbiblioteca", null,1); // se instancia la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.etemail);
        pass = findViewById(R.id.etpass);
        iniciarsesion = findViewById(R.id.btniniciarsesion);
        registrarse = findViewById(R.id.tvregistrar);


        registrarse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),actusuario.class));

            }
        });

        iniciarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                iniciarsesionb (email.getText().toString().trim(), pass.getText().toString().trim());

            }

            private void iniciarsesionb(String memail, String mpass) { // se crean variables que van a contener en orden lo que trae el setonclick de inisiarsesion


                SQLiteDatabase bd = osql.getReadableDatabase();  // se crea variable para instanciar el osql para que lea la informacion de la base de datos

                String sql = "select rol, email from usuario where email = '"+memail+"' and clave = '"+mpass+"'"; // aca buscara en la base de datos y comparara el email y contraseña

                Cursor cuinisiar = bd.rawQuery(sql,null);

                if (cuinisiar.moveToFirst())
                {

                    String mrol = cuinisiar.getString(0); // se le asignan a esas variables lo que esta en la tabla cursor en pa posicion indicada
                    String memail1 = cuinisiar.getString(1);

                    // invocar la actividad material

                    Intent oactmaterial = new Intent(getApplicationContext(), actmaterial.class); // se crea intent para almacenar variables que se enviarana  al otra actividad

                    oactmaterial.putExtra("role", mrol); // se le dice que envie a la actividad material en la variable oactmaterial lo que tiene rol
                    oactmaterial.putExtra("emaile", memail1);

                    startActivity(oactmaterial); // se llama la nueva actividad con lso datos almacenados en el oactmaterial

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Usiario Invalido",Toast.LENGTH_SHORT).show();
                }

            }
        });




    }


}