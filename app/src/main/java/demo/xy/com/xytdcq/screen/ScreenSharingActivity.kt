package demo.xy.com.xytdcq.screen

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import demo.xy.com.xytdcq.R

class ScreenSharingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_sharing)
    }
    fun btnClick(v: View){
        when(v.id){
            R.id.service -> startActivity(Intent(this, ScreenServiceActivity::class.java))
            R.id.client -> startActivity(Intent(this, ScreenClientActivity::class.java))

        }
    }
}
