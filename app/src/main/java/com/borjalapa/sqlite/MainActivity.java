package com.borjalapa.sqlite;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.borjalapa.sqlite.model.Persona;
import com.borjalapa.sqlite.sqlite.SQLManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    /*añadir esto en el manifest.xml para que no se muevan los campos cuando se abre el teclado*/
    //android:windowSoftInputMode="stateHidden|adjustNothing">

    EditText etNombre, etApellidos, etEdad;
    Button btnAdd;
    RecyclerView lista;
    MyAdapter myAdapter;

    List<Persona> listaPersonas;

    SQLManager SQLManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLManager = new SQLManager(this);

        etApellidos = findViewById(R.id.etApellido);
        etNombre = findViewById(R.id.etNombre);
        etEdad = findViewById(R.id.etEdad);
        btnAdd = findViewById(R.id.btnAdd);

        lista = (RecyclerView)findViewById(R.id.lista);

        lista.setLayoutManager(new LinearLayoutManager(this));
        listaPersonas = SQLManager.selectAll();

        myAdapter = new MyAdapter(listaPersonas,this);

        lista.setAdapter(myAdapter);
    }

    //cuando se cierre la actividad que se cierra la bd para ahorrar recursos
    @Override
    protected void onDestroy() {
        SQLManager.cerrar();
        super.onDestroy();
    }

    public void addPersona(View view) {
        if (TextUtils.isEmpty(etNombre.getText().toString()) || TextUtils.isEmpty(etApellidos.getText().toString()) || TextUtils.isEmpty(etEdad.getText().toString())){
            Toast.makeText(MainActivity.this, getString(R.string.empty_fields),Toast.LENGTH_SHORT).show();
        }else{
            String nombre = etNombre.getText().toString();
            String apellidos = etApellidos.getText().toString();
            int edad = Integer.parseInt(etEdad.getText().toString());

            Persona p = new Persona(-1,nombre,apellidos,edad);

            //se añade la persona y al hacer el insert se autoincremente la ID
            p.setId(SQLManager.insert(p));

            myAdapter.addPersona(p);

            limparTextos();

            //cuando le damos a añadir el editText de nombre pide el foco y no muestra el teclado
            etNombre.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etNombre.getWindowToken(), 0);
        }

    }

    private void limparTextos() {
        etNombre.setText("");
        etApellidos.setText("");
        etEdad.setText("");
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        List<Persona> listaPersonas;
        Context c;

        public MyAdapter(List<Persona> listaPersonas, Context c) {
            super();
            this.listaPersonas = listaPersonas;
            this.c = c;
        }

        public void addPersona(Persona p){
            listaPersonas.add(p);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_lista, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.tvNombre.setText(listaPersonas.get(position).getNombre());
            holder.tvApellidos.setText(listaPersonas.get(position).getApellidos());
            holder.tvEdad.setText(""+listaPersonas.get(position).getEdad());

            holder.btnBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //si no da 1 no borra nada
                    //primero lo borramos de la bd y luego dentro del if lo borramos de la lista local
                    if(SQLManager.deleteById(listaPersonas.get(position).getId())==1){
                        listaPersonas.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return listaPersonas.size();
        }

        private class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvNombre, tvApellidos, tvEdad;
            ImageButton btnBorrar;
            public MyViewHolder(View v){
                super(v);
                tvNombre = (TextView)v.findViewById(R.id.tvNombre);
                tvApellidos = (TextView)v.findViewById(R.id.tvApellidos);
                tvEdad = (TextView)v.findViewById(R.id.tvEdad);
                btnBorrar = (ImageButton)v.findViewById(R.id.btnBorrar);
            }

        }
    }
}