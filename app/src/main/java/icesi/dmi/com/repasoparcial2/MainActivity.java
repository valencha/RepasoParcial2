package icesi.dmi.com.repasoparcial2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //Autenticacion
    FirebaseAuth auth;

    //bASE DE DATOS
    FirebaseDatabase database;

    EditText et_nombre;
    EditText et_email;
    EditText et_password;
    Button btn_registrar;
    Button btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();

        final DatabaseReference reference = database.getReference();

         et_nombre = findViewById(R.id.et_nombre);
         et_email = findViewById(R.id.et_email);
         et_password = findViewById(R.id.et_contrasena);
         btn_registrar = findViewById(R.id.btn_registrar);
         btn_login = findViewById(R.id.btn_login);



         btn_registrar.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View view) {

                 final String name = et_nombre.getText().toString();
                 final String email = et_email.getText().toString();
                 final String password = et_password.getText().toString();


                 auth.createUserWithEmailAndPassword(email, password)
                 .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){

                             String uid =auth.getCurrentUser().getUid();
                             Usuario usuario = new Usuario();
                             usuario.setName(name);
                             usuario.setEmail(email);
                             usuario.setUid(uid);

                             DatabaseReference reference = database.getReference();

                             reference.child("Usuarios").child(uid).setValue(usuario);
                         }else{
                             Toast.makeText(MainActivity.this,
                                     task.getException().toString(),
                                     Toast.LENGTH_SHORT).show();
                         }
                     }
                 });




             }
         });

         btn_login.setOnClickListener(new View.OnClickListener() {



             @Override
             public void onClick(View view) {

                 String name = et_nombre.getText().toString();
                 String email = et_email.getText().toString();
                 String password = et_password.getText().toString();

                 auth.signInWithEmailAndPassword(email, password)
                 .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()) {
                             Intent irChat = new Intent(MainActivity.this, Chat.class);
                             startActivity(irChat);
                         }
                     }
                 });
             }
         });










    }






}
