package oob.fingerprinttest.Framework.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import oob.fingerprinttest.Domain.Main.CheckFingerprintSensorUseCase.CheckFingerprintSensorUseCase;
import oob.fingerprinttest.Domain.Main.CheckFingerprintSensorUseCase.CheckFingerprintSensorUseCaseViewInterface;
import oob.fingerprinttest.Domain.Main.CheckUsernameStoredUseCase.CheckUsernameStoredUseCase;
import oob.fingerprinttest.Domain.Main.CheckUsernameStoredUseCase.CheckUsernameStoredUseCaseViewInterface;
import oob.fingerprinttest.Domain.Main.LoginWithFingerprintUseCase.LoginWithFingerprintUseCase;
import oob.fingerprinttest.Domain.Main.LoginWithFingerprintUseCase.LoginWithFingerprintUseCaseViewInterface;
import oob.fingerprinttest.Domain.Main.RegisterUseCase.RegisterUseCase;
import oob.fingerprinttest.Domain.Main.RegisterUseCase.RegisterUseCaseViewInterface;
import oob.fingerprinttest.Framework.Util.DialogUtil;
import oob.fingerprinttest.R;

public class MainActivity extends AppCompatActivity implements
        CheckFingerprintSensorUseCaseViewInterface,
        RegisterUseCaseViewInterface,
        LoginWithFingerprintUseCaseViewInterface,
        CheckUsernameStoredUseCaseViewInterface {

    @BindView(R.id.usernameET)
    EditText usernameET;
    @BindView(R.id.passwordET)
    EditText passwordET;
    @BindView(R.id.fingerprintIconIV)
    View fingerprintIconIV;

    private CheckFingerprintSensorUseCase checkFingerprintSensorUseCase;
    private CheckUsernameStoredUseCase checkUsernameStoredUseCase;
    private RegisterUseCase registerUseCase;
    private LoginWithFingerprintUseCase loginWithFingerprintUseCase;

    private boolean deviceHasFingerprintSensor = false;
    private boolean canAuthenticateViaFingerprintSensor = false;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        this.tintActionBarTextColor();
        this.injectUseCases();

        this.checkFingerprintSensorUseCase.check();
    }

    private void tintActionBarTextColor() {
        Toolbar actionBarToolbar = this.findViewById(R.id.action_bar);
        if (actionBarToolbar != null) {
            actionBarToolbar.setTitleTextColor(this.getResources().getColor(R.color.colorAccent));
        }
    }

    private void injectUseCases() {
        this.checkFingerprintSensorUseCase = new CheckFingerprintSensorUseCase(this);
        this.registerUseCase = new RegisterUseCase(this);
        this.loginWithFingerprintUseCase = new LoginWithFingerprintUseCase(this);
        this.checkUsernameStoredUseCase = new CheckUsernameStoredUseCase(this);
    }

    @OnClick(R.id.registerBtnLayoutLL)
    public void onRegisterBtnClicked() {
        if (!this.deviceHasFingerprintSensor) {
            DialogUtil.showAlertDialog(
                    this,
                    this.getString(R.string.main_warning_dialog_title),
                    this.getString(R.string.main_regular_register_dialog_message),
                    this.getString(R.string.main_close_dialog_positive_button)
            );
            return;
        }

        this.registerUseCase.register(this.usernameET.getText().toString(), this.passwordET.getText().toString());
    }

    @OnClick(R.id.loginBtnLayoutLL)
    public void onLoginBtnClicked() {
        if (!this.deviceHasFingerprintSensor || !this.canAuthenticateViaFingerprintSensor) {
            DialogUtil.showAlertDialog(
                    this,
                    this.getString(R.string.main_warning_dialog_title),
                    this.getString(R.string.main_regular_login_dialog_message),
                    this.getString(R.string.main_close_dialog_positive_button)
            );
            return;
        }

        this.loginWithFingerprintUseCase.login();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void goToAuthenticatedScreen() {
        this.startActivity(
                new Intent(this.getContext(), AuthenticatedActivity.class)
        );
    }

    @Override
    public void showLoginErrorWarning() {
        DialogUtil.showAlertDialog(
                this,
                this.getString(R.string.main_error_dialog_title),
                this.getString(R.string.main_incorrect_username_password_dialog_message),
                this.getString(R.string.main_close_dialog_positive_button)
        );
    }

    @Override
    public void showFingerprintReadyDialog() {
        Drawable icon = this.getResources().getDrawable(R.drawable.fingerprint_icon);
        icon.setColorFilter(this.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        this.alertDialog = DialogUtil.showWaitingAlertDialog(
                this,
                this.getString(R.string.main_fingerprint_dialog_title),
                icon,
                this.getString(R.string.main_fingerprint_dialog_message)
        );
    }

    @Override
    public void hideFingerprintReadyDialog() {
        this.alertDialog.dismiss();
        Drawable icon = this.getResources().getDrawable(R.drawable.fingerprint_icon);
        icon.setColorFilter(this.getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void showRegistrationSuccessDialog() {
        DialogUtil.showAlertDialog(
                this,
                this.getString(R.string.main_register_complete_dialog_title),
                this.getString(R.string.main_register_complete_dialog_message),
                this.getString(R.string.main_close_dialog_positive_button)
        );
        this.canAuthenticateViaFingerprintSensor = true;
        this.clearInputs();
    }

    @Override
    public void showRegistrationErrorsWarning() {
        DialogUtil.showAlertDialog(
                this,
                this.getString(R.string.main_error_dialog_title),
                this.getString(R.string.main_errors_registration_dialog_message),
                this.getString(R.string.main_close_dialog_positive_button)
        );
    }

    @Override
    public void showEmptyUsernamePasswordWarning() {
        DialogUtil.showAlertDialog(
                this,
                this.getString(R.string.main_error_dialog_title),
                this.getString(R.string.main_empty_username_password_dialog_message),
                this.getString(R.string.main_close_dialog_positive_button)
        );
    }

    @Override
    public void showFingerprintIcon() {
        this.fingerprintIconIV.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAndroidVersionLowerThanMarshmallowWarning() {
        DialogUtil.showAlertDialog(
                this.getContext(),
                this.getString(R.string.main_error_dialog_title),
                this.getString(R.string.main_low_android_version_dialog_message),
                this.getString(android.R.string.ok)
        );
    }

    @Override
    public void showPermissionNotGrantedWarning() {
        DialogUtil.showAlertDialog(
                this.getContext(),
                this.getString(R.string.main_error_dialog_title),
                this.getString(R.string.main_fingerprint_permission_missing_dialog_message),
                this.getString(android.R.string.ok)
        );
    }

    @Override
    public void showHardwareNotDetectedWarning() {
        DialogUtil.showAlertDialog(
                this.getContext(),
                this.getString(R.string.main_error_dialog_title),
                this.getString(R.string.main_no_fingerprint_device_dialog_message),
                this.getString(android.R.string.ok)
        );
    }

    @Override
    public void showLockScreenNotSecuredWarning() {
        DialogUtil.showAlertDialog(
                this.getContext(),
                this.getString(R.string.main_error_dialog_title),
                this.getString(R.string.main_no_secure_lockscreen_set_dialog_message),
                this.getString(android.R.string.ok)
        );
    }

    @Override
    public void showFingerprintSensorIconReady() {
        this.deviceHasFingerprintSensor = true;

        this.fingerprintIconIV.setVisibility(View.GONE);
        if (this.checkUsernameStoredUseCase.check()) {
            this.canAuthenticateViaFingerprintSensor = true;
            this.fingerprintIconIV.setVisibility(View.VISIBLE);
        }
    }

    private void clearInputs() {
        this.usernameET.setText("");
        this.passwordET.setText("");
    }
}
