package oob.fingerprinttest.Domain.Main.RegisterUseCase;

import oob.fingerprinttest.Data.Main.RegisterUseCase.RegisterUseCaseRepository;

public class RegisterUseCase implements RegisterUseCaseRepositoryInterface.Callback {
    private RegisterUseCaseViewInterface view;
    private RegisterUseCaseRepositoryInterface repository;

    public RegisterUseCase(RegisterUseCaseViewInterface view) {
        this.view = view;
        this.repository = new RegisterUseCaseRepository(this.view.getContext());
    }

    public void register(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            this.view.showEmptyUsernamePasswordWarning();
            return;
        }

        this.repository.register(username, password, this);
    }

    @Override
    public void onRegisterSuccess() {
        this.view.showRegistrationSuccessDialog();
        this.view.showFingerprintIcon();
    }

    @Override
    public void onRegisterError() {
        this.view.showRegistrationErrorsWarning();
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
