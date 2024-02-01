package com.makogon.alina.pinterest_pexels.photoList.presentation

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
class PhotoListViewModel @Inject constructor(
    private val photoListRepository: PhotoListRepository,
    private val bookmarkListRepository: BookmarkListRepository
) : ViewModel() {
    private var _photoListState = MutableStateFlow(PhotoListState())
    val photoListState = _photoListState.asStateFlow()

    init {
        getPhotoList(false)
        getBookmarksList(false)
    }

    fun onEvent(event: PhotoListEvent) {
        when (event) {
            PhotoListEvent.Navigate -> {
                _photoListState.update {
                    it.copy(isCurrentPhotoScreen = !photoListState.value.isCurrentPhotoScreen)
                }
            }

            is PhotoListEvent.Paginate -> {
                if (event.category == Category.PHOTO) {
                    getPhotoList(true)
                } else if (event.category == Category.BOOKMARKED) {
                    getBookmarksList(true)
                }
            }
        }
    }

    private fun getPhotoList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _photoListState.update {
                it.copy(isLoading = true)
            }

            photoListRepository.getPhotoList(
                forceFetchFromRemote,
                photoListState.value.photoListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _photoListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _photoListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { photoList ->
                            _photoListState.update {
                                it.copy(
                                    photoList = photoListState.value.photoList + photoList.shuffled(),
                                    photoListPage = photoListState.value.photoListPage + 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getBookmarksList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _photoListState.update {
                it.copy(isLoading = true)
            }

            bookmarkListRepository.getBookmarksList().collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _photoListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _photoListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { bookmarksList ->
                            _photoListState.update {
                                it.copy(
                                    bookmarksList = photoListState.value.bookmarksList + bookmarksList.shuffled(),
                                    bookmarksListPage = photoListState.value.bookmarksListPage + 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


object Category {
    const val PHOTO = "photo"
    const val BOOKMARKED = "bookmarks"
}