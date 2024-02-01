package com.makogon.alina.pinterest_pexels.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makogon.alina.pinterest_pexels.photoList.domain.repository.PhotoListRepository
import com.makogon.alina.pinterest_pexels.photoList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val photoListRepository: PhotoListRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val photoId = savedStateHandle.get<Int>("photoId")

    private var _detialsState = MutableStateFlow(DetailsState())
    val detailsState = _detialsState.asStateFlow()

    init {
        getPhoto(photoId ?: -1)
    }

    private fun getPhoto(id: Int) {
        viewModelScope.launch {
            _detialsState.update {
                it.copy(isLoading = true)
            }

            photoListRepository.getPhoto(id).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _detialsState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _detialsState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { photo ->
                            _detialsState.update {
                                it.copy(photo = photo)
                            }
                        }
                    }
                }
            }
        }
    }
}