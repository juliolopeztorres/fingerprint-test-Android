package oob.fingerprinttest.Domain.Main.RegisterUseCase;

public interface RegisterUseCaseRepositoryInterface {

    void register(String username, String password, Callback callback);

    interface Callback {
        void onRegisterSuccess();

        void onRegisterError();

        void onListenerSetUp();

        void onListenerResult();
    }
}
