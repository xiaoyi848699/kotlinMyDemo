package demo.xy.com.xytdcq

import android.content.ComponentName
import android.content.pm.PackageManager
import android.view.View
import demo.xy.com.mylibrary.base.BaseAtivity

/**
 * 修改APP icon
 */
class ChangeAPPIconActivity : BaseAtivity() {
    override fun getLayout(): Int {
       return  R.layout.activity_change_appicon
    }

    override fun setDataAndEvent() {
        //修改icon用(修改后  需要关闭后台进程 等待大概10s左右  桌面会自动切换)
//        mDefault = componentName
        mDefault2 = ComponentName(
                baseContext,
                "demo.xy.com.xytdcq.MainActivity")
        mDouble11 = ComponentName(
                baseContext,
                "demo.xy.com.xytdcq.Test11")
        mDouble12 = ComponentName(
                baseContext,
                "demo.xy.com.xytdcq.Test12")
        mPm = applicationContext.packageManager
    }

    //    private var mDefault: ComponentName? = null
    private var mDefault2: ComponentName? = null
    private var mDouble11: ComponentName? = null
    private var mDouble12: ComponentName? = null
    private var mPm: PackageManager? = null

    fun btnClick(v: View){
        when(v.id){
            R.id.button1 ->{
                disableComponent(this!!.mDefault2!!)
                disableComponent(this!!.mDouble12!!)
                enableComponent(this!!.mDouble11!!)
            }
            R.id.button2 ->{
                disableComponent(this!!.mDefault2!!)
                disableComponent(this!!.mDouble11!!)
                enableComponent(this!!.mDouble12!!)
            }
            R.id.button3 ->{
                disableComponent(this!!.mDouble11!!)
                disableComponent(this!!.mDouble12!!)
                enableComponent(this!!.mDefault2!!)
            }
        }
    }
    private fun enableComponent(componentName: ComponentName) {
        mPm!!.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)
    }

    private fun disableComponent(componentName: ComponentName) {
        mPm!!.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
    }
}
