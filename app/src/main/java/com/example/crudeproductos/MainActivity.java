package com.example.crudeproductos;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends Activity {
    BD miBD;
    Cursor misProductos;
    productos productos;
    ArrayList<productos > stringArrayList = new ArrayList<productos>();
    ArrayList<productos  > copyStringArrayList = new ArrayList<productos>();
    ListView ltsProducto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btnAgregarProducto = (FloatingActionButton)findViewById(R.id.ButtonFloatNuevoProducto);
        btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarProducto("nuevo", new String[]{});
            }
        });
        obtenerDatosProductos();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_productos, menu);

        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        misProductos.moveToPosition(adapterContextMenuInfo.position);
        menu.setHeaderTitle(misProductos.getString(1));
    }

    void buscarProductos(){
        final TextView tempVal = (TextView)findViewById(R.id.txtBuscarProductos);
        tempVal.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    stringArrayList.clear();
                    if (tempVal.getText().toString().trim().length() < 1) {//no hay texto para buscar
                        stringArrayList.addAll(copyStringArrayList);
                    } else {//hacemos la busqueda
                        for (productos am : copyStringArrayList) {
                            String producto = am.getProducto();
                            if (producto.toLowerCase().contains(tempVal.getText().toString().trim().toLowerCase())) {
                                stringArrayList.add(am);
                            }
                        }
                    }
                    adaptadorImagenes adaptadorImg = new adaptadorImagenes(getApplicationContext(), stringArrayList);
                    ltsProducto.setAdapter(adaptadorImg);
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), "Error: "+ ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }



    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnxAgregar:
                agregarProducto("nuevo", new String[]{});
                return true;

            case R.id.mnxModificar:
                String[] dataProducto = {
                        misProductos.getString(0),//idProducto
                        misProductos.getString(1),//nombre
                        misProductos.getString(2),//descripcion
                        misProductos.getString(3),//precio
                        misProductos.getString(4) //
                };
                agregarProducto("modificar", dataProducto);
                return true;

            case R.id.mnxEliminar:
                AlertDialog eliminarProducto =  eliminarProducto();
                eliminarProducto.show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
    AlertDialog eliminarProducto(){
        AlertDialog.Builder confirmacion = new AlertDialog.Builder(MainActivity.this);
        confirmacion.setTitle(misProductos.getString(1));
        confirmacion.setMessage("Esta seguro de eliminar el registro?");
        confirmacion.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                miBD.manttoProductos("eliminar",new String[]{misProductos.getString(0)});
                obtenerDatosProductos();
                Toast.makeText(getApplicationContext(), "Producto eliminado con exito.",Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });
        confirmacion.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Eliminacion cancelada por el usuario.",Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });
        return confirmacion.create();
    }

    void obtenerDatosProductos() {
        miBD = new BD (getApplicationContext(), "", null, 1);
        miBD.manttoProductos("consultar", null);
        if (misProductos.moveToFirst()) { //hay registro en la BD que mostrar
            mostrarDatosProductos();
        } else { //No tengo registro que mostrar.
            Toast.makeText(getApplicationContext(), "No hay registros de amigos que mostrar", Toast.LENGTH_LONG).show();
            agregarProducto("nuevo", new String[]{});
        }
    }

    void agregarProducto(String accion, String[] dataProducto) {
        Bundle enviarParametros = new Bundle();
        enviarParametros.putString("accion", accion);
        enviarParametros.putStringArray("dataProducto", dataProducto);
        Intent agregarProducto = new Intent(MainActivity.this, agregarProducto.class);
        agregarProducto.putExtras(enviarParametros);
        startActivity(agregarProducto);
    }

    void mostrarDatosProductos() {
        ListView ltsProducto = (ListView) findViewById(R.id.ltsProductos);
        ArrayList<String> stringArrayList = new ArrayList<String>();
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, stringArrayList);
        ltsProducto.setAdapter(stringArrayAdapter);
        do {
            stringArrayList.add(misProductos.getString(1));
        } while (misProductos.moveToNext());
        stringArrayAdapter.notifyDataSetChanged();
        registerForContextMenu(ltsProducto);
    }
}

class productos{
    String id;
    String producto;
    String descripcion;
    String precio;
    String urlImg;

    public productos(String id, String producto, String descripcion, String precio, String urlImg) {
        this.id = id;
        this.producto = producto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.urlImg = urlImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio        () {
        return precio        ;
    }

    public void setPrecio        (String direccion) {
        this.precio         = precio      ;
    }



    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }
}
