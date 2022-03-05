package com.example.proy1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.usage.StorageStatsManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText numero, codigo;
    private Button enviar_numero, enviar_codigo;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerification;
    private PhoneAuthProvider.ForceResendingToken mResendingToken;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        numero = (EditText) findViewById(R.id.numero);
        codigo = (EditText) findViewById(R.id.codigo);

        enviar_numero = (Button) findViewById(R.id.enviar_numero);
        enviar_codigo = (Button) findViewById(R.id.enviar_codigo);


        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        enviar_numero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = numero.getText().toString();
            if (TextUtils.isEmpty(phoneNumber)){
                Toast.makeText(LoginActivity.this, "Ingrese su numero", Toast.LENGTH_SHORT).show();

            }else{
                loadingBar.setTitle("Enviando el codigo");
                loadingBar.setMessage("por favor espere...");
                loadingBar.show();
                loadingBar.setCancelable(true);
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(LoginActivity.this)
                        .setCallbacks(callbacks)
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
            }
        });

        enviar_codigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numero.setVisibility(View.GONE);
                enviar_numero.setVisibility(View.GONE);
                String verificacionCode = codigo.getText().toString();
                if(TextUtils.isEmpty(verificacionCode)){
                    Toast.makeText( LoginActivity.this, "Ingresar el codigo recibido", Toast.LENGTH_SHORT).show();
                }else{
                    loadingBar.setTitle("Ingresando");
                    loadingBar.setMessage("tranqui...");
                    loadingBar.show();
                    loadingBar.setCancelable(true);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerification, verificacionCode);
                    signInPhoneAuthCredential(credential);


                }

            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                loadingBar.dismiss();
                Toast.makeText( LoginActivity.this, "Numero Invalido, intentar de nuevo", Toast.LENGTH_SHORT).show();
                numero.setVisibility(View.VISIBLE);
                enviar_numero.setVisibility(View.VISIBLE);
                codigo.setVisibility(View.GONE);
                enviar_codigo.setVisibility(View.GONE);


            }
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token){

                mVerification = verificationId;
                mResendingToken = token;
                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this, "EL codigo se ha enviado, revise su mensagería", Toast.LENGTH_SHORT).show();
                numero.setVisibility(View.GONE);
                enviar_numero.setVisibility(View.GONE);
                codigo.setVisibility(View.VISIBLE);
                enviar_codigo.setVisibility(View.VISIBLE);
            }
        };


    }//********************ONCREATE*****

    private void signInPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if  (task.isSuccessful()){
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Has ingresado uwu", Toast.LENGTH_SHORT).show();
                    EnviarAlInicio();
                }else{
                    String mensaje = task.getException().toString();
                    Toast.makeText(LoginActivity.this, "ERROR...:("+mensaje, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser !=null){
            EnviarAlInicio();

        }
    }

    private void EnviarAlInicio() {
        Intent intent = new Intent(LoginActivity.this, InicioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );

        startActivity(intent);
        finish();
    }
}