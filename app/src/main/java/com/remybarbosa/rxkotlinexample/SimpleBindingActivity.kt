package com.remybarbosa.rxkotlinexample

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_simple_binding.*

class SimpleBindingActivity : AppCompatActivity() {

    companion object {
        private fun intent(context: Context): Intent {
            return Intent(context, SimpleBindingActivity::class.java)
        }

        fun start(context: Context) {
            context.startActivity(intent(context))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_binding)

        editText.toObservable()
                .map { text -> text + text }
                .subscribe { finalText ->
                    textView.text = finalText
                }
    }

    fun EditText.toObservable(): Observable<String> {
        return Observable.create({
            val watcher = object : TextWatcher {

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    it.onNext(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            }
            this.addTextChangedListener(watcher)
            it.setCancellable { this.removeTextChangedListener(watcher) }
        })
    }

}
