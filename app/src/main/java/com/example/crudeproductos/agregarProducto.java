package com.example.crudeproductos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

  public class agregarProducto extends AppCompatActivity{

        BD miDB;
        String accion = "nuevo";
        String idProducto = "0";
        ImageView imgFotoProducto;
        String urlCompletaImg;
        Button btnProductos;
        Intent takePictureIntent;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_agregarproducto);

            imgFotoProducto = findViewById(R.id.imgFotoProducto);

            btnProductos = findViewById(R.id.btnMostrarProductos);
            btnProductos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
            guardarDatosProducto();
            tomarFotoProducto();
        }

    void tomarFotoProducto(){
        imgFotoProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    //guardando la imagen
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    }catch (Exception ex){}
                    if (photoFile != null) {
                        try {
                            Uri photoURI = FileProvider.getUriForFile(agregarProducto.this, "com.example.prueba.fileprovider", photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, 1);
                        }catch (Exception ex){
                            Toast.makeText(getApplicationContext(), "Error Toma Foto: "+ ex.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                Bitmap imageBitmap = BitmapFactory.decodeFile(urlCompletaImg);
                imgFotoProducto.setImageBitmap(imageBitmap);
            }
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Error: "+ ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "imagen_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir.exists() == false) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        urlCompletaImg = image.getAbsolutePath();
        return image;
    }
    void guardarDatosProducto(){
        btnProductos = findViewById(R.id.btnGuardarProductos);
        btnProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Button btnGuardarProductos = (Button) findViewById(R.id.btnGuardarProductos);
                btnGuardarProductos.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        TextView tempVal = (TextView) findViewById(R.id.txtNombreProducto);
                        String producto = tempVal.getText().toString();

                        tempVal = (TextView) findViewById(R.id.txtDescripcion);
                        String descripcion = tempVal.getText().toString();

                        tempVal = (TextView) findViewById(R.id.txtPrecio);
                        String precio = tempVal.getText().toString();

                        tempVal = (TextView) findViewById(R.id.txtBuscarProductos);
                        String BuscarProd = tempVal.getText().toString();

                        String[] data = {idProducto, producto, descripcion, precio, BuscarProd};

                        miDB = new BD(getApplicationContext(), "", null, 1);
                        miDB.manttoProductos(accion, data);

                        Toast.makeText(getApplicationContext(), "Registro de producto insertado con exito", Toast.LENGTH_LONG).show();
                        Intent mostrarListaProducto = new Intent(agregarProducto.this, MainActivity.class);
                        startActivity(mostrarListaProducto);
                    }
                });
            }

                void mostrarListaProducto(){
                    Intent mostrarProducto = new Intent(agregarProducto.this, MainActivity.class);
                    startActivity(mostrarProducto);



            }

            void mostrarDatosProducto() {
                try {
                    Bundle recibirParametros = getIntent().getExtras();
                    accion = recibirParametros.getString("accion");
                    if (accion.equals("modificar")) {
                        String[] dataProductos = recibirParametros.getStringArray("dataProducto");

                        idProducto = dataProductos[0];

                        TextView tempVal = (TextView) findViewById(R.id.txtNombreProducto);
                        tempVal.setText(dataProductos[1]);

                        tempVal = (TextView) findViewById(R.id.txtDescripcion);
                        tempVal.setText(dataProductos[2]);

                        tempVal = (TextView) findViewById(R.id.txtPrecio);
                        tempVal.setText(dataProductos[3]);

                        tempVal = (TextView) findViewById(R.id.txtBuscarProductos);
                        tempVal.setText(dataProductos[4]);

                        urlCompletaImg = dataProductos[5];
                        Bitmap imageBitmap = BitmapFactory.decodeFile(urlCompletaImg);
                        imgFotoProducto.setImageBitmap(imageBitmap);
                    }
                } catch (Exception ex) {
                    ///
                }
            }
        });
    }
}

