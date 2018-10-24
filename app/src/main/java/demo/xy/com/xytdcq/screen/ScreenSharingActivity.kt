package demo.xy.com.xytdcq.screen

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.uitls.PermissionUtils
import demo.xy.com.xytdcq.uitls.VersionUtils

class ScreenSharingActivity : AppCompatActivity() {

    private val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
    private val STORAGE_REQUEST_CODE = 102
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_sharing)
        val isAllGranted = PermissionUtils.checkPermissionAllGranted(this, permissions)
        if (!isAllGranted) {
            //弹提示框
            if (VersionUtils.checkSDKVersion(23)) {
                ActivityCompat.requestPermissions(this, permissions, STORAGE_REQUEST_CODE)
            }
        }
    }
    fun btnClick(v: View){
        when(v.id){
            R.id.service -> startActivity(Intent(this, ScreenServiceActivity::class.java))
            R.id.client -> startActivity(Intent(this, ScreenClientActivity::class.java))

        }
    }
}
