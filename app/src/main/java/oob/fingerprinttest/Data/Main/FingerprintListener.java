package oob.fingerprinttest.Data.Main;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintListener extends FingerprintManagerCompat.AuthenticationCallback {
    private ListenerCallback callback;
    private CancellationSignal cancellationSignal;

    public FingerprintListener(ListenerCallback callback) {
        this.callback = callback;
    }

    public void startAuth(FingerprintManagerCompat manager, FingerprintManagerCompat.CryptoObject cryptoObject) {
        this.cancellationSignal = new CancellationSignal();

        try {
            manager.authenticate(cryptoObject, 0, this.cancellationSignal, this, null);
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
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        this.callback.authenticationSucceeded(result);
    }

    public interface ListenerCallback {
        void authenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result);

        void authenticationFailed(String error);
    }
}