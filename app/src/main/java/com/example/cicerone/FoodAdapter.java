package com.example.cicerone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cicerone.Attivita;

import java.util.ArrayList;

public class FoodAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Attivita> foodModelArrayList;
    private ArrayList<Integer> nRichieste;
    private ArrayList<Prenotazione> p;
    private ArrayList<Utente> u;
    private String chiamante;
    private Integer id;

    public FoodAdapter(Context context, ArrayList<Attivita> foodModelArrayList,String chiamante) {
        this.context = context;
        this.foodModelArrayList = foodModelArrayList;
        this.chiamante = chiamante;
    }

    public FoodAdapter(Context context, ArrayList<Attivita> foodModelArrayList,ArrayList<Integer> r,String chiamante) {
        this.context = context;
        this.foodModelArrayList = foodModelArrayList;
        this.nRichieste = r;
        this.chiamante = chiamante;
    }

    public FoodAdapter(Context context, Integer id, ArrayList<Prenotazione> p, ArrayList<Utente> u,String chiamante) {
        this.context = context;
        this.id=id;
        this.p=p;
        this.u=u;
        this.chiamante = chiamante;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        if(chiamante.equals("dettaglio"))
            return p.size();
        else return foodModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        if(chiamante.equals("dettaglio"))
            return p.get(position);
        else return foodModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.multiple_columns, null, true);

            holder.id = (TextView) convertView.findViewById(R.id.id);
            holder.citta = (TextView) convertView.findViewById(R.id.citta);
            holder.data = (TextView) convertView.findViewById(R.id.data);
            holder.partecipanti = (TextView) convertView.findViewById(R.id.partecipanti);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        if(chiamante.equals("dettaglio")){
            holder.id.setText("" + id);
            holder.citta.setText(u.get(position).getNome()+" "+u.get(position).getCognome());
            holder.data.setText(u.get(position).getEmail());
            holder.partecipanti.setText(""+p.get(position).getPartecipanti());
        }
        else {
            holder.id.setText("" + foodModelArrayList.get(position).getIdAttivita());
            holder.citta.setText(foodModelArrayList.get(position).getCitta());
            holder.data.setText(foodModelArrayList.get(position).getData());

            if (chiamante.equals("richieste"))
                holder.partecipanti.setText("" + nRichieste.get(position));
            if (chiamante.equals("modifica"))
                holder.partecipanti.setText("" + foodModelArrayList.get(position).getMaxPartecipanti());
            if (chiamante.equals("cerca")) {
                int h = foodModelArrayList.get(position).getMaxPartecipanti() - nRichieste.get(position);
                holder.partecipanti.setText("" + h);
            }
        }

        return convertView;
    }

    private class ViewHolder {

        protected TextView id, citta, data, partecipanti;

    }
}