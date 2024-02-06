package com.makogon.alina.pinterest_pexels.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makogon.alina.pinterest_pexels.photoList.domain.repository.BookmarkListRepository
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
    private val bookmarkPhotoListRepository: BookmarkListRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val photoId = savedStateHandle.get<Int>("photoId")

    private var _detailsState = MutableStateFlow(DetailsState())
    val detailsState = _detailsState.asStateFlow()

    init {
        getPhoto(photoId ?: -1)
        getBookmarkedPhoto(photoId ?: -1)
    }

    private fun getPhoto(photoId: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(isLoading = true)
            }

            photoListRepository.getPhoto(photoId).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _detailsState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _detailsState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { photo ->
                            _detailsState.update {
                                it.copy(photo = photo)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getBookmarkedPhoto(photoId: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(isLoading = true)
            }

            bookmarkPhotoListRepository.getBookmarkedPhoto(photoId).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _detailsState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _detailsState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { photo ->
                            _detailsState.update {
                                it.copy(photo = photo)
                            }
                        }
                    }
                }
            }
        }
    }
}