package com.example.examples

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExampleLiveDataAndFlowViewModel : ViewModel() {

    //We have provided initial value here
    private val _liveData = MutableLiveData("Hello world!!")
    val liveData: LiveData<String> = _liveData

    //We have provided initial value here
    private val _stateFlow = MutableStateFlow("Hello state world!!!")
    val stateFlow = _stateFlow.asStateFlow()

    //Here we haven't provided initial value.
    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun triggerLiveData() {
        println("Hey I am demo viewmodel${System.currentTimeMillis()}")
        _liveData.value = "LiveData"
    }

    //Use state flow when you wanna save state. It will trigger on screen rotation
    // StateFlow is a hot flow. It means it will keep emitting values even if there is no collector.
    //Stateflow need to have a initial value. LiveData need not to have that. So advantage is it can't be null at anytime.
    //StateFlow will remain active when your app is in background not just like LiveData which just become inactive. But with
    //little bit of setup we can accomplish the same with StateFlow. The major advantage is we can use powerful flow operators.
    fun triggerStateFlow() {
        _stateFlow.value = "Stateflow"
    }

    //Use Flow when you want to emit value over the period of time
    //It is a producer
    fun triggerFlow(): Flow<String> {
        return flow<String> {
            repeat(5) {
                emit("this $it")
                delay(1000L)
            }
        }
    }
    /* Use shared flow when you want to send events to the UI
    As normal flow it doesn't keep value in itself. This is a hot flow.
    This is used to send one time event/value when a specific user action happened or when we received an api response.
     e.g showing a Toast/Snack bar */
    fun triggerSharedFlow() {
        viewModelScope.launch {
            _sharedFlow.emit("SharedFLow")
        }
    }
}