package com.example.mydebts.transaction;

import java.io.Serializable;

public class DebtsModel implements Serializable {
    // ID,NOM,MONTANT,NOTE,TYPE,ETAT,DATE
    public    String NOM;
    public    String NOTE;
    public    String DATE;
    public    String TIME;
    public    int ID;
    public    int ETAT;
    public    int TYPE;
    public    int ID_ACCOUNT;
    public    double    MONTANT;


        public DebtsModel(int ID, String NOM, double MONTANT, String NOTE, int TYPE, int ETAT, String DATE, String TIME, int ID_ACCOUNT  ){
            this.ID = ID;
            this.NOM = NOM;
            this.MONTANT = MONTANT;
            this.NOTE = NOTE;
            this.TYPE = TYPE;
            this.ETAT = ETAT;
            this.DATE = DATE;
            this.TIME = TIME;
            this.ID_ACCOUNT=ID_ACCOUNT;

        }
}
