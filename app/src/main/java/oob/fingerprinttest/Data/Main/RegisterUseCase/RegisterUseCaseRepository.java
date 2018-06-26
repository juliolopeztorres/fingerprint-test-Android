package oob.fingerprinttest.Data.Main.RegisterUseCase;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import oob.fingerprinttest.Data.Main.FingerprintDependenciesWrapper;
import oob.fingerprinttest.Data.Main.FingerprintListener;
import oob.fingerprinttest.Domain.Main.RegisterUseCase.RegisterUseCaseRepositoryInterface;
import oob.fingerprinttest.Framework.ApplicationContext;

public class RegisterUseCaseRepository implements RegisterUseCaseRepositoryInterface, FingerprintListener.ListenerCallback {

    private SharedPreferences sharedPreferences;
    private FingerprintListener fingerprintListener;
    private Callback callback;

    private String username;
    private String password;

    public RegisterUseCaseRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences(ApplicationContext.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        this.fingerprintListener = new FingerprintListener(this);
    }

    @Override
    public void register(String username, String password, Callback callback) {
        this.fingerprintListener.startAuth(FingerprintDependenciesWrapper.getFingerprintManager(), FingerprintDependenciesWrapper.getCryptoObjectForEncrypting());

        this.username = username;
        this.password = password;
        this.callback = callback;
        this.callback.onListenerSetUp();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void authenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        this.callback.onListenerResult();

        String encryptedPassword = FingerprintDependenciesWrapper.encryptString(result.getCryptoObject().getCipher(), this.password);

        if (encryptedPassword == null) {
            this.callback.onRegisterError();
        } else {
            this.sharedPreferences.edit()
                    .putString(ApplicationContext.USERNAME_KEY, this.username)
                    .putString(ApplicationContext.ENCRYPTED_PASSWORD_KEY, encryptedPassword)
                    .apply();
            this.callback.onRegisterSuccess();
        }


        this.cleanDependenciesValues();
    }

    @Override
    public void authenticationFailed(String error) {
        if (this.callback == null) {
            return;
        }

        this.callback.onListenerResult();
        this.callback.onRegisterError();

        this.cleanDependenciesValues();
    }

    private void cleanDependenciesValues() {
        this.username = null;
        this.password = null;
        this.fingerprintListener.cancel();
        this.callback = null;
    }
}
