package icesi.dmi.com.repasoparcial2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Chat extends AppCompatActivity {

    EditText chatear;
    Button enviar;
    ListView lista;

    FirebaseAuth auth;
    FirebaseListAdapter listAdapter;
    FirebaseDatabase database;

    String nameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        auth = FirebaseAuth.getInstance();

        chatear = findViewById(R.id.mensajeDeChat);
        enviar = findViewById(R.id.enviar);
        lista = findViewById(R.id.lista);




        database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference();


        FirebaseUser user = auth.getCurrentUser();

        DatabaseReference ref = database.getReference()
                .child("Usuarios").child(user.getUid());


        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    Usuario user = dataSnapshot.getValue(Usuario.class);
                    nameUser = user.getName();

                   // Log.e("Secion de usuario", getValue().toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        final Query chat = reference.child("Chat");

        FirebaseListOptions options = new FirebaseListOptions.Builder<Mensaje>()
                .setLayout(R.layout.reglon)
                .setQuery(chat, Mensaje.class)
                .build();


        listAdapter = new FirebaseListAdapter<Mensaje>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Mensaje model, final int position) {

                TextView nombre = v.findViewById(R.id.userioChatReglon);
                TextView mensaje = v.findViewById(R.id.menssage);

                nombre.setText(model.getName());
                mensaje.setText(model.getMensaje());

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listAdapter.getRef(position).removeValue();
                    }
                });

            }
        };

        lista.setAdapter(listAdapter);



        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String me = chatear.getText().toString();

                Mensaje m = new Mensaje();
                m.setName(nameUser);
                m.setMensaje(me);

                DatabaseReference publicar = database.getReference();

                        publicar.child("Chat").push().setValue(m);

            }
        });

    }

    @Override
    protected void onStart() {
        listAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        listAdapter.stopListening();
        super.onStop();
    }
}
