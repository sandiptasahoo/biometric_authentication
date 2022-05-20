package com.example.examples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.biometric.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ExampleLiveDataAndFlowActivity : AppCompatActivity() {
    private lateinit var btnLiveData: Button
    private lateinit var btnStateFlow: Button
    private lateinit var btnFlow: Button
    private lateinit var btnSharedFlow: Button
    private lateinit var tvLiveData: TextView
    private lateinit var tvStateFLow: TextView
    private lateinit var tvFLow: TextView
    private lateinit var parent: ConstraintLayout
    private lateinit var mViewModel: ExampleLiveDataAndFlowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livedata_flow_example)
        mViewModel = ViewModelProvider(this).get(ExampleLiveDataAndFlowViewModel::class.java)

        btnLiveData = findViewById(R.id.btnLiveData)
        tvLiveData = findViewById(R.id.tvLiveData)
        btnStateFlow = findViewById(R.id.btnStateFlow)
        btnSharedFlow = findViewById(R.id.btnSharedFlow)
        tvStateFLow = findViewById(R.id.tvStateFlow)
        btnFlow = findViewById(R.id.btnFlow)
        tvFLow = findViewById(R.id.tvFlow)
        parent = findViewById(R.id.parent)

        btnLiveData.setOnClickListener {
            mViewModel.triggerLiveData()
        }
        btnStateFlow.setOnClickListener {
            mViewModel.triggerStateFlow()
        }
        btnFlow.setOnClickListener {
            /*Here I can use launch bcz it's not a state flow. Normal flow doesn't contain the state.
             So on orientation change we will loose data.
              It is a consumer. Consumer must launch in a coroutine */
            lifecycleScope.launch {
                mViewModel.triggerFlow().collectLatest {
                    tvFLow.text = it
                }
            }
        }
        btnSharedFlow.setOnClickListener {
            mViewModel.triggerSharedFlow()
        }
        subscribeToObserveLiveData()
    }

    private fun subscribeToObserveLiveData() {
        //In case of fragment viewLifeCycleOwner
        mViewModel.liveData.observe(this) {
            tvLiveData.text = it
        }
        lifecycleScope.launchWhenStarted {
            mViewModel.stateFlow.collectLatest {
                tvStateFLow.text = it
                //Snackbar.make(parent, it, Snackbar.LENGTH_LONG).show()
            }
        }
        lifecycleScope.launchWhenStarted {
            mViewModel.sharedFlow.collectLatest {
                Snackbar.make(parent, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Login", "OnDestroy")
    }
}
