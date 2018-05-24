package theekransje.douaneapp.Domain;

/**
 * Created by Sander on 5/24/2018.
 */

public enum DouaneStatus {
    ERROR ,                   // Er is iets fout gegaan
    NONE,
    VERZONDEN,                // De aangifte is verzonden, we wachten op antwoord
    VERTREK_OK, 			  // Vertrek is goedgekeurd door de douane
    GEANNULEERD,             // De zending is geannuleerd. De chauffeur mag niet gaan rijden.
    GEEN_VRIJGAVE,           // Douane geeft de zending niet vrij. De chauffeur mag niet gaan rijden.
    CONTROLE,				  // De douane wil de lading eerst controleren. De chauffeur mag niet gaan rijden.
    LOSSEN_OK,				  // De lading is vrijgegeven om te worden gelost.
    VOORBEREID_INCOMPLEET,   // De aangifte is onderhande, maar niet niet volledig. De aangifte kan al wel ingepland worden, maar mag nog niet worden verstuurd.
    VOORBEREID_COMPLEET,     // De aangifte staat klaar om verstuurd te worden.
}
