package com.example.cicerone.data;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cicerone.R;

import java.util.ArrayList;

public class DettaglioRichieste extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_richieste);

        ListView lista = findViewById(R.id.listaAttivita);

        ArrayList<String> array = new ArrayList<>();
        ArrayList<Utente> u= new ArrayList<>(); //n richieste per attività
        Integer idAttivita = getIntent().getExtras().getInt("id");
        String chiamante = "richieste";

        final ArrayList<Prenotazione> p = DBhelper.getAllPrenotazioni(idAttivita,chiamante);

        final Integer[] ids;
        String avv="Nessuna richiesta trovata";
        int flag=0;
        int j=0;

        if(p==null||p.size()==0){
            Toast.makeText(DettaglioRichieste.this, "Nessuna richiesta trovata", Toast.LENGTH_SHORT).show();
            flag=1;
        }

        if (flag==0) {
            ids = new Integer[p.size()];
            for (Prenotazione b : p) {
                ids[j] = b.getIdAttivita();
                Utente u2 = DBhelper.getInfoUtentebyID(DettaglioRichieste.this,b.getId());
                u.add(u2);
                j++;
            }
        }
        else
            array.add(avv);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                DettaglioRichieste.this,android.R.layout.simple_list_item_1,array
        );

        EAdapter fadapter = new EAdapter(this,idAttivita,p,u,"dettaglio");

        if (flag==1)
            lista.setAdapter(adapter);

        else{
            lista.setAdapter(fadapter);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent inte = new Intent(DettaglioRichieste.this, GestioneRichiesta.class);
                    inte.putExtra("prenotazione",p.get(position));
                    startActivity(inte);
                    finish();
                }
            });
        }
    }
}
