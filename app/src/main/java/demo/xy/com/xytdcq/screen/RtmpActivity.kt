package demo.xy.com.xytdcq.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import demo.xy.com.xytdcq.BaseAtivity
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.uitls.PermissionUtils
import demo.xy.com.xytdcq.uitls.VersionUtils

class RtmpActivity : BaseAtivity() {
    private val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
    private val STORAGE_REQUEST_CODE = 102
    private val REQUEST_CODE = 1
    private var mMediaProjectionManager: MediaProjectionManager? = null
    private var mCreateScreenCaptureResultCode: Int = 0
    private var mCreateScreenCaptureResultData: Intent? = null
    // Now, Using service
    override fun getLayout(): Int {
        return  R.layout.activity_rtmp
    }
    override fun setDataAndEvent() {
        val isAllGranted = PermissionUtils.checkPermissionAllGranted(this, permissions)
        if (!isAllGranted) {
            //弹提示框
            if (VersionUtils.checkSDKVersion(23)) {
                ActivityCompat.requestPermissions(this, permissions, STORAGE_REQUEST_CODE)
            }
        }
        mMediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }
    fun btnClick(v: View){
        when(v.id){
            R.id.button ->{
                if (/*mRecorder != null*/ mCreateScreenCaptureResultCode != 0 && mCreateScreenCaptureResultData != null) {
                    val stopCastIntent = Intent(ScreenRecorderService.ACTION_STOP)
                    sendBroadcast(stopCastIntent)
                } else {
                    val captureIntent = mMediaProjectionManager!!.createScreenCaptureIntent()
                    startActivityForResult(captureIntent, REQUEST_CODE)
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CODE) {

            mCreateScreenCaptureResultCode = resultCode
            mCreateScreenCaptureResultData = data

            if (mCreateScreenCaptureResultCode != 0 && mCreateScreenCaptureResultData != null) {

                Log.d("@@", "Starting ScreenRecorderService...")

                val intent = Intent(this, ScreenRecorderService::class.java)
                intent.putExtra(ScreenRecorderService.EXTRA_RESULT_CODE, mCreateScreenCaptureResultCode)
                intent.putExtra(ScreenRecorderService.EXTRA_RESULT_DATA, mCreateScreenCaptureResultData)
                intent.putExtra(ScreenRecorderService.EXTRA_RTMP_ADDRESS, "rtmp://192.168.1.104/videotest/test")
                startService(intent)

                Toast.makeText(this, "Screen recorder is running...", Toast.LENGTH_SHORT).show()
            }
            //moveTaskToBack(true);
        }
    }
}
