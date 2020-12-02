package com.example.bibliosqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class actusuario extends AppCompatActivity {
    EditText emailu, nombreu, passwordu;
    RadioButton adminu, usuu;
    Button agregaru, buscaru, actualizaru, eliminaru, listaru;
    String emailanterior, emailnuevo;

    //se instancia la base de datos
    basedatossqlite osqlite = new basedatossqlite(this, "BDBIBLIOTECA", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actusuario);
        emailu = findViewById(R.id.etemailu);
        nombreu = findViewById(R.id.etnombreu);
        passwordu = findViewById(R.id.etpassu);
        adminu = findViewById(R.id.rbadminu);
        usuu = findViewById(R.id.rbusuu);
        agregaru = findViewById(R.id.btagregaru);
        buscaru = findViewById(R.id.btbuscaru);
        actualizaru = findViewById(R.id.btactualizaru);
        eliminaru = findViewById(R.id.bteliminaru);
        listaru = findViewById(R.id.btlistaru);

        eliminaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarUsuarios();
            }
        });

        actualizaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actualizausuario (emailu.getText().toString(), nombreu.getText().toString(), passwordu.getText().toString(),adminu,usuu);
            }
        });

        buscaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarusuario(emailu.getText().toString().trim());

            }

        });


        listaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), listados.class));
            }
        });

        agregaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarUsuario();

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater refemenu = getMenuInflater();
        refemenu.inflate(R.menu.menu_crud,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuagregar:
                agregarUsuario();
                return true;
            case R.id.menubuscar:
                buscarusuario(emailu.getText().toString().trim());
                return true;
            case R.id.menuactualizar:
                actualizausuario (emailu.getText().toString(), nombreu.getText().toString(), passwordu.getText().toString(),adminu,usuu);
                return true;
            case R.id.menueliminar:
                eliminarUsuarios();
                return true;
            case R.id.menulistar:
                startActivity(new Intent(getApplicationContext(), listados.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void agregarUsuario (){
        String memail = emailu.getText().toString();
        String mnombre = nombreu.getText().toString();
        String mpassword = passwordu.getText().toString();


        if(!memail.isEmpty() && !mnombre.isEmpty() && !mpassword.isEmpty() && (adminu.isChecked()||(usuu.isChecked()))) {


            //instanciar un objeto ara manipular la base de daos

            SQLiteDatabase db = osqlite.getReadableDatabase();//leer es aplicar SELECT
            String sql = "Select email From Usuario Where email = '" + emailu.getText().toString() + "'";// Crear una variable
            //crear tabla cursor para almacenar registros que devuelve la instrucción Select
            Cursor cusuario = db.rawQuery(sql, null);
            //verificar si la tabla cursos está vacía o tiene al menos un regístro
            //si está ubicado en el primer registro de la tabla cursor
            if (cusuario.moveToFirst()) {
                Toast.makeText(getApplicationContext(), "Error!. Email asignado a otro usuario", Toast.LENGTH_SHORT).show();
            } else {
                SQLiteDatabase db1 = osqlite.getWritableDatabase();
                try {
                    //Contenedor de datos del contacto
                    ContentValues contusuario = new ContentValues();
                    contusuario.put("email", emailu.getText().toString().trim());
                    contusuario.put("nombre", nombreu.getText().toString().trim());
                    contusuario.put("clave", passwordu.getText().toString().trim());
                    if (adminu.isChecked()) {
                        contusuario.put("rol", "1");
                    } else {
                        contusuario.put("rol", "0");
                    }

                    db1.insert("Usuario", null, contusuario);
                    db1.close();
                    Toast.makeText(getApplicationContext(), "Contacto agregado correctamente...", Toast.LENGTH_SHORT).show();
                    emailu.setText("");
                    nombreu.setText("");
                    passwordu.setText("");
                    adminu.setChecked(false);
                    usuu.setChecked(false);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            Toast.makeText(getApplicationContext(), "Debe ingresar todos los datos del usuario", Toast.LENGTH_SHORT).show();

        }

    }
    private void buscarusuario(String emailbuscar) {
        if(emailbuscar.isEmpty()){
            Toast.makeText(getApplicationContext(),"Es Necesario completar el email",Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = osqlite.getReadableDatabase(); //Leer es aplicar SELECT
        //Crear una vble para la instrucción que permite buscar el email
        String sql = "Select email,nombre,clave,rol From Usuario Where email = '" + emailbuscar + "'";
        //Crear una tabla cursor (ram) para almacenar los registros que devuelve la instrucción SELECT
        Cursor cusuario = db.rawQuery(sql, null);
        //Verificar si la tabla cursor tiene al menos un registro
        if (cusuario.moveToFirst()) //Si está ubicado en el primer registro de la tabla cursor
        {
            //Lo encontró (email)
            nombreu.setText(cusuario.getString(1));
            passwordu.setText(cusuario.getString(2));
            emailanterior = cusuario.getString(0);
            if (cusuario.getString(3).equals("1"))
            {
                adminu.setChecked(true);
            }
            else
            {
                usuu.setChecked(true);
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "El usuario con email:"+emailbuscar+" NO existe...", Toast.LENGTH_SHORT).show();
        }

    }
    private void eliminarUsuarios() {

        AlertDialog.Builder ventana = new AlertDialog.Builder(actusuario.this);
        ventana.setMessage("Está seguro de eliminar el usuario: "+nombreu.getText().toString()+"?");
        ventana.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SQLiteDatabase obde = osqlite.getWritableDatabase();
                        obde.execSQL("DELETE FROM usuario WHERE email = '"+emailu.getText().toString()+"'");
                        Toast.makeText(getApplicationContext(),"Usuario Eliminado correctamente...",Toast.LENGTH_SHORT).show();
                        obde.close();
                    }
                });

        ventana.setNegativeButton("No",
                new DialogInterface.OnClickListener() {



                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(getApplicationContext(),"Usuario no eliminado", Toast.LENGTH_SHORT).show();

                    }
                });

        AlertDialog alertDialog = ventana.create();
        alertDialog.show();

    }
    private void actualizausuario(String emailact, String nombreact, String passwordact,RadioButton administrador, RadioButton usuario) {
        if (validarCampos("Debe ingresar el email",emailu)){
            return;
        }
        if (validarCampos("Debe ingresar el Nombre del usuario",nombreu)){
            return;
        }
        if (validarCampos("Debes ingresar la contraseña",passwordu)){
            return;
        }
        if (!adminu.isChecked()&& !usuu.isChecked()){
            Toast.makeText(getApplicationContext(),"Debes Seleccionar un rol",Toast.LENGTH_SHORT).show();
            return;
        }

        String rolact ="0";
        if (adminu.isChecked()){
            rolact ="1";

        }
        emailnuevo = emailact.trim();
        SQLiteDatabase dbact = osqlite.getWritableDatabase();
        //Buscar el email nuevo para verificar que no esté asignado en otro usuario
        SQLiteDatabase db = osqlite.getReadableDatabase(); //leer es aplicar SELECT
        if(emailnuevo.equals(emailanterior.trim())){
            dbact.execSQL("UPDATE Usuario SET nombre = '"+nombreact+"', clave = '"+passwordact+"', rol = '"+rolact+"' WHERE  email = '"+emailanterior+"'");
            Toast.makeText(getApplicationContext(),"Contacto Actualizado correctamente...",Toast.LENGTH_SHORT).show();

        }else {
            //Buscar emailnuevo
            //Crear una vble para la instrucción que permite buscar el email
            String sql = "Select email From Usuario Where email = '" + emailnuevo + "'";
            //Crear una tabla cursor (ram) para almacenar los registros que devuelve la instrucción SELECT
            Cursor cusuario = db.rawQuery(sql, null);
            //Verificar si la tabla cursor tiene al menos un registro
            if (cusuario.moveToFirst()) //Si está ubicado en el primer registro de la tabla cursor
            {
                Toast.makeText(getApplicationContext(),"El email está asignado a otro usuario!! ...",Toast.LENGTH_SHORT).show();
            }
            else
            {
                dbact.execSQL("UPDATE Usuario SET email = '"+emailnuevo+"', nombre = '"+nombreact+"', clave = '"+passwordact+"', rol = '"+rolact+"' WHERE  email = '"+emailanterior+"'");
                Toast.makeText(getApplicationContext(),"Contacto Actualizado correctamente...",Toast.LENGTH_SHORT).show();
            }

        }


    }
    private boolean validarCampos( String mensaje, EditText campoValidar) {
        if(campoValidar.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_SHORT).show();
            return true;
        }else{
            return false;
        }
    }



}
