package com.example.agendacliente.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.agendacliente.R;

import java.util.List;

public class AdapterListView extends BaseAdapter {


    private List<String> horarios;
    private Activity activity;
    private ClickItemListView clickItemListView;


    public AdapterListView(Activity activity, List<String> horarios, ClickItemListView clickItemListView){
        this.activity = activity;
        this.horarios = horarios;
        this.clickItemListView = clickItemListView;

    }

    @Override
    public int getCount() {
        return horarios.size();
    }

    @Override
    public Object getItem(int position) {
        return horarios.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = activity.getLayoutInflater().inflate(R.layout.listview_item,parent, false);
        TextView textView = view.findViewById(R.id.textView_ListViewItem);
        CardView cardView = view.findViewById(R.id.cardView_ListView);

        textView.setText(horarios.get(position));

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItemListView.clickItem(horarios.get(position),position);
            }
        });



        return view;
    }


    public interface ClickItemListView{
        void clickItem(String horario, int position);

    }
}
