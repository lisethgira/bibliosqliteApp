package com.example.bibliosqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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

    EditText emailu, nombreu, passu;
    RadioButton adminu, usuu;
    Button agregaru, buscaru, actualziaru, eliminaru, listaru;

    String emailanterior, emailnuevo;


    //instanciar la base de datos, se pone aca para volverla variable global, el nombre de la BD solo se pone cuando se instancia desde las clases java
    basedatossqlite osql = new basedatossqlite(this, "bdbiblioteca", null, 1);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actusuario);


        emailu = findViewById(R.id.etemailu);
        nombreu = findViewById(R.id.etnombreu);
        passu = findViewById(R.id.etpassu);
        adminu = findViewById(R.id.rbadminu);
        usuu = findViewById(R.id.rbusuu);
        agregaru = findViewById(R.id.btagregaru);
        buscaru = findViewById(R.id.btbuscaru);
        actualziaru = findViewById(R.id.btactualizaru);
        eliminaru = findViewById(R.id.bteliminaru);
        listaru = findViewById(R.id.btlistaru);


        // instanciar el boton agregar y hacerle metodo onclick

        agregaru.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                metidoagregarusuario();
            }
        });


        // boton listar

        listaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), listados.class));
            }
        });



        // boton buscar

        buscaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buscarusuario(emailu.getText().toString().trim());
            }
        });



        // boton actualizar
        actualziaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String mrol = "0";

                if(adminu.isChecked())
                {
                    mrol = "1";
                }
                actualizarusuario (emailu.getText().toString(), nombreu.getText().toString(), passu.getText().toString(), mrol);  // se crea metodo
            }
        });

        // boton de eliminar
        eliminaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    // metodo actualizar
    private void actualizarusuario(String emailuact, String nombreuact, String passuact, String mrolact) {


        emailnuevo = emailuact.trim(); // se crea variable para guardar lo que se traiga de emailact que a su vez toma lo traido en emailu.getText().toString()

        // se llaman las bases de datos
        SQLiteDatabase obde = osql.getWritableDatabase();
        SQLiteDatabase bd = osql.getReadableDatabase();

        if (emailnuevo.equals(emailanterior.trim())) // se comprara email nuevo con email viejo
        {
            obde.execSQL("UPDATE usuario SET nombre = '"+nombreuact+"', clave = '"+passuact+"', rol = '"+mrolact+"' where email = '"+emailnuevo+"'"); // si son iguales, cambia los datos sin el email

            Toast.makeText(getApplicationContext(),"Contacto actualizado correctamente", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // buscar email nuevo para verificar que no este asignado a otro usuario

            String sql = "select email, nombre, clave, rol From usuario where email = '" + emailnuevo + "'";  // si emailnuevo e email viejo son diferentes compra email nuevo
            Cursor cusuari = bd.rawQuery(sql, null); // guarda los datos en tabla cursor

            if (cusuari.moveToFirst()) // si encontro el emailnuevo lanza mensaje
            {
                Toast.makeText(getApplicationContext(),"El email esta asignado a otro usuario", Toast.LENGTH_SHORT).show();
            }
            else
            {
                // este es el comando update para modificar los datos en la tabla incluyendo el email

                obde.execSQL("UPDATE usuario SET email = '"+emailnuevo+"', nombre = '"+nombreuact+"', clave = '"+passuact+"', rol = '"+mrolact+"' where email = '"+emailanterior+"'"); // si no encuenta emailnuevo  actualzia todo

                Toast.makeText(getApplicationContext(),"Contacto actualizado correctamente", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // metodo buscar
    private void buscarusuario(String emailbuscar) {

        if (!emailbuscar.isEmpty()) // revisa si no ingresa email entonces apoarece mensaje que dice ingrese email a buscar
        {
            // buscarusuario(emailu.getText().toString().trim());

            SQLiteDatabase bd = osql.getReadableDatabase(); // instanciamos la base de datos

            // lo que quiere decir es que seleccione todos los campos de la tabla usuario y compare el email ingresado (emailbuscar) lo compare con el email
            String sql = "select email, nombre, clave, rol From usuario where email = '" + emailbuscar + "'";

            // se crea una tabla cursor que se almacena en la memoria ram y contiene los registros de la instruccion select, se debe almacenar en una tabla en memoria antes de guar en la tabla de sql
            Cursor cusuari = bd.rawQuery(sql, null);


            // esto trae todos los datos del usuario en dependencia al orden del String sql
            if (cusuari.moveToFirst()) {

                nombreu.setText(cusuari.getString(1));
                passu.setText(cusuari.getString(2));

                emailanterior = cusuari.getString(0); // al crear la variable global se le esta diciendo que almacene ahi lo que tenga tabla cursor en posicion 0

                if (cusuari.getString(3).equals("1")) // verifica el radiobuttom para traser encendido el que corresponda al usuario
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
                // el email no esta registrado
                Toast.makeText(getApplicationContext(), "¡Error!. el usuario con email " + emailbuscar + " no existe", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Ingrese un email a buscar", Toast.LENGTH_SHORT).show();
        }

    }
    // ---------------  MENU ----------------------

    // antes de cerrar la llave de la actividad de invoca el metodo del sobrecargado del menu

   // el metodo se crea dando click derecho generate, selecciona overrrite methods y selecciona el oncreateoptionmenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater infcrud = getMenuInflater(); // esto crea una variable apra inflar el menu
        infcrud.inflate(R.menu.menu_crud, menu);  // esto trae el archivo xml menu_crud desde la carpeta menu
        return super.onCreateOptionsMenu(menu);
    }

    //se crea otro metodo de sobrecargado para el evento click y asi escuchar donde le da usuario

    // el metodo se crea dando click derecho generate, selecciona overrrite methods y selecciona el optionitemselectes - tambien se puede hacer con CTRL+O


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.menuagregar: // se refencia el id del xml agregar
                metidoagregarusuario(); // esto trase el metodo agregar usuario -- SIEMPRE QUE SE CREE UN MENU DEBE TENER UN METODO APRA INSTANCIAR
            return true;

            case R.id.menubuscar:
                buscarusuario(emailu.getText().toString().trim());  // se trae en el metodo buscar el emmailu ya qye esta en una variable global
            return true;

            case R.id.menuactualizar:
                String mrol = "0";

                if(adminu.isChecked())
                {
                    mrol = "1";
                }
                actualizarusuario (emailu.getText().toString(), nombreu.getText().toString(), passu.getText().toString(), mrol);  // se crea metodo
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void metidoagregarusuario() {
        if (!emailu.getText().toString().isEmpty() && !nombreu.getText().toString().isEmpty() && !passu.getText().toString().isEmpty() && adminu.isChecked() || usuu.isChecked())
        {
            //instanciar un objeto de la clase base de datos para que guarde datos en ella -------- linea 17 ---------

            // se instania un objeto para manipular la BD
            SQLiteDatabase bd = osql.getReadableDatabase();  // esto permite traer la base de datos, leerla y comparar datos.

            // screa la variable sql para la instruccion que permite busar el email.
            String sql = "select email From usuario where email = '" + emailu.getText().toString() + "'";  // lo que queire decir es que recupere lo que hay en la tabla usuario, la columna email.

            // se crea una tabla cursor que se almacena en la memoria ram y contiene los registros de la instruccion select, se debe almacenar en una tabla en memoria antes de guar en la tabla de sql
            Cursor cusuari = bd.rawQuery(sql, null);

            // verifica si la tabla cursor tiene al menos 1 registro
            if (cusuari.moveToFirst()) {
                // el email ya se encuentra registrado
                Toast.makeText(getApplicationContext(), "¡Error!. Email asignado a otro usuario", Toast.LENGTH_SHORT).show();
            } else {

                // instanciar objeto de la BD para guardar el usuario si no existe en modo escritura
                SQLiteDatabase bd1 = osql.getWritableDatabase();

                try {

                    //contenedor de datos del usuario
                    ContentValues contusuario = new ContentValues();
                    contusuario.put("email", emailu.getText().toString().trim());   // lo que esta diciendo es que una tabla provicional contusuario le guarde lo que esta en emailu
                    // el trim corta los espacios que quedan a la derecha del edittext
                    // la tabla Cursor es de solo lectura y el contentvalues es para modificar datos

                    contusuario.put("nombre", nombreu.getText().toString().trim());
                    contusuario.put("clave", passu.getText().toString().trim());

                    if (adminu.isChecked())  // estamos chequeando si el rol es administrador o usuario
                    {
                        contusuario.put("rol", "1");
                    } else {
                        contusuario.put("rol", "0");
                    }

                    bd1.insert("usuario", null, contusuario); // indica que el va a insertar en la tabla usuario lo que esta almacenado en la tabla provicional contusuario

                    bd1.close(); // cierra la base de datos

                    Toast.makeText(getApplicationContext(), "Usuario agregado correctamente", Toast.LENGTH_SHORT).show();
                } catch (Exception e) // se usa el catch para que el programa no se dispare y en lugar de eso saque un error
                {
                    Toast.makeText(getApplicationContext(), "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }
        else{
            Toast.makeText(getApplicationContext(),"debe ingresar todos los datos del usuario", Toast.LENGTH_LONG).show();
        }
    }
}