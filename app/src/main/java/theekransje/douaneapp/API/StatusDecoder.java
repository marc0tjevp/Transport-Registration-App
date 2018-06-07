package theekransje.douaneapp.API;

import theekransje.douaneapp.Domain.DouaneStatus;

/**
 * Created by Sander on 6/7/2018.
 */

public class StatusDecoder {

    public static DouaneStatus decodeStatusCode(int code) {

        DouaneStatus d = DouaneStatus.NONE;
        switch (code) {
            case -1:
                d = DouaneStatus.ERROR;
                break;
            case 0:
                d = DouaneStatus.NONE;
                break;
            case 1:
                d = DouaneStatus.VERZONDEN;
                break;
            case 8:
                d = DouaneStatus.VERTREK_OK;
                break;
            case 13:
                d = DouaneStatus.GEANNULEERD;
                break;
            case 18:
                d = DouaneStatus.GEEN_VRIJGAVE;
                break;
            case 22:
                d = DouaneStatus.CONTROLE;
                break;
            case 25:
                d = DouaneStatus.VERTREK_OK;
                break;
            case 36:
                d = DouaneStatus.VOORBEREID_INCOMPLEET;
                break;
            case 37:
                d = DouaneStatus.VOORBEREID_COMPLEET;
                break;
        }
        return d;

    }
}
