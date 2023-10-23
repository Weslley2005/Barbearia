package br.unigran.trabalho;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import br.unigran.trabalho.model.Barbearia;


public class MainActivity extends AppCompatActivity {

    EditText nome;
    EditText telefone;
    RatingBar avaliacao;
    Button salvar;
    List dados;
    ListView listagem;
    ArrayAdapter listaAdapter;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        nome = findViewById(R.id.idNome);
        telefone = findViewById(R.id.idTelefone);
        avaliacao = findViewById(R.id.idAvaliacao);
        dados = new LinkedList();
        listagem = findViewById(R.id.idListagem);
        listaAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dados);
        listagem.setAdapter(listaAdapter);
        ler();

    }

    public void salvar(View view) {
        Barbearia b = new Barbearia();
        b.setNome(nome.getText().toString());
        b.setTelefone(telefone.getText().toString());
        b.setAvaliacao(avaliacao.getRating());
        DatabaseReference barbearia = databaseReference.child("barbearia");
        barbearia.push().setValue(b);
        Toast.makeText(getApplicationContext(), "Salvo", Toast.LENGTH_SHORT).show();
        limparcampos();
    }

    public void ler() {
        DatabaseReference barbearia = databaseReference.child("barbearia");
        barbearia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dados.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Barbearia barbearia = item.getValue(Barbearia.class);
                    dados.add(barbearia);
                    listaAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), "erro de conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void limparcampos() {
        nome.setText("");
        telefone.setText("");
        avaliacao.setNumStars(0);
    }

}