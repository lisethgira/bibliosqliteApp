package com.example.bibliosqlite;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText email, contrasena;
    Button iniciarSesion;
    TextView  registrarse;
    basedatossqlite osqlite = new basedatossqlite(this, "BDBIBLIOTECA", null, 1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email= findViewById(R.id.etemail);
        contrasena = findViewById(R.id.etpass);
        iniciarSesion = findViewById(R.id.btniniciarsesion);
        registrarse = findViewById(R.id.tvregistrar);

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), actusuario.class));


            }
        });

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarsesionb(email.getText().toString().trim(), contrasena.getText().toString().trim());


            }

            private void iniciarsesionb(String memail, String mcontra) {

                SQLiteDatabase db = osqlite.getReadableDatabase(); //Leer es aplicar SELECT
                //Crear una vble para la instrucción que permite buscar el email
                String query = "Select rol, email From Usuario Where email = '" + memail + "'AND clave = '" + mcontra +"'";
                //Crear una tabla cursor (ram) para almacenar los registros que devuelve la instrucción SELECT
                Cursor cusuario = db.rawQuery(query, null);
                //Verificar si la tabla cursor tiene al menos un registro
                if (cusuario.moveToFirst()) //Si está ubicado en el primer registro de la tabla cursor
                {
                    //String mrol = cusuario.getString(0);
                    //String memail1 = cusuario.getString(1);
                    //invocar la actividad actmaterial, enviando mrol y memail1
                    Intent oactmaterial = new Intent(getApplicationContext(),actmaterial.class);
                    oactmaterial.putExtra("role",cusuario.getString(0));
                    oactmaterial.putExtra("emaile",memail);
                    startActivity(oactmaterial);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Usuario Inválido",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
}
