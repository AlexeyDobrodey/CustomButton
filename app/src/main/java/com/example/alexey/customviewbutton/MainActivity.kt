package com.example.alexey.customviewbutton

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.EditText
import com.example.alexey.views.CustomButton
import com.example.alexey.views.CustomImageView
import kotlinx.android.synthetic.main.test_image.*

class MainActivity : AppCompatActivity() {

//    private lateinit var customButton: CustomButton
//
//    private lateinit var btnUpdateFont: Button
//
//    private lateinit var etText: EditText
//    private lateinit var etMinTextSize: EditText
//    private lateinit var etTextSize: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_image)


//        btnUpdateFont = findViewById(R.id.btnUpdateFont)
//        etText = findViewById(R.id.etText)
//        etMinTextSize = findViewById(R.id.etMinTextSize)
//        etTextSize = findViewById(R.id.etTextSize)
//
//        customButton = findViewById(R.id.custom_button)
//
//        btnUpdateFont.setOnClickListener {
//            val minTextSize = if (etMinTextSize.text.isNullOrEmpty().not()) etMinTextSize.text.toString().toInt() else 0
//            val textSize = if (etTextSize.text.isNullOrEmpty().not()) etTextSize.text.toString().toInt() else 0
//            val text = etText.text.toString()
//            customButton.setTextData(text, minTextSize, textSize)
//        }


        civ_test.setOnClickListener {
            civ_test.changeCrop()
        }
    }

    override fun onStart() {
        super.onStart()
        civ_test.viewTreeObserver.addOnDrawListener(listenerOnDraw)
    }

    override fun onStop() {
        super.onStop()
        civ_test.viewTreeObserver.removeOnDrawListener(listenerOnDraw)
    }

    private var listenerOnDraw = object: ViewTreeObserver.OnDrawListener{
        override fun onDraw() {
            tvCounter.text = civ_test.cropShape.toString()
        }
    }
}
