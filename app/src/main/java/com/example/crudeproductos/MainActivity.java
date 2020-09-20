package com.example.crudeproductos;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {
    BD miBD;
    Cursor misProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                return true;

            default:
                return super.onContextItemSelected(item);
        }
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
        Intent agregarProducto = new Intent(MainActivity.this, AgregarProducto.class);
        agregarProducto.putExtras(enviarParametros);
        startActivity(agregarProducto);
    }

    void mostrarDatosProductos() {
        ListView ltsProducto = (ListView) findViewById(R.id.ListaDeProductos);
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