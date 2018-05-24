package theekransje.douaneapp.Domain;

import java.time.LocalDate;

/**
 * Created by Sander on 5/24/2018.
 */

public class MRMFormulier {



        public String Mrn;
        public DouaneStatus Status;
        public String Reference;
        public LocalDate DateTime;
        public String Afzender;
        public String Ontvanger;
        public String Opdrachtgever;
        public int AantalArtikelen;
        public double TotaalBedrag;
        public String Currency;
        public double TotaalGewicht;

}
