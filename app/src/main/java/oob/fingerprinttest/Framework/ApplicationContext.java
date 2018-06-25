package oob.fingerprinttest.Framework;

import android.app.Application;

import oob.fingerprinttest.Data.Main.FingerprintDependenciesWrapper;

public class ApplicationContext extends Application {
    public static final String SHARED_PREFERENCES_NAME = "Fingerprint Test";

    public static final String USERNAME_KEY = "username";
    public static final String ENCRYPTED_PASSWORD_KEY = "encrypted password";
    public static final String PREFERENCES_IV_KEY= "iv";

    @Override
    public void onCreate() {
        super.onCreate();

        FingerprintDependenciesWrapper.init(this.getApplicationContext());
    }
}
