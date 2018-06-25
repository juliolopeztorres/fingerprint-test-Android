package oob.fingerprinttest.Framework.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;

public class DialogUtil {
    public static void showDialog(Context context,
                                  String title,
                                  String message,
                                  String positiveActionText,
                                  DialogInterface.OnClickListener positiveCallback,
                                  String negativeActionText,
                                  DialogInterface.OnClickListener negativeCallback) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveActionText, positiveCallback)
                .setNegativeButton(negativeActionText, negativeCallback)
                .show();
    }

    public static void showAlertDialog(Context context,
                                       String title,
                                       String message,
                                       String positiveActionText) {
        showAlertDialog(context, title, message, positiveActionText, null, true);
    }

    public static void showAlertDialog(Context context,
                                       String title,
                                       String message,
                                       String positiveActionText,
                                       DialogInterface.OnClickListener positiveCallback,
                                       boolean cancellable) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveActionText, positiveCallback)
                .setCancelable(cancellable)
                .show();
    }

    public static AlertDialog showWaitingAlertDialog(Context context,
                                                     String title,
                                                     @DrawableRes int icon,
                                                     String message) {
        return new AlertDialog.Builder(context)
                .setIcon(context.getResources().getDrawable(icon))
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .show();
    }

    public static AlertDialog showWaitingAlertDialog(Context context,
                                                     String title,
                                                     Drawable icon,
                                                     String message) {
        return new AlertDialog.Builder(context)
                .setIcon(icon)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .show();
    }
}
