package com.borjalapa.sqlite.sqlite;

import android.provider.BaseColumns;

/*Clase que indicamos el nombre de la tabla así como sus campos*/
public class PersonaContract {
    //Para evitar que nadie la pueda instanciar hacemos el constructor privado
    private  PersonaContract(){}

    //Creamos una clase interna estática con la información
    public static class PersonaInfo implements BaseColumns {
        public static final String TABLE_NAME = "persona";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
        public static final String COLUMN_NAME_APELLIDOS = "apellidos";
        public static final String COLUMN_NAME_EDAD = "edad";
    }

    /*creacion de entradas*/
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PersonaInfo.TABLE_NAME + " (" +
                    PersonaInfo._ID + " INTEGER PRIMARY KEY," +
                    PersonaInfo.COLUMN_NAME_NOMBRE + " TEXT," +
                    PersonaInfo.COLUMN_NAME_APELLIDOS + " TEXT," +
                    PersonaInfo.COLUMN_NAME_EDAD + " NUMBER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PersonaInfo.TABLE_NAME;
}