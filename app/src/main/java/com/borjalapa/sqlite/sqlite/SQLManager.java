package com.borjalapa.sqlite.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.borjalapa.sqlite.model.Persona;

import java.util.ArrayList;
import java.util.List;

public class SQLManager {
    public static PersonaDBHelper personaDBHelper;

    Context c;

    public SQLManager(Context c) {
        this.c = c;
    }

    public PersonaDBHelper getInstance(){
        if(personaDBHelper == null){
            personaDBHelper=  new PersonaDBHelper(c);
        }
        return personaDBHelper;
    }
    public long insert(Persona p){
        PersonaDBHelper dbHelper = getInstance();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PersonaContract.PersonaInfo.COLUMN_NAME_NOMBRE, p.getNombre());
        values.put(PersonaContract.PersonaInfo.COLUMN_NAME_APELLIDOS, p.getApellidos());
        values.put(PersonaContract.PersonaInfo.COLUMN_NAME_EDAD, p.getEdad());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(PersonaContract.PersonaInfo.TABLE_NAME, null, values);

        return newRowId;


    }

    public List<Persona> selectAll(){
        PersonaDBHelper dbHelper = getInstance();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Seleccionamos los campos que queremos recupearr
        String[] projection = {
                BaseColumns._ID,
                PersonaContract.PersonaInfo.COLUMN_NAME_NOMBRE,
                PersonaContract.PersonaInfo.COLUMN_NAME_APELLIDOS,
                PersonaContract.PersonaInfo.COLUMN_NAME_EDAD
        };

        // Elegimos el orden, en este caso por apellidos ascendente
        String sortOrder =
                PersonaContract.PersonaInfo.COLUMN_NAME_APELLIDOS + " ASC";

        //Lanzamos la consulta
        Cursor cursor = db.query(
                PersonaContract.PersonaInfo.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        //Leemos el cursor
        List<Persona> personas = new ArrayList<Persona>();
        while(cursor.moveToNext()) {
            //Buscamos todas por el _ID
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(PersonaContract.PersonaInfo._ID));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(PersonaContract.PersonaInfo.COLUMN_NAME_NOMBRE));
            String apellidos = cursor.getString(cursor.getColumnIndexOrThrow(PersonaContract.PersonaInfo.COLUMN_NAME_APELLIDOS));
            int edad = cursor.getInt(cursor.getColumnIndexOrThrow(PersonaContract.PersonaInfo.COLUMN_NAME_EDAD));
            personas.add(new Persona(itemId,nombre,apellidos,edad));
        }
        cursor.close();

        return personas;
    }

    public List<Persona> selectByName(String name){
        PersonaDBHelper dbHelper = getInstance();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Seleccionamos los campos que queremos recupearr
        String[] projection = {
                BaseColumns._ID,
                PersonaContract.PersonaInfo.COLUMN_NAME_NOMBRE,
                PersonaContract.PersonaInfo.COLUMN_NAME_APELLIDOS,
                PersonaContract.PersonaInfo.COLUMN_NAME_EDAD
        };

        // Filtramos los resultados donde el nombre = name (parámetro)
        String selection = PersonaContract.PersonaInfo.COLUMN_NAME_NOMBRE + " = ?";
        String[] selectionArgs = { name };

        // Elegimos el orden, en este caso por apellidos ascendente
        String sortOrder =
                PersonaContract.PersonaInfo.COLUMN_NAME_APELLIDOS + " ASC";

        //Lanzamos la consulta
        Cursor cursor = db.query(
                PersonaContract.PersonaInfo.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        //Leemos el cursor
        List<Persona> personas = new ArrayList<Persona>();
        while(cursor.moveToNext()) {
            //Buscamos todas por el _ID
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(PersonaContract.PersonaInfo._ID));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(PersonaContract.PersonaInfo._ID));
            String apellidos = cursor.getString(cursor.getColumnIndexOrThrow(PersonaContract.PersonaInfo._ID));
            int edad = cursor.getInt(cursor.getColumnIndexOrThrow(PersonaContract.PersonaInfo._ID));
            personas.add(new Persona(itemId,nombre,apellidos,edad));
        }
        cursor.close();

        return personas;
    }

    public int deleteById(long id){
        PersonaDBHelper dbHelper = getInstance();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Definimos el Where de la consulta
        String selection = PersonaContract.PersonaInfo._ID + " = ?";
        // Vinculamos el valor al que corresponda, como la consulta solo tiene un ?, solo habrá un argunmento.
        String[] selectionArgs = { String.valueOf(id) };
        // ejecutamos la orden que nos devuelve las filas eliminadas
        return db.delete(PersonaContract.PersonaInfo.TABLE_NAME, selection, selectionArgs);
    }

    public int updateNameById(long id, String newName){
        PersonaDBHelper dbHelper = getInstance();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Elegimos el nuevo valor para la columna
        ContentValues values = new ContentValues();
        values.put(PersonaContract.PersonaInfo.COLUMN_NAME_NOMBRE, newName);

        // Qué fila actualizamos. La que coincida con el ID
        String selection = PersonaContract.PersonaInfo._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        return db.update(
                PersonaContract.PersonaInfo.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        //Devuelve la cantidad de filas afectadas, debería ser 0 o 1 si existe ya que filtramos por el ID.
    }

    public void cerrar(){
        PersonaDBHelper dbHelper = getInstance();
        dbHelper.close();
    }
}