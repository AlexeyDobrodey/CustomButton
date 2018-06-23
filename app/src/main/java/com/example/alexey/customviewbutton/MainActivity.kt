package com.example.alexey.customviewbutton

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.example.alexey.views.CustomButton

class MainActivity : AppCompatActivity() {

    private lateinit var customButton: CustomButton

    private lateinit var btnUpdateFont: Button

    private lateinit var etText: EditText
    private lateinit var etMinTextSize: EditText
    private lateinit var etTextSize: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnUpdateFont = findViewById(R.id.btnUpdateFont)
        etText = findViewById(R.id.etText)
        etMinTextSize = findViewById(R.id.etMinTextSize)
        etTextSize = findViewById(R.id.etTextSize)

        customButton = findViewById(R.id.custom_button)

        btnUpdateFont.setOnClickListener {
            val minTextSize = if(etMinTextSize.text.isNullOrEmpty().not()) etMinTextSize.text.toString().toInt() else 0
            val textSize = if(etTextSize.text.isNullOrEmpty().not()) etTextSize.text.toString().toInt() else 0
            val text = etText.text.toString()
            customButton.setTextData(text, minTextSize, textSize)
        }
    }
}
