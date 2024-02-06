package com.makogon.alina.pinterest_pexels.photoList.presentation

import androidx.lifecycle.ViewModel
import com.makogon.alina.pinterest_pexels.photoList.presentation.PhotoList.PhotoListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(): ViewModel() {

    private var _sharedState = MutableStateFlow(SharedState())
    val sharedState = _sharedState.asStateFlow()

    fun onEvent(event: SharedEvent) {
        when (event) {
            SharedEvent.Navigate -> {
                _sharedState.update {
                    it.copy(isCurrentPhotoScreen = !sharedState.value.isCurrentPhotoScreen)
                }
            }
        }

    }

    sealed interface SharedEvent {
        object Navigate : SharedEvent
    }
    data class SharedState(val isCurrentPhotoScreen: Boolean = true)
}