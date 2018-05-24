package theekransje.douaneapp.Interfaces;

import theekransje.douaneapp.Domain.Chauffeur;

/**
 * Created by Sander on 5/24/2018.
 */

public interface OnLoginResult {
    public void onLoginSucces(Chauffeur chauffeur);
    public void onLoginFailure(String error);
}
