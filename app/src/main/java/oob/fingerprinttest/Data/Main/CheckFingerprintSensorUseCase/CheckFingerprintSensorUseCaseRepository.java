package oob.fingerprinttest.Data.Main.CheckFingerprintSensorUseCase;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import oob.fingerprinttest.Data.Main.FingerprintDependenciesWrapper;
import oob.fingerprinttest.Domain.Main.CheckFingerprintSensorUseCase.CheckFingerprintSensorUseCaseRepositoryInterface;

public class CheckFingerprintSensorUseCaseRepository implements CheckFingerprintSensorUseCaseRepositoryInterface {

    private Context context;

    public CheckFingerprintSensorUseCaseRepository(Context context) {
        this.context = context;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void check(FingerprintSensorCallback callback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callback.onAndroidVersionLowerThanMarshmallow();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            callback.onPermissionNotGranted();
            return;
        }

        KeyguardManager keyguardManager = FingerprintDependenciesWrapper.getKeyguardManager();

        if (keyguardManager == null || !keyguardManager.isKeyguardSecure()) {
            callback.onLockScreenNotSecured();
            return;
        }

        FingerprintManager fingerprintManager = FingerprintDependenciesWrapper.getFingerprintManager();

        if (fingerprintManager == null || !fingerprintManager.isHardwareDetected()) {
            callback.onHardwareNotDetected();
            return;
        }

        callback.onFingerprintSensorDetected();
    }
}
