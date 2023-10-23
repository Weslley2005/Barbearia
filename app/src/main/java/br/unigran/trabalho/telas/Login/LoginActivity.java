package br.unigran.trabalho.telas.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.unigran.trabalho.MainActivity;
import br.unigran.trabalho.R;
import br.unigran.trabalho.telas.Cadastro.CadastroActivity;

public class LoginActivity extends AppCompatActivity {

    EditText email1;
    EditText senha1;
    Button login;
    Button tcadastro;
    SignInButton btgo;
    private FirebaseAuth mAuth;

    GoogleSignInClient googleSignInClient;
    @SuppressLint("MissingInflatedId")
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        email1 = findViewById(R.id.idEmail1);
        senha1 = findViewById(R.id.idSenha1);
        login = findViewById(R.id.idLogin);
        tcadastro = findViewById(R.id.idtelaCadastro);
        btgo = findViewById(R.id.idGoogle);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("839034501991-pkroq9v715mgu53d5ubsvc6mk4g1ufg1.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email1.getText().toString().isEmpty() || senha1.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Obrigatório preencher todos os campos!", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email1.getText().toString(), senha1.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Login efetuado com Sucesso!", Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        telaprincipal();
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "ERRO ao efetuar o Login!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


        tcadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                telacadastro();
            }
        });

        btgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singnInG();
            }
        });

    }

    private void loginGoogle(String token) {
        AuthCredential credencial = GoogleAuthProvider.getCredential(token, null);
        mAuth.signInWithCredential(credencial).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Login com o Google efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                    telaprincipal();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "ERRO ao efetuar o Login!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void singnInG() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
            try {
                GoogleSignInAccount conta = task.getResult(ApiException.class);
                loginGoogle(conta.getIdToken());
            } catch (ApiException exception) {
                Toast.makeText(getApplicationContext(), "Nenhum usuario foi encontrado logado!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void telacadastro() {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    private void telaprincipal() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
