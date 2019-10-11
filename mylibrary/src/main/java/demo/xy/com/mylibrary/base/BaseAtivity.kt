package demo.xy.com.mylibrary.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * Created by xy on 2018/10/19.
 */
abstract class BaseAtivity : AppCompatActivity(){
    var unbinder : Unbinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        //注册ButterKnife
        unbinder = ButterKnife.bind(this)
        setDataAndEvent()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder?.unbind()//!!.为空会报异常
    }

    /**
     * 设置当前Activity的布局
     */
    protected abstract fun getLayout(): Int

    /**
     * 设置数据和事件
     */
    protected abstract fun setDataAndEvent()
}