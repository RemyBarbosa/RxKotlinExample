package com.remybarbosa.rxkotlinexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.remybarbosa.rxkotlinexample.activity_part_autocomplete.PartAutoComplete
import com.remybarbosa.rxkotlinexample.activity_part_autocomplete.PartAutoCompleteBlank
import com.remybarbosa.rxkotlinexample.part4and5.Part4And5
import com.remybarbosa.rxkotlinexample.part3.Part3
import com.remybarbosa.rxkotlinexample.part6.Part6
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        goSimpleBinding.setOnClickListener { Part3.start(this) }
        goSimpleBackgroundService.setOnClickListener { Part4And5.start(this) }
        goHandleError.setOnClickListener { Part6.start(this) }
        goHandleError.setOnClickListener { Part6.start(this) }
        goAutocomplete.setOnClickListener { PartAutoCompleteBlank.start(this) }
    }
}
