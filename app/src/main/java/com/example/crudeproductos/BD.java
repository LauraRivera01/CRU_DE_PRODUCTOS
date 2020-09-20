package com.example.crudeproductos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BD extends SQLiteOpenHelper {
    static String nameDB = "bd_Productos";
    static String tblProductos = "CREATE TABLE Productos(idProducto integer primary key autoincrement, nombre_producto text, descripcion_producto text, precio integer)";
    private SQLiteDatabase sqLiteDatabaseReadable;
    private Cursor cursor;

    public BD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nameDB, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblProductos);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void manttoProductos(String accion, String[] data) {
        SQLiteDatabase sqLiteDatabaseReadable = getReadableDatabase();
        SQLiteDatabase sqLiteDatabaseWritable = getWritableDatabase();
        Cursor cursor = null;

        switch (accion) {
            case "consultar":
                cursor = sqLiteDatabaseReadable.rawQuery("SELECT * FROM productos ORDER BY nombre ASC", null);

                break;

            case "nuevo":

                sqLiteDatabaseWritable.execSQL("INSERT INTO productos (nombre,descripcion,precio) VALUES('" + data[1] + "','" + data[2] + "','" + data[3] + "','" + data[4] + "')");
                break;

            case "modificar":
                break;

            case "eliminar":

                break;

            default:
                break;

        }
    }
}

