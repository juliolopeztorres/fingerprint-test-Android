package oob.fingerprinttest.Domain.Main.CheckFingerprintSensorUseCase;

public interface CheckFingerprintSensorUseCaseRepositoryInterface {
    void check(FingerprintSensorCallback callback);

    interface FingerprintSensorCallback {
        void onAndroidVersionLowerThanMarshmallow();

        void onPermissionNotGranted();

        void onHardwareNotDetected();

        void onLockScreenNotSecured();

        void onFingerprintSensorDetected();
    }
}
