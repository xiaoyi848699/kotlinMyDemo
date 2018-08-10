package demo.xy.com.xytdcq.uitls;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by pcdalao on 2017/9/28.
 */

public class PermissionUtils {
    private static Dialog dialog;
    /**
     * 检查是否拥有指定的所有权限
     */
    public static boolean checkPermissionAllGranted(Activity activities, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activities, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }
}
