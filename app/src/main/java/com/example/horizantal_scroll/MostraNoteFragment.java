package com.example.horizantal_scroll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MostraNoteFragment extends Fragment {
    Context context;
    LinearLayout layout_contenitore_cardViews ;//contenitore cardView (in cui ci sono le note)
    ArrayList<Nota> notaArrayList;
    SQLiteDatabase sqLiteDatabase;
    NoteDBHelper dbHelper;
    public MostraNoteFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("Debug","MostraNoteFragment : onCreateView") ;

        context = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.mostra_note_fragment_layout, container, false);
        layout_contenitore_cardViews = view.findViewById(R.id.layout_contenitore_cardViews) ;

        notaArrayList = leggiDalDB() ;
        mostraNoteDalDB();
        return view;
    }
    private void eleminaImmagineDalInternalStorage(String filename){
        getActivity().deleteFile(filename) ;
    }

    //si occupa di mostrare le note prese dal DB
    public void mostraNoteDalDB(){

        Drawable mio_cardview = getActivity().getDrawable(R.drawable.mio_cardview);
        ImageView imageView = null ;

        for(int i = 0 ; i < notaArrayList.size() ; i++) {
            CardView cardView = new CardView(context);

            ViewGroup.LayoutParams params = new CardView.LayoutParams(150, 230);
            params.width = 420;
            params.height = 630;
            ((ViewGroup.MarginLayoutParams)params).setMargins(20, 0, 20, 0);

            cardView.setLayoutParams(params);
            cardView.setBackground(mio_cardview);
            cardView.setCardElevation(20);

            LinearLayout linearLayout = new LinearLayout(context) ;
            ViewGroup.LayoutParams linear_linearLayoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(linear_linearLayoutparams);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            cardView.addView(linearLayout);

            TextView textView = new TextView(context);
            textView.setId(notaArrayList.get(i).getId() - 1);//auto increment parte da 1
            textView.setText(notaArrayList.get(i).getTitolo() + "\n" + notaArrayList.get(i).getTesto()
            + notaArrayList.get(i).getPercorsoImmagine());
            textView.setPadding(20, 20, 0, 0);
            textView.setTextSize(15);
            textView.setMinimumHeight(230);
            textView.setMinimumWidth(150);
            textView.setTextColor(Color.WHITE);
            textView.setOnLongClickListener((View v)-> {
                notaCliccata(v);
                return true;
            });

            linearLayout.addView(textView);

            if(!(notaArrayList.get(i).getPercorsoImmagine() == null)) {
                if(!(notaArrayList.get(i).getPercorsoImmagine().equals("null")) &&!(notaArrayList.get(i).getPercorsoImmagine().equals("nessunaImmagneFornita"))) {
                    if(!(notaArrayList.get(i).getPercorsoImmagine().contains("/"))) {

                        imageView = caricaImmagineDalInternalStorage(notaArrayList.get(i).getPercorsoImmagine());
                        if(imageView != null)
                            linearLayout.addView(imageView);
                    }
            }}

            layout_contenitore_cardViews.addView(cardView);

        }
    }

    //carica il file dall'internal storage
    private ImageView caricaImmagineDalInternalStorage(String filename){

        ImageView imageView = new ImageView(context) ;
        imageView.setMinimumHeight(300);
        imageView.setMinimumWidth(300);
        byte[]bytes = new byte[4096];
        byte []buffer = new byte[0] ;
        byte []temp_buffer = new byte[4096]; ;

        FileInputStream fis = null;
        int n = 0;
        try{
            fis = getActivity().openFileInput(filename) ;
            while ((n = fis.read(bytes, 0, bytes.length))> -1) {
                temp_buffer = new byte[buffer.length + n];
                System.arraycopy(buffer, 0, temp_buffer, 0, buffer.length);
                System.arraycopy(bytes, 0, temp_buffer, buffer.length, n);
                buffer = temp_buffer ;
            }
            Bitmap immagine = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);   //interpreta come Bitmap
            Drawable drawable = new BitmapDrawable(immagine);
            imageView.setImageDrawable(drawable);
            return imageView;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("ResourceType")
    public void notaCliccata(View v){
        Log.d("Debug","elimino dal file storage");
        Toast.makeText(context, "ELIMINATA: id view : " + v.getId() + " nota " + notaArrayList.get( v.getId() - 1).getTitolo() +  notaArrayList.get( v.getId()-1).getTesto(), Toast.LENGTH_SHORT).show();
        eleminaImmagineDalInternalStorage(notaArrayList.get( v.getId()-1).getTitolo());
        eliminaDalDB(notaArrayList.get( v.getId()-1).getTitolo());
    }

    public void eliminaDalDB(String filename){
        Log.d("Debug","elimino dal db");
        String selection = SchemaNote.Note.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = { filename };
        int deletedRows = sqLiteDatabase.delete(SchemaNote.Note.TABLE_NAME, selection, selectionArgs);

    }
    //effettua una query per prendere tutte le note dal DB
    public ArrayList<Nota> leggiDalDB() {
        Log.d("Debug", "lettura dal DB note in MainActivity");
        dbHelper = new NoteDBHelper(context);
        sqLiteDatabase = dbHelper.getReadableDatabase();
        ArrayList<Nota> notaArrayList = new ArrayList<>();

        String[] projection = {
                BaseColumns._ID,
                SchemaNote.Note.COLUMN_NAME_TITLE,
                SchemaNote.Note.COLUMN_NAME_TEXT,
                SchemaNote.Note.COLUMN_NAME_IMAGE_PATH
        };

        Cursor cursor = sqLiteDatabase.query(
                SchemaNote.Note.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String titolo = cursor.getString(1);
            String testo = cursor.getString(2);
            String percorsoImmagine = cursor.getString(3);
            Nota nota = new Nota(Integer.parseInt(id), titolo, testo, percorsoImmagine);
            notaArrayList.add(nota);
        }
        cursor.close();
        return notaArrayList ;
    }



    //metodi per il ciclo di vita




    public void onCreate(Bundle savedInstanceState) {
        Log.d("Debug","MostraNoteFragment onCreate") ;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d("Debug","MostraNoteFragment onAttach") ;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Log.d("Debug","MostraNoteFragment onDetach") ;
        super.onDetach();
    }

    @Override
    public void onStart() {
        Log.d("Debug","MostraNoteFragment onStart") ;
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("Debug","MostraNoteFragment onResume") ;


        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("Debug","MostraNoteFragment onPause") ;
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("Debug","MostraNoteFragment onStop") ;
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("Debug","MostraNoteFragment onDestroy") ;

        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.d("Debug","MostraNoteFragment onDestroyView") ;
        super.onDestroyView();
    }
}
