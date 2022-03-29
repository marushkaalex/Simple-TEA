package com.malex.tea

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.malex.tea.tea.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val store = MyStore(
        State(),
        MyReducer(),
        MyActor(),
    )
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.text)
        textView.setOnClickListener {
            store.accept(Event.Ui.ReloadClick)
        }
        init()
    }

    private fun init() {
        store.accept(Event.Ui.Init)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                store.state.collect(::render)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                store.effects.collect(::handleEffect)
            }
        }
    }

    private fun render(state: State) {
        textView.text = when {
            state.isLoading -> "Loading.. . .. . "
            else -> state.value
        }
    }

    private fun handleEffect(effect: Effect) = when (effect) {
        Effect.ShowError -> Snackbar
            .make(textView, "Error!", Snackbar.LENGTH_SHORT)
            .show()
    }.also {
        println("kek effect: $effect")
    }
}