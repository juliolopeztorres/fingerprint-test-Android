package oob.fingerprinttest.Domain.Main.LoginWithFingerprintUseCase;

import oob.fingerprinttest.Data.Main.LoginWithFingerprintUseCase.LoginWithFingerprintUseCaseRepository;

public class LoginWithFingerprintUseCase implements LoginWithFingerprintUseCaseRepositoryInterface.LoginWithFingerprintCallback {
    private LoginWithFingerprintUseCaseViewInterface view;
    private LoginWithFingerprintUseCaseRepositoryInterface repository;

    public LoginWithFingerprintUseCase(LoginWithFingerprintUseCaseViewInterface view) {
        this.view = view;
        this.repository = new LoginWithFingerprintUseCaseRepository(this.view.getContext());
    }

    public void login() {
        this.repository.login(this);
    }

    @Override
    public void onLoginSuccess() {
        this.view.goToAuthenticatedScreen();
    }

    @Override
    public void onLoginError() {
        this.view.showLoginErrorWarning();
    }

    @Override
    public void onListenerSetUp() {
        this.view.showFingerprintReadyDialog();
    }

    @Override
    public void onListenerResult() {
        this.view.hideFingerprintReadyDialog();
    }
}
