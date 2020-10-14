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

public class actmaterial extends AppCompatActivity {

    EditText idmat, emailu, nombremat;
    RadioButton lenguaje, basedatos;
    Button agregarm, buscarm, actualziarm, listarm, eliminarm;

    String idmatnuevo, idmatviejo;

    basedatossqlite osql = new basedatossqlite(this, "bdbiblioteca", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actmaterial);

        idmat = findViewById(R.id.etidmatm);
        emailu = findViewById(R.id.etemailm);
        nombremat = findViewById(R.id.etnombrematu);

        lenguaje = findViewById(R.id.rblenguajem);
        basedatos = findViewById(R.id.rbbasedatosm);

        agregarm = findViewById(R.id.btagregarm);
        buscarm = findViewById(R.id.btbuscarm);
        actualziarm = findViewById(R.id.btactualizarm);
        listarm = findViewById(R.id.btlistarm);
        eliminarm = findViewById(R.id.bteliminarm);

        //resibir los datos enciados del mainactivity que se mandaron con el Intent

        String mrol = getIntent().getStringExtra("role"); // esto resibe lo que se envie en el intent en con variable role
        String memail = getIntent().getStringExtra("emaile");

        emailu.setText(memail);


        // verifica el rol teniendo en cuenta que en la actividad usuario de inicializo en 0

        if (mrol.equals("1"))
        {
            // activar todos los botones

            agregarm.setEnabled(true);
            buscarm.setEnabled(true);
            actualziarm.setEnabled(true);
            listarm.setEnabled(true);
            eliminarm.setEnabled(true);

        }
        else
        {
            // este desactiva todos los botones ya que si viene en 0 es usuario invitado

            agregarm.setEnabled(false);
            buscarm.setEnabled(true);
            actualziarm.setEnabled(false);
            listarm.setEnabled(true);
            eliminarm.setEnabled(false);
        }

        // boton agregar

        agregarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                metodoagregar();

            }
        });

        buscarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                metodobuscar();

            }
        });

        actualziarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                metodoactualizar();
            }
        });

        eliminarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                metodoeliminar();

            }
        });

        listarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                metodolistar();
            }
        });


    }

    private void metodolistar() {
        startActivity(new Intent(getApplicationContext(), listadomaterial.class));
    }

    private void metodoeliminar() {

        AlertDialog.Builder alertacuadro = new AlertDialog.Builder(actmaterial.this);
        alertacuadro.setMessage("Eliminara el libro");
        alertacuadro.setPositiveButton("Sí",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                SQLiteDatabase bd1 = osql.getWritableDatabase();

                bd1.execSQL("DELETE FROM material WHERE idmat = '"+idmat.getText().toString()+"'");

                idmat.setText("");
                nombremat.setText("");
                lenguaje.setChecked(false);
                basedatos.setChecked(false);

                Toast.makeText(getApplicationContext(),"Libro Eliminado Correctamente",Toast.LENGTH_SHORT).show();
            }
        });

        alertacuadro.setNegativeButton("No",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertacuadro.create();
        alertDialog.show();
    }

    private void metodoactualizar() {

        idmatnuevo = idmat.getText().toString();

        SQLiteDatabase bd = osql.getReadableDatabase();
        SQLiteDatabase bd1 = osql.getWritableDatabase();

        String generom = "0";

        if (basedatos.isChecked())
        {
            generom = "1";
        }

        if (idmatnuevo.equals(idmatviejo.trim()))
        {
            bd1.execSQL(" UPDATE material SET nombre = '"+nombremat.getText().toString()+"', genero = '"+generom+"' where  idmat = '"+idmatnuevo+"' ");

            Toast.makeText(getApplicationContext(),"Libro actualizado correctamente", Toast.LENGTH_SHORT).show();

        }
        else
        {

            String sql = " select idmat, nombre, genero from material where idmat = '"+idmatnuevo+"'";

            Cursor cursomat = bd.rawQuery(sql,null);

            if (cursomat.moveToFirst())
            {
                Toast.makeText(getApplicationContext(),"Id ya esta asignado",Toast.LENGTH_SHORT).show();
            }
            else
            {
                bd1.execSQL(" UPDATE material SET  idmat = '"+idmatnuevo+"', nombre = '"+nombremat.getText().toString()+"', genero = '"+generom+"' where  idmat = '"+idmatviejo+"' ");

                Toast.makeText(getApplicationContext(),"Libro actualizado correctamente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void metodobuscar() {

        if (idmat.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Llene el id",Toast.LENGTH_SHORT).show();
        }
        else
        {

            SQLiteDatabase bd = osql.getReadableDatabase();

            String sql = " select idmat, email, nombre, genero from material where idmat = '"+idmat.getText().toString()+"'";

            Cursor cursomat = bd.rawQuery(sql,null);

            if (cursomat.moveToFirst())
            {
                idmatviejo = cursomat.getString(0);
                emailu.setText(cursomat.getString(1));
                nombremat.setText(cursomat.getString(2));

                if (cursomat.getString(3).equals("0"))
                {
                    lenguaje.setChecked(true);
                }
                else
                {
                    basedatos.setChecked(true);
                }

            }
            else
            {
                Toast.makeText(getApplicationContext(), "¡Error!. el libro con id " + idmat.getText().toString()+ " no existe", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void metodoagregar() {

        if (!idmat.getText().toString().isEmpty() && !nombremat.getText().toString().isEmpty() && lenguaje.isChecked() || basedatos.isChecked())
        {

            SQLiteDatabase bd = osql.getReadableDatabase();

            String sql = " select idmat from material where idmat = '"+idmat.getText().toString()+"'";

            Cursor cursomat = bd.rawQuery(sql,null);

            if (cursomat.moveToFirst())
            {
                Toast.makeText(getApplicationContext(),"El libro ya se encuentra agregado",Toast.LENGTH_SHORT).show();
            }
            else
            {
                SQLiteDatabase bd1 = osql.getWritableDatabase();

                try
                {

                    ContentValues contmaterial = new ContentValues();

                    contmaterial.put("idmat", idmat.getText().toString());
                    contmaterial.put("nombre", nombremat.getText().toString());
                    contmaterial.put("email", emailu.getText().toString());

                    if(lenguaje.isChecked())
                    {
                        contmaterial.put("genero", "0");
                    }
                    else
                    {
                        contmaterial.put("genero", "1");
                    }

                    bd1.insert("material", null, contmaterial);
                    bd1.close();

                    idmat.setText("");
                    nombremat.setText("");
                    lenguaje.setChecked(false);
                    basedatos.setChecked(false);

                    Toast.makeText(getApplicationContext(),"Libro agregado correctamente",Toast.LENGTH_SHORT).show();

                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


        }
        else
        {

            Toast.makeText(getApplicationContext(),"Ingrese todos los datos",Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater infcrud = getMenuInflater();
        infcrud.inflate(R.menu.menu_crud, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.menuagregar:
                metodoagregar();
                return true;
            case R.id.menubuscar:
                metodobuscar();
                return true;
            case R.id.menuactualizar:
                metodoactualizar();
                return true;
            case R.id.menulistar:
                metodolistar();
                return true;
            case R.id.menueliminar:
                metodoeliminar();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}