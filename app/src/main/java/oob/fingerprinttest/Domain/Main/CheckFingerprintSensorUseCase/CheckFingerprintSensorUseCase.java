package oob.fingerprinttest.Domain.Main.CheckFingerprintSensorUseCase;

import oob.fingerprinttest.Data.Main.CheckFingerprintSensorUseCase.CheckFingerprintSensorUseCaseRepository;

public class CheckFingerprintSensorUseCase implements CheckFingerprintSensorUseCaseRepositoryInterface.FingerprintSensorCallback {
    private CheckFingerprintSensorUseCaseViewInterface view;
    private CheckFingerprintSensorUseCaseRepositoryInterface repository;

    public CheckFingerprintSensorUseCase(CheckFingerprintSensorUseCaseViewInterface view) {
        this.view = view;
        this.repository = new CheckFingerprintSensorUseCaseRepository(this.view.getContext());
    }

    public void check() {
        this.repository.check(this);
    }

    @Override
    public void onAndroidVersionLowerThanMarshmallow() {
        this.view.showAndroidVersionLowerThanMarshmallowWarning();
    }

    @Override
    public void onPermissionNotGranted() {
        this.view.showPermissionNotGrantedWarning();
    }

    @Override
    public void onHardwareNotDetected() {
        this.view.showHardwareNotDetectedWarning();
    }

    @Override
    public void onLockScreenNotSecured() {
        this.view.showLockScreenNotSecuredWarning();
    }

    @Override
    public void onFingerprintSensorDetected() {
        this.repository.initFingerprintDependencies();
        this.view.showFingerprintSensorIconReady();
    }

    @Override
    public void onNoFingerprintsEnrolled() {
        this.view.showNoFingerprintsEnrolledWarning();
    }
}
