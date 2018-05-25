package theekransje.douaneapp.Interfaces;

import theekransje.douaneapp.Domain.Driver;

/**
 * Created by Sander on 5/24/2018.
 */

public interface OnLoginResult {
    public void onLoginSucces(Driver chauffeur);
    public void onLoginFailure(String error);
}
