package com.example.horizantal_scroll;

import com.example.horizantal_scroll.ActivityPersonalizzaHome.widget_disponibili;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LinearLayout linearLayoutPrincipale;
    LinearLayout ll_per_gestire_widget; //nel quale vengono aggiunti,rimossi widget
    LinearLayout layout_anteprima;
    FrameLayout contenitore_fragment_mostra_Note ;
    Button si_anteprima;
    Button no_anteprima;
    View aggiungi_widget;
    ArrayList<String> widgents_currents;//contiene gl'elementi dall'intent ActivityPersonalizzaHome, per l'anteprima

    FragmentManager fragmentManager ;
    MostraNoteFragment mostraNoteFragment = new MostraNoteFragment() ;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Debug","MainActivity onCreate");

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayoutPrincipale = findViewById(R.id.ll_principale);
        ll_per_gestire_widget = findViewById(R.id.ll_Per_Aggiugere_Widget);
        layout_anteprima = findViewById(R.id.layout_anteprima);
        contenitore_fragment_mostra_Note = findViewById(R.id.contenitore_fragment_mostra_Note) ;

        //se clicca si, semplicemente layout_anteprima sparisce
        si_anteprima = findViewById(R.id.si_anteprima);
        si_anteprima.setOnClickListener((View view) -> layout_anteprima.setVisibility(View.GONE));

        //se clicca no, rimuoviamo gl'elementi
        no_anteprima = findViewById(R.id.no_anteprima);
        no_anteprima.setOnClickListener((View view) -> {
            for (int i = 0; i < ll_per_gestire_widget.getChildCount(); i++) {
                View child = ll_per_gestire_widget.getChildAt(i);
                child.setVisibility(View.GONE);
            }
            layout_anteprima.setVisibility(View.GONE);
        });

        fragmentManager = getSupportFragmentManager() ;

        ottieniIntent_ActivityPersonalizzaHome();

        aggiungi_widget = findViewById(R.id.aggiungi_widget);
        aggiungi_widget.setOnClickListener((View v) -> mostraActivityPersonalizzazioneHome(v));

        Log.d("Debug","ricreo il fragment") ;
        //ricrea il frame
        fragmentManager.beginTransaction()
                .replace(R.id.contenitore_fragment_mostra_Note, mostraNoteFragment, "tag")
                .commit();


    }//fine OnCreate



    /**
     * questa funzione ottiene l'intent lanciato da ActivityPersonalizzaHome con startActivity
     * da cui prende l'extra, nel quale è presente listaWidgetCorrenti.
     * Usa listaWidgetCorrenti per decide se fare l'inflate(o addView) di tutte le view passate
     * appunto in listaWidgetCorrenti
     * */
    public void ottieniIntent_ActivityPersonalizzaHome(){
        if(getIntent().hasExtra("listaWidgetCorrenti")) {


            Log.d("Debug", "In mainActivity ha correnti");
            layout_anteprima.setVisibility(View.VISIBLE);

            widgents_currents = getIntent().getStringArrayListExtra("listaWidgetCorrenti");

            for(int i = 0; i < widgents_currents.size() ; i++){
                if(widgents_currents.get(i).equals(widget_disponibili.BlocchettoAppunti.toString())) {
                    getLayoutInflater().inflate(R.layout.blocchetto_appunti_layout,ll_per_gestire_widget,true);
                }
                if(widgents_currents.get(i).equals(widget_disponibili.Calendario.toString())) {
                    View calendario = new CalendarView(getApplicationContext()) ;
                    calendario.setBackgroundColor(Color.WHITE);
                    ll_per_gestire_widget.addView(calendario);
                }
                if(widgents_currents.get(i).equals(widget_disponibili.Taccuini.toString())) {
                    getLayoutInflater().inflate(R.layout.taccuini,ll_per_gestire_widget,true);
                }
                if(widgents_currents.get(i).equals(widget_disponibili.NotaFissata.toString())) {
                    getLayoutInflater().inflate(R.layout.nota_fissata,ll_per_gestire_widget,true);
                }
                if(widgents_currents.get(i).equals(widget_disponibili.Attivita.toString())) {
                    View view = getLayoutInflater().inflate(R.layout.attivita,ll_per_gestire_widget,true);
                    ListView l_view =(ListView) view.findViewById(R.id.list_attivita) ;

                    getLayoutInflater().inflate(R.layout.list_attivita_singolo_item_,null,false);
                    String []a = new String[]{"ciao","ciao2","ciao3"} ;
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.list_attivita_singolo_item_,
                            R.id.singolo_elemento_list_attivita ,
                            a) ;
                    l_view.setAdapter(arrayAdapter) ;
                }
            }
        }
    }

    public void mostraActivityPersonalizzazioneHome(View v){
        Intent intentActivityPersoHome = new Intent(this, ActivityPersonalizzaHome.class) ;
        startActivityForResult(intentActivityPersoHome,450);
    }

    /**
     * questa funzione il bottom dialog menu con
     * vai alle note, crea nuova nota , rimuovi widget
     **/
    public void showBottomDialogMenu(View v){
        Dialog dialog = new Dialog(this) ;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) ;
        dialog.setContentView(R.layout.bottom_dialog_menu);

        LinearLayout ly_vai_alle_note = dialog.findViewById(R.id.vai_alle_note) ;
        LinearLayout ly_crea_nuova_nota = dialog.findViewById(R.id.crea_nuova_nota) ;
        LinearLayout ly_rimuovi_nota = dialog.findViewById(R.id.rimuovi_widget) ;

        ly_vai_alle_note.setOnClickListener((View view)->{
            Toast.makeText(this, "Cliccato vai alle note", Toast.LENGTH_SHORT).show(); ;
        });

        ly_crea_nuova_nota.setOnClickListener((View view)->{
            Toast.makeText(this, "Cliccato vai alle note", Toast.LENGTH_SHORT).show(); ;
        });

        ly_rimuovi_nota.setOnClickListener((View view)->{
            Toast.makeText(this, "Cliccato vai alle note", Toast.LENGTH_SHORT).show(); ;
        });

        dialog.show();
        dialog.getWindow().setGravity(Gravity.BOTTOM) ;
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT) ;
        dialog.getWindow().setBackgroundDrawable(null);
    }

    /**
     * questa funzione deve mostra il bottom dialog menu crea
     * con 6 bottoni : fotocamera, alle
     * */
    public void showBottomDialogMenu_Crea(View view){
        Dialog dialog = new Dialog(this) ;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) ;
        dialog.setContentView(R.layout.crea_bottom_dialog_menu);

        //fai partire la activity crea nota
        View creaNotaButton = dialog.findViewById(R.id.creaNotaButton) ;
        creaNotaButton.setOnClickListener((View v)->startActivityCreaNota(v));

        //fai partire l'activity per la fotocamera
        View attivitaBtn = dialog.findViewById(R.id.btn_attivita) ;
        attivitaBtn.setOnClickListener((View v)->{
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE) ;
            startActivityForResult(cameraIntent,452);
        });


        dialog.show();
        dialog.getWindow().setGravity(Gravity.BOTTOM) ;
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT) ;
        dialog.getWindow().setBackgroundDrawable(null);
    }

    public void startActivityCreaNota(View v){
        Intent intent = new Intent(getApplicationContext(), ActivityCreaNota.class) ;
        startActivityForResult(intent,453);
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("Debug","onActivityResult called con resultCode = "+ resultCode +
                " requestCode = " + requestCode + " data : " + data) ;
        super.onActivityResult(requestCode, resultCode, data);
        //if (requestCode != 453 || requestCode != 452) return;
        //if (resultCode != Activity.RESULT_OK) return;
        if (data == null) return;
        switch (requestCode){ // da anctivity CreaNota
            case 453:
                String testo = data.getStringExtra("risposta");
                Log.d("Debug","risultato activity creaNota : " + testo) ;

                if (testo.equals("scrittoNulla"))
                    Toast.makeText(this, "nota non salvata!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "nota salvata!", Toast.LENGTH_LONG).show();


            break ;
            case 452: //da cameraIntent per MainActivity
                Log.d("Debug","camera  ") ;
                System.out.println("camera");
                Bitmap bitmap =(Bitmap) data.getExtras().get("data") ;

                //necessito di inviare anche un nome dell'immagine a AcitityCreaNota

                Intent intent = new Intent(getApplicationContext(), ActivityCreaNota.class) ;
                intent.putExtra("immagine",bitmap) ;
                startActivityForResult(intent,453);
                break;
        }


    }


    @Override
    protected void onResume() {
        Log.d("Debug","MainActivity onResume") ;


        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.d("Debug","MainActivity onStart") ;


        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d("Debug","MainActivity onPause") ;
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("Debug","MainActivity onStop") ;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("Debug","MainActivity onDestroy") ;
        super.onDestroy();
    }
}