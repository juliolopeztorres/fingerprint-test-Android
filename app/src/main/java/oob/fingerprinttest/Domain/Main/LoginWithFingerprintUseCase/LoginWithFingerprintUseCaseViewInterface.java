package oob.fingerprinttest.Domain.Main.LoginWithFingerprintUseCase;

import android.content.Context;

public interface LoginWithFingerprintUseCaseViewInterface {
    void goToAuthenticatedScreen();

    void showLoginErrorWarning();

    void showFingerprintReadyDialog();

    void hideFingerprintReadyDialog();

    Context getContext();
}
