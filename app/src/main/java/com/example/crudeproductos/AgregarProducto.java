package com.example.crudeproductos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AgregarProducto extends AppCompatActivity{

        BD miDB;
        String accion = "nuevo";
        String idProducto = "0";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_agregarproducto);

            Button btnGuardarProductos = (Button)findViewById(R.id.btnGuardarProductos);
            btnGuardarProductos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView tempVal = (TextView)findViewById(R.id.txtNombreProducto);
                    String producto = tempVal.getText().toString();

                    tempVal = (TextView)findViewById(R.id.txtDescripcion);
                    String descripcion = tempVal.getText().toString();

                    tempVal = (TextView)findViewById(R.id.txtPrecio);
                    String precio = tempVal.getText().toString();

                    tempVal = (TextView)findViewById(R.id.txtBuscarProductos);
                    String BuscarProd = tempVal.getText().toString();

                    String[] data = {idProducto, producto, descripcion, precio, BuscarProd};

                    miDB = new BD (getApplicationContext(),"", null, 1);
                    miDB.manttoProductos(accion, data);

                    Toast.makeText(getApplicationContext(),"Registro de producto insertado con exito", Toast.LENGTH_LONG).show();
                    Intent mostrarProducto = new Intent(AgregarProducto.this, MainActivity.class);
                    startActivity(mostrarProducto);
                }
            });
            mostrarDatosProducto();
        }
        void mostrarDatosProducto(){
            try {
                Bundle recibirParametros = getIntent().getExtras();
                accion = recibirParametros.getString("accion");
                if (accion.equals("modificar")){
                    String[] dataProductos = recibirParametros.getStringArray("dataProducto");

                    idProducto = dataProductos[0];

                    TextView tempVal = (TextView)findViewById(R.id.txtNombreProducto);
                    tempVal.setText(dataProductos[1]);

                    tempVal = (TextView)findViewById(R.id.txtDescripcion);
                    tempVal.setText(dataProductos[2]);

                    tempVal = (TextView)findViewById(R.id.txtPrecio);
                    tempVal.setText(dataProductos[3]);

                    tempVal = (TextView)findViewById(R.id.txtBuscarProductos);
                    tempVal.setText(dataProductos[4]);
                }
            }catch (Exception ex){
                ///
            }
        }
    }

