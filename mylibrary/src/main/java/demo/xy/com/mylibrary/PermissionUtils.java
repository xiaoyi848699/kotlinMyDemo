package demo.xy.com.mylibrary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by xy on 2018/12/6.
 */
public class PermissionUtils {
    private static Dialog dialog;
    /**
     * 检查是否拥有指定的所有权限
     */
    public static boolean checkPermissionAllGranted(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }
    //请求权限
    public static void requestPermissions(final Activity activity, String permissionHint, final String[] permissions, final int requestCode) {
        if(null != dialog && dialog.isShowing()){
            dialog.dismiss();
        }
        dialog = new AlertDialog.Builder(activity)
                .setMessage(permissionHint)
                .setPositiveButton(activity.getString(R.string.i_konw), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(VersionUtils.checkSDKVersion(23)){
                            ActivityCompat.requestPermissions(activity,permissions, requestCode);
                        }else{
                            Toast.makeText(activity,activity.getString(R.string.refuse_permission),Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.setCancelable(false);
        dialog.show();
    }
}
