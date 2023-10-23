package br.unigran.trabalho.telas.Cadastro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.unigran.trabalho.R;
import br.unigran.trabalho.model.Usuario;
import br.unigran.trabalho.telas.Login.LoginActivity;

public class CadastroActivity extends AppCompatActivity {


    private EditText email;
    private EditText senha;
    private Button cadastrar;
    private FirebaseAuth mAuth;
    private Usuario u;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.idEmail);
        senha = findViewById(R.id.idSenha);
        cadastrar = findViewById(R.id.idCadastrar);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty() || senha.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Obrigatório preencher todos os campos!", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), senha.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(getApplicationContext(), "Cadastro efetuado com Sucesso!", Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        limpaCampos();
                                        mudarTela();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "ERRO ao efetuar o Cadastro!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });
    }

    private void mudarTela() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void limpaCampos() {
        email.setText("");
        senha.setText("");
    }
}