package com.example.horizantal_scroll;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActivityPersonalizzaHome extends AppCompatActivity {

    View view_btn_anteprima ;
    ListView lista_widget_disponibili ;//lista dei widget disponibili per modificare la home
    ListView lista_widget_correnti;//lista dei widget disponibili per modificare la home
    ArrayList<String> listaWidgetCorrenti = new ArrayList<>() ;
    ArrayList<String> listaWisponibili = new ArrayList<>()  ;
    ArrayAdapter arrayAdapter ;
    ArrayAdapter arrayAdapterCorrente ;

    enum widget_disponibili{
        BlocchettoAppunti,
        Calendario,
        NotaFissata,
        Taccuini,
        Attivita
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Debug","ActivityPersonalizzazioneHome onCreate") ;
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_personalizza_home);

        lista_widget_disponibili = findViewById(R.id.lista_widget_disponibili) ;
        lista_widget_correnti =  findViewById(R.id.lista_widget_correnti) ;

        view_btn_anteprima = findViewById(R.id.btn_anteprima) ;
        view_btn_anteprima.setOnClickListener((View v)->mostraAnteprima(v));

        //riempi l'array dei widgetDisponibili con quelli dentro enum
        for(widget_disponibili w : widget_disponibili.values()){
            listaWisponibili.add(w.toString()) ;
        }

        //lista per widget disponibili
        arrayAdapter = new ArrayAdapter(getApplicationContext(),
                R.layout.singoloitem_menu_disponibili,R.id.singolo_elemento_widget_disponibili, listaWisponibili) ;
        lista_widget_disponibili.setAdapter(arrayAdapter);

        //attacca il listener alla lista_widget_disponibili
        lista_widget_disponibili.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id)
                        ->itemListaDisponibiliCliccato(parent,view,position,id)) ;

        //lista per widget correnti
        arrayAdapterCorrente = new ArrayAdapter(getApplicationContext(),
                R.layout.singoloitem_menu_corrente,R.id.singolo_elemento_widget_corrente, listaWidgetCorrenti) ;
        lista_widget_correnti.setAdapter(arrayAdapterCorrente);

        //attacca il listener alla lista_widget_corrente
        lista_widget_correnti.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id)
                ->itemListaCorrenteCliccato(parent,view,position,id)) ;

    }

    public void mostraAnteprima(View v){
        //invia richiesta
        Intent intentCreaWidget = new Intent(this, MainActivity.class) ;
        intentCreaWidget.putExtra("listaWidgetCorrenti",listaWidgetCorrenti) ;
        startActivity(intentCreaWidget);
    }
    /**
     * questa funzione è il listener della lista disponibili
     **/
    public void itemListaDisponibiliCliccato(AdapterView<?> parent, View view, int position, long id){

        Log.d("Debug","ActivityPersolizzazioneHome itemListaDisponibiliCliccato ") ;
        switch (position){
            case 0://blocchetto appunti
                Log.d("Debug","ActivityPersolizzazioneHome aggiungo Blocchetto ") ;

                creaBlocchettoAppunti(null);
                aumentaDimensioneLista(arrayAdapterCorrente, lista_widget_correnti);
                lista_widget_correnti.invalidate();
                break ;
            case 1://calendario
                creaCalendario(null) ;
                aumentaDimensioneLista(arrayAdapterCorrente, lista_widget_correnti);
                lista_widget_correnti.invalidate();
                break;
            case 2://nota fissata
                creaNotaFissata(null) ;
                aumentaDimensioneLista(arrayAdapterCorrente, lista_widget_correnti);
                lista_widget_correnti.invalidate();
                break;
            case 3://taccuino
                creaTaccuino(null) ;
                aumentaDimensioneLista(arrayAdapterCorrente, lista_widget_correnti);
                lista_widget_correnti.invalidate();
                break;
            case 4://attivita
                creaAttivita(null);
                aumentaDimensioneLista(arrayAdapterCorrente, lista_widget_correnti);
                lista_widget_correnti.invalidate();
                break;

            default:
                break;

        }

        aumentaDimensioneLista(arrayAdapterCorrente, lista_widget_correnti);


    }

    /**
     * questa funzione è il listener della lista corrente
     **/
    public void itemListaCorrenteCliccato(AdapterView<?> parent, View view, int position, long id) {


    }

    /**
     * questa funzione crea un intent per dire alla
     * MainActivity di aggiungere un blocchetto appunti
     **/
    public void creaBlocchettoAppunti(View v) {
        Log.d("Debug","ActivityPersolizzazioneHome dentro aggiungo Blocchetto ") ;
        listaWidgetCorrenti.add(widget_disponibili.BlocchettoAppunti.toString()) ;
    }

    /**
     * questa funzione crea un intent per dire alla
     * MainActivity di aggiungere un calendario
     **/
    public void creaCalendario(View v){
        listaWidgetCorrenti.add(widget_disponibili.Calendario.toString()) ;

        listaWisponibili.remove(1) ;     //togli il calendario dall'array
        arrayAdapter.notifyDataSetChanged();   //avvisa del cambiamento la lista associata

        riduciDimensioneLista(arrayAdapter, lista_widget_disponibili);
        lista_widget_disponibili.invalidate();
    }
    /**
     * questa funzione crea un intent per dire alla
     * MainActivity di aggiungere un blocchetto appunti
     **/
    public void creaNotaFissata(View v) {
        listaWidgetCorrenti.add(widget_disponibili.NotaFissata.toString()) ;
    }

    /**
     * questa funzione crea un intent per dire alla
     * MainActivity di aggiungere un taccuino
     **/
    public void creaTaccuino(View v) {
        listaWidgetCorrenti.add(widget_disponibili.Taccuini.toString()) ;

        listaWisponibili.remove(3) ;     //togli il taccuni dall'array
        arrayAdapter.notifyDataSetChanged();   //avvisa del cambiamento la lista associata

        riduciDimensioneLista(arrayAdapter, lista_widget_disponibili);
        lista_widget_disponibili.invalidate();
    }
    public void creaAttivita(View v) {
        listaWidgetCorrenti.add(widget_disponibili.Attivita.toString()) ;
    }

    /**
     * aumenta l'altezza della list view a seconda del numero di elementi
     */
    public void aumentaDimensioneLista(ArrayAdapter arrayAdapter, ListView listView){

        View listItem = arrayAdapter.getView(0,null,listView) ;
        ViewGroup.LayoutParams lp = listView.getLayoutParams();

        listItem.measure(0,0);
        int altezzaItem = listItem.getMeasuredHeight() ;

        if(arrayAdapter.getCount() > 10)
            lp.height = 12 * altezzaItem + 100;
        else
            lp.height = arrayAdapter.getCount()* altezzaItem + 100;

        listView.setLayoutParams(lp);

    }

    /**
     * aumenta l'altezza della list view a seconda del numero di elementi
     */
    public void riduciDimensioneLista(ArrayAdapter arrayAdapter, ListView listView){

        View listItem = arrayAdapter.getView(0,null,listView) ;
        ViewGroup.LayoutParams lp = listView.getLayoutParams();

        listItem.measure(0,0);
        int altezzaItem = listItem.getMeasuredHeight() ;

        Log.d("Debug ", "list View measured height :" + listView.getMeasuredHeight()) ;

        lp.height = listView.getMeasuredHeight() - altezzaItem;

        listView.setLayoutParams(lp);

    }

    @Override
    protected void onResume() {
        Log.d("Debug","ActivityPersonalizzaHome onResume") ;
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.d("Debug","ActivityPersonalizzaHome onStart") ;
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d("Debug","ActivityPersonalizzaHome onPause") ;
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("Debug","ActivityPersonalizzaHome onStop") ;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("Debug","ActivityPersonalizzaHome onDestroy") ;
        super.onDestroy();
    }
}
