package theekransje.douaneapp.Domain;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Sander on 5/24/2018.
 */

public class MRNFormulier implements Serializable {
        private static final String TAG = "MRNFormulier";

        public String Mrn;
        public String Reference;
        public String DateTime;
        public String Afzender;
        public String Ontvanger;
        public String Opdrachtgever;
        public int AantalArtikelen;
        public double TotaalBedrag;
        public String Currency;
        public double TotaalGewicht;
        private String ontvangstAdres;
        private String verzenderAdres;


        public static String getTAG() {
                return TAG;
        }

        public String getMrn() {
                return Mrn;
        }

        public void setMrn(String mrn) {
                Mrn = mrn;
        }

        public String getReference() {
                return Reference;
        }

        public void setReference(String reference) {
                Reference = reference;
        }

        public String getDateTime() {
                return DateTime;
        }

        public void String(String dateTime) {
                DateTime = dateTime;
        }

        public String getAfzender() {
                return Afzender;
        }

        public void setAfzender(String afzender) {
                Afzender = afzender;
        }

        public String getOntvanger() {
                return Ontvanger;
        }

        public void setOntvanger(String ontvanger) {
                Ontvanger = ontvanger;
        }

        public String getOpdrachtgever() {
                return Opdrachtgever;
        }

        public void setOpdrachtgever(String opdrachtgever) {
                Opdrachtgever = opdrachtgever;
        }

        public int getAantalArtikelen() {
                return AantalArtikelen;
        }

        public void setAantalArtikelen(int aantalArtikelen) {
                AantalArtikelen = aantalArtikelen;
        }

        public double getTotaalBedrag() {
                return TotaalBedrag;
        }

        public void setTotaalBedrag(double totaalBedrag) {
                TotaalBedrag = totaalBedrag;
        }

        public String getCurrency() {
                return Currency;
        }

        public void setCurrency(String currency) {
                Currency = currency;
        }

        public double getTotaalGewicht() {
                return TotaalGewicht;
        }

        public void setTotaalGewicht(double totaalGewicht) {
                TotaalGewicht = totaalGewicht;
        }

        public String getOntvangstAdres() {
                return ontvangstAdres;
        }

        public void setOntvangstAdres(String ontvangstAdres) {
                this.ontvangstAdres = ontvangstAdres;
        }

        public String getVerzenderAdres() {
                return verzenderAdres;
        }

        public void setVerzenderAdres(String verzenderAdres) {
                this.verzenderAdres = verzenderAdres;
        }
}
