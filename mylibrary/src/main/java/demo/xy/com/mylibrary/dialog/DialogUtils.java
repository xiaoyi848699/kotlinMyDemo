package demo.xy.com.mylibrary.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;

import demo.xy.com.mylibrary.R;
import demo.xy.com.mylibrary.log.LogUtil;


/**
 * Created by xy on 2017/10/16.
 * 使用dialog
 */
public class DialogUtils {
    private static Dialog dialog;
    private static Dialog dialogSingle;

    private static Dialog dialogPermission;
    public static int  maxWaitTime = 15 * 1000;

    public static void show(final Activity activity, final String msg) {
        try {
            if (null != dialog && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.e("DialogUtils show");
                    dialog = DialogFactory.createLoadingDialog(activity, msg,maxWaitTime);
    //                dialog.setCancelable(false);
                        dialog.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void show(final Activity activity, final String msg, final boolean isCanCanel) {
        try {
            if (null != dialog && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.e("DialogUtils show");
                    dialog = DialogFactory.createLoadingDialog(activity, msg,maxWaitTime);
                    dialog.setCancelable(isCanCanel);
                    dialog.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void showSingle(final Activity activity, final String msg, final boolean isCanCanel, final int maxWaitTime) {
        try {
            if (null != dialogSingle && dialogSingle.isShowing()) {
                dialogSingle.dismiss();
                dialogSingle = null;
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.e("DialogUtils dialogSingle");
                    dialogSingle = DialogFactory.createLoadingDialog(activity, msg,maxWaitTime);
                    dialogSingle.setCancelable(isCanCanel);
                    dialogSingle.show();
                }
            });
        } catch (Exception e) {
            LogUtil.e("DialogUtils showSingle Exception"+e.getMessage());
        }

    }

    public static void dismissSingle() {
        if (null != dialogSingle && dialogSingle.isShowing()) {
            LogUtil.e("DialogUtils dismissSingle");
            try {
                dialogSingle.dismiss();
            } catch (Exception e) {
                LogUtil.e("DialogUtils dismissSingle Exception"+e.getMessage());
            }
            dialogSingle = null;
        }
    }

    public static void dismiss() {
        if (null != dialog && dialog.isShowing()) {
            LogUtil.e("DialogUtils dismiss");
            try {
                dialog.dismiss();
            } catch (Exception e) {
                LogUtil.e("DialogUtils dismiss Exception"+e.getMessage());
            }
            dialog = null;
        }
    }

    public static void showPermissionDialog(final Activity activity, String msg) {
        dialogPermission = DialogFactory.createTipDialog(activity,
                msg, activity.getString(R.string.btn_cancel), activity.getString(R.string.btn_to_set), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.tip_comfirm) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + activity.getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            activity.startActivity(intent);
                        }
                        dialogPermission.dismiss();
                        dialogPermission = null;
                    }
                });
        dialogPermission.show();
    }
    public static void releaseDialog(){
        dialogPermission = null;
        dialog = null;
    }
}
