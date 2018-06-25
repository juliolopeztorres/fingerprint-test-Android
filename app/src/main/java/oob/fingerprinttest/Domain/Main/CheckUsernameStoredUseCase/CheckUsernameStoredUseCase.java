package oob.fingerprinttest.Domain.Main.CheckUsernameStoredUseCase;

import oob.fingerprinttest.Data.Main.CheckUsernameStoredUseCase.CheckUsernameStoredUseCaseRepository;

public class CheckUsernameStoredUseCase {
    private CheckUsernameStoredUseCaseViewInterface view;
    private CheckUsernameStoredUseCaseRepositoryInterface repository;

    public CheckUsernameStoredUseCase(CheckUsernameStoredUseCaseViewInterface view) {
        this.view = view;
        this.repository = new CheckUsernameStoredUseCaseRepository(this.view.getContext());
    }

    public boolean check() {
        return this.repository.check();
    }
}
