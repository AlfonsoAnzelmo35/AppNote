package com.example.horizantal_scroll;

public class Nota {
    private int id ;
    private String titolo, testo, percorsoImmagine ;

    public Nota(int id, String titolo, String testo, String percorsoImmagine){
        this.titolo = titolo ;
        this.testo = testo ;
        this.percorsoImmagine = percorsoImmagine;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getPercorsoImmagine() {
        return percorsoImmagine;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setPercorsoImmagine(String percorsoImmagine) {
        this.percorsoImmagine = percorsoImmagine;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }
}
