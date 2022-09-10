package ru.svolf.pcompiler.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import ru.svolf.pcompiler.R
import ru.svolf.pcompiler.settings.Preferences
import ru.svolf.pcompiler.util.StrF

/**
 * Created by Snow Volf on 26.10.2017, 22:49
 */

class HelpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        val content = findViewById<TextView>(R.id.textView)
        content.text = StrF.parseText("help/help.txt")
        val read = findViewById<Button>(R.id.buttonPanel)
        read.setOnClickListener { _ ->
            Preferences.setHelpShowed()
            finishAfterTransition()
        }
    }

    override fun onBackPressed() {}
}
