package com.example.agendacliente.fragment;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.example.agendacliente.R;
import com.example.agendacliente.ServicosActivity;
import com.example.agendacliente.activity.HorariosActivity;
import com.example.agendacliente.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment implements View.OnClickListener {

    private CardView cardView_ligacao;
    private CardView cardView_localizacao;
    private CardView cardView_Servicos;

    private String latitude_Empresa = "-21.655028";
    private String longitude_Empresa = "-42.344162";

    private String numero = "032999173670";


    public FragmentHome() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cardView_ligacao = view.findViewById(R.id.cardView_Contato);
        cardView_localizacao = view.findViewById(R.id.cardView_Localizacao);
        cardView_Servicos = view.findViewById(R.id.cardView_Servicos);

        cardView_ligacao.setOnClickListener(this);
        cardView_localizacao.setOnClickListener(this);
        cardView_Servicos.setOnClickListener(this);

        return view;
    }



    // --------------------- AÇÃO DE CLICK DENTRO DO FRAGMENT -------------------------


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.cardView_Contato:
              click_ContatoTelefone();
            break;

            case R.id.cardView_Localizacao:
               click_AbrirLocalizacao();
                break;

            case R.id.cardView_Servicos:
                click_AbrirServicos();
                break;
        }
    }


    public void dialogoContato(String numero){

        String contato = numero.replace(" ","").replace("-","")
                .replace("(","").replace(")","");

        final StringBuffer numeroContato = new StringBuffer(contato);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Entrar em contato")
                .setMessage("O que você gostaria de fazer?")
                .setPositiveButton("WhatsApp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        entrarEmContatoWhatsApp(numeroContato);
                    }
                })
                .setNegativeButton("Ligar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        entrarEmContatoLigacao(numeroContato);
                    }
                });

            builder.show();


    }


    public void click_ContatoTelefone(){

        if (!numero.isEmpty()){
            dialogoContato(numero);
        }else{
            Toast.makeText(getContext(), "Numero de contato indisponivel", Toast.LENGTH_SHORT).show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void click_AbrirLocalizacao(){
        if (!latitude_Empresa.isEmpty() && !longitude_Empresa.isEmpty()){
            if (Util.statusInternet_MoWi(getContext())){
                abrirLocalizacaoEmpresa();
            }else{
                Toast.makeText(getContext(), "Erro de conexão com a internet", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(getContext(), "Localização indisponivel", Toast.LENGTH_SHORT).show();
        }
    }


    public void entrarEmContatoWhatsApp(StringBuffer numeroContato){

        try {
            numeroContato.deleteCharAt(0);
            numeroContato.deleteCharAt(2);

            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            intent.putExtra("jid",
                    PhoneNumberUtils.stripSeparators("55"+numeroContato) + "@s.whatsapp.net");

            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(getContext(), "Erro - Verifique se o whatsapp esta insatalado no dispositivo", Toast.LENGTH_SHORT).show();
        }


    };


    public void entrarEmContatoLigacao(StringBuffer numeroContato){

        Uri uri = Uri.parse("tel:"+numeroContato);
        Intent intent = new Intent(Intent.ACTION_DIAL,uri);
        startActivity(intent);
    };


    private void abrirLocalizacaoEmpresa(){
        String url = "https://www.google.com/maps/search/?api=1&query="+latitude_Empresa+longitude_Empresa;
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
        startActivity(intent);
    }


    private void click_AbrirServicos(){
        Intent intent = new Intent(getContext(), ServicosActivity.class);
        startActivity(intent);
    }

}
