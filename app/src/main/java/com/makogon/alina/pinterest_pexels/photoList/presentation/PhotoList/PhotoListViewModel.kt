package com.makogon.alina.pinterest_pexels.photoList.presentation.PhotoList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makogon.alina.pinterest_pexels.photoList.data.remote.PhotoApi.Companion.PAGE
import com.makogon.alina.pinterest_pexels.photoList.domain.repository.BookmarkListRepository
import com.makogon.alina.pinterest_pexels.photoList.domain.repository.PhotoListRepository
import com.makogon.alina.pinterest_pexels.photoList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        getFeaturedCollections(false)
        getCuratedPhotoList(false)
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
                    getCuratedPhotoList(true)
                    getFeaturedCollections(true)
                } else if (event.category == Category.BOOKMARKED) {
                    getBookmarksList(true)
                }
            }

            is PhotoListEvent.ChangeSearchText -> {
                viewModelScope.launch {
                    _photoListState.update {
                        it.copy(
                            searchText = event.searchText
                        )
                    }
                }
            }
            is PhotoListEvent.ChangeSelectedTitle -> {
                viewModelScope.launch {
                    _photoListState.update {
                        it.copy(
                            selectedTitle = event.selectedTitle
                        )
                    }
                }
            }
            is PhotoListEvent.ChangeIsSearchActive -> {
                viewModelScope.launch {
                    _photoListState.update {
                        it.copy(
                            isSearchActive = event.isSearchActive
                        )
                    }
                }
            }

            is PhotoListEvent.PerformPhotoSearch -> getPhotoList(true)
            PhotoListEvent.PerformBookmarkedPhotoSearch -> getBookmarksList(true)
            PhotoListEvent.PerformFCollectionsSearch -> getFeaturedCollections(true)
            PhotoListEvent.PerformCuratedPhotoSearch -> getCuratedPhotoList(true)
        }
    }

    private var shimmerJob: Job? = null

    private suspend fun launchShimmerEffect() {
        withContext(Dispatchers.Main) {
            _photoListState.update {
                it.copy(isLoading = true)
            }
            delay(7000)
            _photoListState.update {
                it.copy(isLoading = false)
            }
        }
    }

    private fun startShimmerEffect() {
        shimmerJob?.cancel() // for possible previous job

        shimmerJob = viewModelScope.launch {
            launchShimmerEffect()
        }
    }

    private fun stopShimmerEffect() {
        shimmerJob?.cancel() // for the job
        shimmerJob = null
    }



    private fun getPhotoList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _photoListState.update {
                it.copy(isLoading = true)
            }

            if (forceFetchFromRemote) {
                startShimmerEffect()
            }

            photoListRepository.getPhotoList(
                forceFetchFromRemote,
                photoListState.value.photoListPage,
                photoListState.value.searchText
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _photoListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        if (!forceFetchFromRemote) {
                            _photoListState.update {
                                it.copy(isLoading = result.isLoading)
                            }
                        }
                    }

                    is Resource.Success -> {
                        stopShimmerEffect()

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

    private fun getCuratedPhotoList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _photoListState.update {
                it.copy(isLoading = true)
            }

            photoListRepository.getCuratedPhotoList(PAGE).collectLatest { result ->
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
                                    photoList = /*photoListState.value.photoList +*/ photoList.shuffled()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun getFeaturedCollections(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _photoListState.update {
                it.copy(isLoading = true)
            }

            photoListRepository.getFeaturedCollections().collectLatest { result ->
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
                        result.data?.let { featuredCollections ->
                            _photoListState.update {
                                it.copy(
                                    featuredCollections = photoListState.value.featuredCollections + featuredCollections.shuffled()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getBookmarksList(forceFetch: Boolean) {
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