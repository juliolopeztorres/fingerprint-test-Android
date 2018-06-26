package oob.fingerprinttest.Data.Main.CheckFingerprintSensorUseCase;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import oob.fingerprinttest.Data.Main.FingerprintDependenciesWrapper;
import oob.fingerprinttest.Domain.Main.CheckFingerprintSensorUseCase.CheckFingerprintSensorUseCaseRepositoryInterface;

public class CheckFingerprintSensorUseCaseRepository implements CheckFingerprintSensorUseCaseRepositoryInterface {

    private Context context;

    public CheckFingerprintSensorUseCaseRepository(Context context) {
        this.context = context;
    }

    @Override
    public void check(FingerprintSensorCallback callback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callback.onAndroidVersionLowerThanMarshmallow();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            callback.onPermissionNotGranted();
            return;
        }

        KeyguardManager keyguardManager = (KeyguardManager) this.context.getSystemService(Context.KEYGUARD_SERVICE);

        if (keyguardManager == null || !keyguardManager.isKeyguardSecure()) {
            callback.onLockScreenNotSecured();
            return;
        }

        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(this.context);

        if (!fingerprintManager.isHardwareDetected()) {
            callback.onHardwareNotDetected();
            return;
        }

        if (!fingerprintManager.hasEnrolledFingerprints()) {
            callback.onNoFingerprintsEnrolled();
            return;
        }

        callback.onFingerprintSensorDetected();
    }

    @Override
    public void initFingerprintDependencies() {
        FingerprintDependenciesWrapper.init(this.context);
    }
}
