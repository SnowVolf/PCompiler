package ru.svolf.pcompiler.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.transition.Fade
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

/**
 * Created by Snow Volf on 19.08.2017, 12:09
 */

open class BaseActivity : AppCompatActivity() {
    //Theme
    private val mThemeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (SettingsActivity::class.java == this@BaseActivity.javaClass) {
                finish()
                startActivity(getIntent())
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } else
                recreate()
        }
    }

//    override fun attachBaseContext(base: Context) {
//        super.attachBaseContext(LocaleGirl.onAttach(base))
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.enterTransition = Explode()
        window.exitTransition = Fade()
        window.allowEnterTransitionOverlap = true
    }

    public override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mThemeReceiver)
        super.onDestroy()
    }
}
