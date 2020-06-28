package com.example.agendacliente;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendacliente.fragment.FragmentCalendario;
import com.example.agendacliente.fragment.FragmentServicos;
import com.example.agendacliente.util.Permissao;
import com.example.agendacliente.util.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;

    private FragmentCalendario fragmentCalendario;
    private FragmentServicos fragmentServicos;

    private Fragment fragment;
    private FragmentManager fragmentManager;

    private String latitude_Empresa = "-21.655028";
    private String longitude_Empresa = "-42.344162";

    private String numero = "032999173670";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        navigationBottom();

        fragmentCalendario = new FragmentCalendario();
        fragmentServicos = new FragmentServicos();

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.frameLayout_Fragment,fragmentCalendario).commit();

        permissao();
    }

    @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        finishAffinity(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.button_wpp,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemWpp:
                click_ContatoTelefone();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


 */
    private void navigationBottom(){

        onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            private MenuItem item;

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {

                switch (menuitem.getItemId()){
                    case R.id.item_navegacao_servicos:
                        fragment  = fragmentServicos;
                        break;

                    case R.id.item_navegacao_calendario:
                        fragment = fragmentCalendario;
                        break;

                    case R.id.item_localizacao:
                        click_AbrirLocalizacao();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_Fragment,fragment).commit();

                return true;
            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }


    private void permissao(){
        String permissoes[] = new String[]{
                Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_CONTACTS
        };

        Permissao.validate(this, 333, permissoes);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }




    public void dialogoContato(String numero){

        String contato = numero.replace(" ","").replace("-","")
                .replace("(","").replace(")","");

        final StringBuffer numeroContato = new StringBuffer(contato);

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext())
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
            Toast.makeText(getApplicationContext(), "Numero de contato indisponivel", Toast.LENGTH_SHORT).show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void click_AbrirLocalizacao() {
        if (Util.statusInternet_MoWi(getApplicationContext())) {
            abrirLocalizacaoEmpresa();
        } else {
            Toast.makeText(getApplicationContext(), "Erro de conexão com a internet", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "Erro - Verifique se o whatsapp esta insatalado no dispositivo", Toast.LENGTH_SHORT).show();
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



}
