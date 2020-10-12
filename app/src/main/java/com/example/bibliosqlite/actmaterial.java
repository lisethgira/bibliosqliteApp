package com.example.bibliosqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class actmaterial extends AppCompatActivity {

    EditText idmat, emailu, nombremat;
    RadioButton lenguaje, basedatos;
    Button agregarm, buscarm, actualziarm, listarm, eliminarm;

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


    }
}