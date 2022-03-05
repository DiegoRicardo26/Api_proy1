package com.example.proy1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class BuscarAmigosActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView BuscaramigosReciclerView;
    private DatabaseReference UserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_amigos);

        BuscaramigosReciclerView = (RecyclerView)findViewById(R.id.busca_amigos_recicleview);
        BuscaramigosReciclerView.setLayoutManager(new LinearLayoutManager(this));
        UserRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        toolbar=(Toolbar)findViewById(R.id.buscar_amigos_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Buscar Amigos");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contactos>options =
                new FirebaseRecyclerOptions.Builder<Contactos>().setQuery(UserRef, Contactos.class).build();

       FirebaseRecyclerAdapter<Contactos, BuscarAmigosViewHolder> adapter =
               new FirebaseRecyclerAdapter<Contactos, BuscarAmigosViewHolder>(options) {
                   @Override
                   protected void onBindViewHolder(@NonNull BuscarAmigosViewHolder holder, int position, @NonNull Contactos model) {

                       holder.nombreu.setText(model.getNombre());
                       holder.ciudadu.setText(model.getCiudad());
                       holder.estadou.setText(model.getEstado());
                       Picasso.get().load(model.getImagen()).placeholder(R.drawable.error).into(holder.imagenu);

                   }

                   @NonNull
                   @Override
                   public BuscarAmigosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

                       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_display_layout,viewGroup,false);
                       BuscarAmigosViewHolder viewHolder = new BuscarAmigosViewHolder(view);
                       return viewHolder;



                   }
               };

       BuscaramigosReciclerView.setAdapter(adapter);
       adapter.startListening();


    }


    public static class BuscarAmigosViewHolder extends RecyclerView.ViewHolder{

        TextView nombreu, ciudadu, estadou;
        CircleImageView imagenu;
        public BuscarAmigosViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreu=itemView.findViewById(R.id.user_nombre);
            ciudadu=itemView.findViewById(R.id.user_ciudad);
            estadou=itemView.findViewById(R.id.user_estado);
            imagenu=itemView.findViewById(R.id.user_imagen_perfil);


        }
    }

}