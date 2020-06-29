package com.example.agendacliente.fragment;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.agendacliente.R;
import com.example.agendacliente.activity.HorariosActivity;
import com.example.agendacliente.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCalendario extends Fragment implements CalendarView.OnDateChangeListener {

   private CalendarView calendarView;
   int dia_atual;
   int mes_atual;
   int ano_atual;




    public FragmentCalendario() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendario, container, false);
        calendarView = view.findViewById(R.id.calendarView_Calendario);

        obterDataAtual();
        configurarCalendario();

        calendarView.setOnDateChangeListener(this);

        versaoLollipop();

        return view;
    }

    //----------------------------- OBTER DATA ATUAL ---------------------------------------------

    private void obterDataAtual(){
        long dataLong = calendarView.getDate();
    Locale locale = new Locale("pt", "BR");

        SimpleDateFormat dia = new SimpleDateFormat("dd", locale);
        SimpleDateFormat mes = new SimpleDateFormat("MM", locale);
        SimpleDateFormat ano = new SimpleDateFormat("yyyy", locale);

        dia_atual = Integer.parseInt(dia.format(dataLong));
        mes_atual = Integer.parseInt(mes.format(dataLong));
        ano_atual = Integer.parseInt(ano.format(dataLong));

    }



    //----------------------------- CONFIGURAR CALENDARIO ---------------------------------------------

    private  void configurarCalendario(){

        Calendar dataMinima = configurarDataMinima();
        Calendar dataMaxima = configurarDataMaxima();

        calendarView.setMinDate(dataMinima.getTimeInMillis());
        calendarView.setMaxDate(dataMaxima.getTimeInMillis());

    }

    private Calendar configurarDataMinima(){
        Calendar dataMinima = Calendar.getInstance();
        int diaInicioCalendario = 1;
        dataMinima.set(ano_atual,mes_atual-1,diaInicioCalendario);
        return dataMinima;
    }

    private Calendar configurarDataMaxima(){
        Calendar dataMaxima = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        int diaFinalCalendario = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (dia_atual == diaFinalCalendario){
            dataMaxima.set(ano_atual,mes_atual,15);
        }else{
            dataMaxima.set(ano_atual,mes_atual-1,diaFinalCalendario);
        }
        return dataMaxima;
    }

    //----------------------------- CLICK CALENDARIO ---------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int anoSelecionado, int mesSelecionado, int diaSelecionado) {
        int mes = mesSelecionado + 1;
        //Toast.makeText(getContext(), "Dia: "+ diaSelecionado + "\nMes: " + mes + "\nAno: " + anoSelecionado, Toast.LENGTH_SHORT).show();

        dataSelecionada(diaSelecionado,mes,anoSelecionado);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void dataSelecionada(int diaSelecionado, int mesSelecionado, int anoSelecionado){
        Locale locale = new Locale("pt", "BR");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", locale);
        Calendar data = Calendar.getInstance();
        try {
            data.setTime(simpleDateFormat.parse(diaSelecionado+"/"+mesSelecionado+"/"+anoSelecionado));
            boolean disponivelAgendamento;

            if (mesSelecionado != mes_atual){
                disponivelAgendamento = true;
            }else{
                disponivelAgendamento = agendaDisponivel(data, diaSelecionado);
            }



            if (disponivelAgendamento){

                if (Util.statusInternet_MoWi(getContext())){
                    //----------------------------------------
                    //CHAMAR A PROXIMA ACTIVITY NESSE METODO
                    //----------------------------------------
                    String dia = String.valueOf(diaSelecionado);
                    String mes = String.valueOf(mesSelecionado);
                    String ano = String.valueOf(anoSelecionado);

                    ArrayList<String> dataList = new ArrayList<String>();
                    dataList.add(dia);
                    dataList.add(mes);
                    dataList.add(ano);

                    Intent intent = new Intent(getContext(), HorariosActivity.class);
                    intent.putExtra("data",dataList);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(), "Erro - Sem conexao com a internet", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }


    //----------------------------- VERIFICA SE  O DIA ESTA DISPONIVEL ---------------------------------------------


    private boolean agendaDisponivel(Calendar data, int diaSelecionado){
        Calendar calendar = Calendar.getInstance();
        int diaFinalCalendario = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (diaFinalCalendario == dia_atual){
            Toast.makeText(getContext(), "Agendamento disponivel do dia 1 para frente", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            Toast.makeText(getContext(), "Infelismente não trabalhamos no domingo!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (diaSelecionado < dia_atual){
            Toast.makeText(getContext(), "Agendamento disponivel do dia "+(dia_atual+1) + " pra frente!", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }



    //----------------------------- VERIFICA A VERSAO DO SO PARA AJUSTE DO CALENDAR ---------------------------------------------


    private void versaoLollipop(){

        int versao = Build.VERSION.SDK_INT;
        if (versao <= Build.VERSION_CODES.LOLLIPOP){

            WindowManager windowManager = (WindowManager)getActivity().getSystemService(getActivity().WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();

            Point tamanho = new Point();
            display.getSize(tamanho);

            int width = tamanho.x;
            if (width == 480){
                calendarView.getLayoutParams().width = 730;
                calendarView.getLayoutParams().height = 500;
            }else{
                calendarView.getLayoutParams().width = 800;
                calendarView.getLayoutParams().height = 650;
            }

        } else {

        }

    }

}
