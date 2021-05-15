package com.example.final_proyect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.final_proyect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Reset_passwd_Activity extends AppCompatActivity {

    Button btnRecuperar;
    EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_passwd);


        btnRecuperar = findViewById(R.id.btn_reset_passswd);
        editTextEmail=findViewById(R.id.email_reset_passwd);

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

        Intent intent = new Intent(Reset_passwd_Activity.this, Login_Activity.class);
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
                    Toast.makeText(Reset_passwd_Activity.this, "Correo enviado!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Reset_passwd_Activity.this, Login_Activity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(Reset_passwd_Activity.this, "Correo inválido", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}