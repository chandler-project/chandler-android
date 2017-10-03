package com.chandlersystem.chandler.utilities;

public class DialogUtil {
    /*private static final String TAG_MESSAGE_DIALOG = "message-dialog";
    private static final String TAG_FORCE_UPDATE_DIALOG = "force-update-dialog";
    private static final String TAG_UPLOAD_IMAGE_DIALOG = "upload-image-dialog";
    private static final String TAG_UNAUTHORIZED_DIALOG = "un-authorized_dialog";

    private DialogUtil() {
        // Private constructor for class which full of constants
    }

    *//**
     * Show a pop-up whose message is a string converting from throwable
     *
     * @param context
     * @param throwable
     *//*
    public static void showMessageDialog(Context context, Throwable throwable) {
        String error = ErrorUtil.getErrorMessage(context, throwable);
        showMessageDialog(context, error);
    }

    public static void showUnauthorizationDialog(Context context, Throwable throwable) {
        String error = ErrorUtil.getErrorMessage(context, throwable);
        UnauthorizedDialog dialog = UnauthorizedDialog.getDialog(error);
        showDialog(context, dialog, TAG_UNAUTHORIZED_DIALOG);
    }

    *//**
     * The same as @showMessageDialog, but errors is explicit with String
     *
     * @param context
     * @param error
     *//*
    public static void showMessageDialog(Context context, String error) {
//        MessageDialog messageDialog = MessageDialog.getDialog(error);
        MessageDialog messageDialog = MessageDialog.getDialog(error);
        showDialog(context, messageDialog, TAG_MESSAGE_DIALOG);
    }

    *//**
     * Show force-update dialog, which has one button to connect to google play store
     *
     * @param context
     * @param content
     *//*
    public static void showForceUpdateDialog(Context context, String content) {
        ForceUpdateDialog forceUpdateDialog = ForceUpdateDialog.getDialog(content);
        showDialog(context, forceUpdateDialog, TAG_FORCE_UPDATE_DIALOG);
    }

    *//**
     * Show pure @DialogFragment with TAG
     *
     * @param context
     * @param dialog
     * @param tag
     *//*
    public static void showDialog(Context context, DialogFragment dialog, String tag) {
        if (context instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) context;
            // Check if activity state is available or not
            // If it was destroyed or paused, then show nothing
            if (!appCompatActivity.isFinishing()) {
                try {
                    FragmentManager fm = appCompatActivity
                            .getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    dialog.show(ft, tag);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    *//**
     * Create new progress dialog with message inside, then show it
     *
     * @param context
     * @param mess
     * @return
     *//*
    public static ProgressDialog showProgressDialog(Context context, String mess) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(mess);
        progressDialog.show();
        return progressDialog;
    }

    *//**
     * Show error message. In this app, it was a blur layout and a pop-up overlay on it
     *
     * @param context
     * @param throwable
     *//*
    public static void showErrorLayout(Context context, Throwable throwable) {
        showMessageDialog(context, throwable);
    }

    *//**
     * Check if progress dialog is available, then hide it
     *
     * @param progressDialog
     *//*
    public static void hideProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void showUploadImageDialog(Context context) {
        UploadImageDialog dialog = UploadImageDialog.getDialogInstance();
        showDialog(context, dialog, TAG_UPLOAD_IMAGE_DIALOG);
    }*/
}
