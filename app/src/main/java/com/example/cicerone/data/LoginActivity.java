package com.example.cicerone.data;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cicerone.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private Utente u;
    private String email;
    private String password;
    private TextView reset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        u = new Utente(LoginActivity.this,"","","","","", 0);

        ImageView img = findViewById(R.id.imageView2);

        emailText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        TextView signUpLink = findViewById(R.id.button_registrazione2);
        reset = findViewById(R.id.reset);

        reset.setClickable(true);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailText.getText().toString();
                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() )
                    Toast.makeText(LoginActivity.this, "Inserisci un indirizzo e-mail valido", Toast.LENGTH_LONG).show();
                else {
                    Utente u = new Utente(LoginActivity.this,"","","",email,"", 0);
                    u = DBhelper.getInfoUtente(u);
                    String subject = "Problemi all'accesso";
                    String corpo = "Ciao "+u.getNome()+"!\n\nEcco la password per effettuare l'accesso: " + u.getPassword() +
                            "\nNon dimenticarla e fai attenzione a non rivelarla a nessuno!\n\nIl team Step di Cicerone.";
                    SendIt sendIt = new SendIt(u.getEmail(), subject, corpo, LoginActivity.this);
                    sendIt.execute();
                    Toast.makeText(LoginActivity.this, "Controlla la tua casella di posta elettronica", Toast.LENGTH_LONG).show();
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityMain();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Autenticazione...");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP && resultCode == RESULT_OK)
            this.finish();
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        u.setEmail(email);
        u.setPassword(password);
        u=DBhelper.getInfoUtente(u);
        loginButton.setEnabled(true);
        Intent homeIntent = new Intent(this,HomeActivity.class);
        homeIntent.putExtra("nome",u.getNome());
        homeIntent.putExtra("cognome",u.getCognome());
        homeIntent.putExtra("datanascita",u.getDatanascita());
        homeIntent.putExtra("email",u.getEmail());
        homeIntent.putExtra("id",u.getId());
        homeIntent.putExtra("CF",u.getCF());
        startActivity(homeIntent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        Utente validateUser = new Utente (LoginActivity.this,password,"","",email,"", 0);

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ) {
            emailText.setError("Inserisci un indirizzo email valido");
            valid = false;
        } else {
            emailText.setError(null);
        }

        /*Verifico che l'email inserita esista per un utente registrato*/
        if (!DBhelper.isSignedUp(validateUser))
        {
            emailText.setError("Nessun utente registrato con questo indirizzo mail!");
            valid = false;
        }
        else
        {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 5 || password.length() > 15) {
            passwordText.setError("La password è di almeno 5 caratteri e massimo 15");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        /* Verifico che la password corrisponda a quella dell'utente*/
        if (!DBhelper.searchPassword(validateUser).equals(password))
        {
            passwordText.setError("La password inserita non coincide con quella dell'utente!");
            valid = false;
        }
        else {
            passwordText.setError(null);
        }

        return valid;
    }

    public void openActivityMain()
    {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class );
        startActivity(mainIntent);
    }
}