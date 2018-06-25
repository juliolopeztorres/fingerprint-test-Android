package oob.fingerprinttest.Data.Main.CheckUsernameStoredUseCase;

import android.content.Context;
import android.content.SharedPreferences;

import oob.fingerprinttest.Domain.Main.CheckUsernameStoredUseCase.CheckUsernameStoredUseCaseRepositoryInterface;
import oob.fingerprinttest.Framework.ApplicationContext;

public class CheckUsernameStoredUseCaseRepository implements CheckUsernameStoredUseCaseRepositoryInterface {
    private SharedPreferences sharedPreferences;

    public CheckUsernameStoredUseCaseRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences(ApplicationContext.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean check() {
        return !this.sharedPreferences.getString(ApplicationContext.USERNAME_KEY, "").isEmpty();
    }
}
