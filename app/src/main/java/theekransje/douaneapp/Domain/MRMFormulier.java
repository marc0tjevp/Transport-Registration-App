package theekransje.douaneapp.Domain;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Sander on 5/24/2018.
 */

public class MRMFormulier implements Serializable {
        private static final String TAG = "MRMFormulier";

        public String Mrn;
        public String Reference;
        public LocalDate DateTime;
        public String Afzender;
        public String Ontvanger;
        public String Opdrachtgever;
        public int AantalArtikelen;
        public double TotaalBedrag;
        public String Currency;
        public double TotaalGewicht;


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

        public LocalDate getDateTime() {
                return DateTime;
        }

        public void setDateTime(LocalDate dateTime) {
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
}
