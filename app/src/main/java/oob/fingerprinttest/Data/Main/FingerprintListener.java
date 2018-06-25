package oob.fingerprinttest.Data.Main;

import android.annotation.TargetApi;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintListener extends FingerprintManager.AuthenticationCallback {
    private ListenerCallback callback;
    private CancellationSignal cancellationSignal;

    public FingerprintListener(ListenerCallback callback) {
        this.callback = callback;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        this.cancellationSignal = new CancellationSignal();

        try {
            manager.authenticate(cryptoObject, this.cancellationSignal, 0, this, null);
        } catch (SecurityException ex) {
            this.callback.authenticationFailed(ex.getMessage());
        } catch (Exception ex) {
            this.callback.authenticationFailed(ex.getMessage());
        }
    }

    public void cancel() {
        if (this.cancellationSignal != null) {
            this.cancellationSignal.cancel();
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.callback.authenticationFailed(errString.toString());
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.callback.authenticationFailed(helpString.toString());
    }

    @Override
    public void onAuthenticationFailed() {
        this.callback.authenticationFailed("Authentication failed");
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.callback.authenticationSucceeded(result);
    }

    public interface ListenerCallback {
        void authenticationSucceeded(FingerprintManager.AuthenticationResult result);

        void authenticationFailed(String error);
    }
}