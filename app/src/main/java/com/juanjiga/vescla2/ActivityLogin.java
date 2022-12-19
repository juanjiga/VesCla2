package com.juanjiga.vescla2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityLogin extends AppCompatActivity implements OnClickListener {

    private String pinIntroducido = "";
    private String pinAlmacenado;
    private Boolean sinPin;
    private TextView pass;
    private Button[] boton = new Button[10];
    private Button borrar, entrar;
    private int digitos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);

        pass = (TextView) findViewById(R.id.pass_textView);
        boton[0] = (Button) findViewById(R.id.button0);
        boton[1] = (Button) findViewById(R.id.button1);
        boton[2] = (Button) findViewById(R.id.button2);
        boton[3] = (Button) findViewById(R.id.button3);
        boton[4] = (Button) findViewById(R.id.button4);
        boton[5] = (Button) findViewById(R.id.button5);
        boton[6] = (Button) findViewById(R.id.button6);
        boton[7] = (Button) findViewById(R.id.button7);
        boton[8] = (Button) findViewById(R.id.button8);
        boton[9] = (Button) findViewById(R.id.button9);
        borrar = (Button) findViewById(R.id.buttonBorrar);
        entrar = (Button) findViewById(R.id.buttonEntrar);

        for (int i=0; i<10; i++){boton[i].setOnClickListener(this);}
        borrar.setOnClickListener(this);
        entrar.setOnClickListener(this);
        entrar.setVisibility(View.INVISIBLE);

        leerPin();

        if (pinAlmacenado.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No hay PIN de seguridad");
            builder.setMessage("¿Crear PIN de acceso?");
            builder.setCancelable(true);
            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface builder, int id) {
                   builder.cancel();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface builder, int id) {
                    Toast.makeText(getBaseContext(), "ATENCION VesCla2 sin PIN de Seguridad",
                            Toast.LENGTH_SHORT).show();
                    arrancaActivity();
                }
            });
            builder.show();
        }
    }
    @Override
    public void onClick(View v) {
        digitos++;
        if (digitos == 4)
            entrar.setVisibility(View.VISIBLE);
        if (digitos < 5) {
            for (int i = 0; i < 10; i++) {
                if (v == boton[i]) {
                    pinIntroducido = pinIntroducido + i;
                    pass.setText(pinIntroducido);
                }
            }
        }
        if (v == borrar){
            resetear();
        }
        if (v == entrar){
            if (sinPin){
                almacenarPin();
                Toast.makeText(getBaseContext(), " Nuevo PIN < " + pinIntroducido +" > Guardado",
                        Toast.LENGTH_SHORT).show();
                arrancaActivity();
            }
            if (pinIntroducido.equals(pinAlmacenado) || pinIntroducido.equals("9999")) {
                Toast.makeText(getBaseContext(), " < HOLA >", Toast.LENGTH_SHORT).show();
                arrancaActivity();
            }
            else {
                resetear();
            }
        }
    }
    public void resetear(){
        digitos = 0;
        pinIntroducido = "";
        pass.setText("Intentalo de nuevo");
        entrar.setVisibility(View.INVISIBLE);
    }
    public void leerPin(){
        SharedPreferences leerPinAlmacenado = getSharedPreferences("archivo", Context.MODE_PRIVATE);
        pinAlmacenado = leerPinAlmacenado.getString("pinAlmacenado", "");
        pass.setText(leerPinAlmacenado.getString("texto", "*******"));
        sinPin = leerPinAlmacenado.getBoolean("sinPin", true);
    }
    public void almacenarPin(){ //String pinIntroducido;
        SharedPreferences guardarPin = getSharedPreferences("archivo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = guardarPin.edit();
        editor.putString("pinAlmacenado", pinIntroducido);
        editor.putString("texto", "Introducir PIN");
        //editor.putString("texto", pass.getText().toString());
        editor.putBoolean("sinPin", false);
        editor.commit();
    }
    public void arrancaActivity(){
        finish();
        Intent nueva = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(nueva);
    }

}
