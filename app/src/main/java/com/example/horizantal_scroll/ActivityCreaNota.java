package com.example.horizantal_scroll;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ActivityCreaNota extends AppCompatActivity {
    ImageView imageView;
    View layoutImmagini;
    View cardView_aggiungiImmagine ;
    TextView button_back;
    TextView titolo;
    TextView textArea;
    View bottone_aggiungi_foto;
    Bitmap immagine;


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_nota);

        cardView_aggiungiImmagine = findViewById(R.id.layout_immagini);
        layoutImmagini = findViewById(R.id.contenitore_immagini);

        bottone_aggiungi_foto = findViewById(R.id.bottone_aggiungi_foto);
        bottone_aggiungi_foto.setOnClickListener((View v) -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 451);
        });

        button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener((View v) -> {
            Intent intent = new Intent();
            if (textArea.getText().toString().length() == 0) {
                intent.putExtra("risposta", "scrittoNulla");
                setResult(453, intent);
            } else {
                if(immagine!= null)
                    inserisciNelDB(true);
                else inserisciNelDB(false);
                intent.putExtra("risposta", textArea.getText().toString());
                setResult(453, intent);
            }
            onBackPressed();
        });

        titolo = findViewById(R.id.crea_nota_titolo);
        titolo.setOnClickListener((View v) -> {
            button_back.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.done, 0, 0, 0);
        });

        textArea = findViewById(R.id.textArea);
        textArea.setOnClickListener((View v) -> {
            button_back.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.done, 0, 0, 0);
        });


        if (getIntent().hasExtra("immagine")) {
            cardView_aggiungiImmagine.setVisibility(View.VISIBLE);
            immagine = getIntent().getParcelableExtra("immagine");
            imageView = new ImageView(this);
            imageView.setImageBitmap(immagine);

            imageView.setMinimumWidth(600);
            imageView.setMinimumHeight(600);
            ((LinearLayout) layoutImmagini).addView(imageView);
        }
    }

    //salva nota nel DB, di cui : titolo, testo e percorsoImmagine(solo il nome)
    public void inserisciNelDB(boolean camera){
        Log.d("Debug","ActivityCreaNota : Scrittura dalla nota in DB note ") ;

        if(textArea.getText().toString().length() <= 0 || titolo.getText().toString().length() <= 0) {
            Toast.makeText(this, "inserisci il testo e il titolo!!", Toast.LENGTH_SHORT).show();
            return;
        }

        NoteDBHelper dbHelper = new NoteDBHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase() ;

        // crea una mappa
        ContentValues values = new ContentValues();
        values.put(SchemaNote.Note.COLUMN_NAME_TITLE, titolo.getText().toString());
        values.put(SchemaNote.Note.COLUMN_NAME_TEXT, textArea.getText().toString());
        if(!camera)
            values.put(SchemaNote.Note.COLUMN_NAME_IMAGE_PATH, "nessunaImmagneFornita");
        else {
            //qui inserisco il percorso dell'immagine salvato in una directory
            String path = salvaImmagineInInternalStorage();
            values.put(SchemaNote.Note.COLUMN_NAME_IMAGE_PATH, path);
        }
        //inserisci nel DB
        long newRowId = sqLiteDatabase.insert(SchemaNote.Note.TABLE_NAME, null, values);

    }

    //si occupa di salvare l'immagine nell'internalStorage
    private String salvaImmagineInInternalStorage(){
        FileOutputStream fos = null;
        try{
            fos = openFileOutput(titolo.getText().toString()+".jpg", MODE_PRIVATE) ;
            if(!immagine.compress(Bitmap.CompressFormat.JPEG, 95, fos))
                throw new IOException("compress fallita");

            return titolo.getText().toString()+".jpg" ;


        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Debug", "onActivityResult called in Activity creaNota" +
                "con resultCode = " + resultCode +
                " requestCode = " + requestCode + " data : " + data);
        switch (requestCode) { // da anctivity CreaNota
            case 451: //da camera
                Log.d("Debug", "camera  ");

                System.out.println("camera");
                immagine =(Bitmap) data.getExtras().get("data") ;
                imageView = new ImageView(this);
                imageView.setImageBitmap(immagine);
                imageView.setMinimumWidth(600);
                imageView.setMinimumHeight(600);
                ((LinearLayout) layoutImmagini).addView(imageView);


                break;
        }
    }

    @Override
    protected void onResume() {
        Log.d("Debug","ActivityCreaNota onResume") ;
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.d("Debug","ActivityCreaNota onStart") ;
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d("Debug","ActivityCreaNota onPause") ;
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("Debug","ActivityCreaNota onStop") ;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("Debug","ActivityCreaNota onDestroy") ;
        super.onDestroy();
    }
}