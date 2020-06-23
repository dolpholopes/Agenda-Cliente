package com.example.agendacliente.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.agendacliente.R;
import com.example.agendacliente.model.Agendamento;
import com.example.agendacliente.util.DialogProgress;
import com.example.agendacliente.util.Util;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AgendamentoServicoActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> data = new ArrayList<String>();
    private EditText editNome, editTelefone;
    private CheckBox checkWpp, checkCorte, checkBarba;
    private CardView cardViewAgendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento_servico);

        data = getIntent().getStringArrayListExtra("data");

        editNome = findViewById(R.id.editAgendamentoNome);
        editTelefone = findViewById(R.id.editAgendamentoTelefone);
        checkWpp = findViewById(R.id.checkAgendamentoWpp);
        checkBarba = findViewById(R.id.checkBarba);
        checkCorte = findViewById(R.id.checkCorte);
        cardViewAgendar = findViewById(R.id.cardViewAgendar);
        cardViewAgendar.setOnClickListener(this);
    }


    //-------------------------------- AÇÃO DE CLICK DO CARD VIEW ------------------------------
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cardViewAgendar:
                agendar();
            break;
        }
    }


    //-------------------------------- AÇÃO DE AGENDAR ------------------------------

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void agendar() {
        String nome = editNome.getText().toString();
        String contato = editTelefone.getText().toString();
        boolean wpp = checkWpp.isChecked();
        boolean barba = checkBarba.isChecked();
        boolean corte = checkCorte.isChecked();

        if (!nome.isEmpty() || !contato.isEmpty()){
            //--------------------------------
            if (!corte && !barba){
                Toast.makeText(getBaseContext(), "Escolha qual o serviço desejado", Toast.LENGTH_SHORT).show();
            }else{
                //--------------------------------------------
                if (Util.statusInternet_MoWi(getBaseContext())){
                    Agendamento agendamento = new Agendamento(nome, contato,wpp,barba,corte);
                    agendarFirebase(nome,contato,wpp,barba,corte);
                    limparCampos();

                }else{
                    Toast.makeText(getBaseContext(), "Erro - Verifique a conexão com a intenert", Toast.LENGTH_SHORT).show();
                }
                //--------------------------------------------
            }
            //---------------------------------
        }else{
            Toast.makeText(getBaseContext(), "Insira todos os dados para agendar", Toast.LENGTH_SHORT).show();
        }
    }

    private void limparCampos(){
        editNome.setText("");
        editTelefone.setText("");
        checkCorte.setChecked(false);
        checkBarba.setChecked(false);
    }



    //-------------------------------- AÇÃO DE AGENDAR NO FIREBASE ------------------------------

    private void agendarFirebase(String nome, String contato, boolean wpp, boolean barba, boolean corte){
        Agendamento agendamento = new Agendamento(nome,contato,wpp,barba,corte);

        final DialogProgress dialogProgress = new DialogProgress();
        dialogProgress.show(getSupportFragmentManager(),"dialog");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference().child("BD").child("Calendario")
                .child("HorariosAgendados").child(data.get(2)).child("Mes").child(data.get(1))
                .child("dia").child(data.get(0));

        reference.child(data.get(3)).setValue(agendamento).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    dialogProgress.dismiss();
                    Toast.makeText(getBaseContext(), "Sucesso ao agendar!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    dialogProgress.dismiss();
                    Toast.makeText(getBaseContext(), "Falha ao agendar!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
