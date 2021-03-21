package com.example.proyectodam.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarActivity extends AppCompatActivity {


    Button btnRecuperar;
    EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordar_password);


        btnRecuperar = findViewById(R.id.btnedit);
        editTextEmail=findViewById(R.id.editartexto);

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });


    }


    public void validate(){
        String email = editTextEmail.getText().toString().trim();

        if(email.isEmpty()|| !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Correo inválido");
            return;
        }
        sendEmail(email);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        Intent intent = new Intent(RecuperarActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendEmail(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAdress=email;

        auth.sendPasswordResetEmail(emailAdress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RecuperarActivity.this, "Correo enviado!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RecuperarActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    
                }else{
                    Toast.makeText(RecuperarActivity.this, "Correo inválido", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
