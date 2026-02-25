package com.example.photosapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photosapp.data.model.Photo
import com.example.photosapp.data.repository.FlickrRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PhotoUiState(
    val photos: List<Photo> = emptyList(),
    val isLoading: Boolean = false,
    val isPaginating: Boolean = false,
    val errorMessage: String? = null,
    val currentPage: Int = 1,
    val canPaginate: Boolean = true,
    val currentQuery: String = ""
)

@HiltViewModel
class PhotoViewModel @Inject constructor(private val repository: FlickrRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PhotoUiState())
    val uiState: StateFlow<PhotoUiState> = _uiState.asStateFlow()

    init {
        searchPhotos("")
    }

    fun searchPhotos(query: String) {
        _uiState.update {
            it.copy(
                photos = emptyList(),
                currentPage = 1,
                canPaginate = true,
                currentQuery = query,
                errorMessage = null
            )
        }
        getPhotos()
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.isLoading || state.isPaginating || !state.canPaginate) return
        _uiState.update { it.copy(currentPage = it.currentPage + 1) }
        getPhotos()
    }

    private fun getPhotos() {
        val state = _uiState.value
        val isFirstPage = state.currentPage == 1

        _uiState.update {
            if (isFirstPage) it.copy(isLoading = true)
            else it.copy(isPaginating = true)
        }

        viewModelScope.launch {
            val result = repository.getPhotos(
                query = state.currentQuery,
                page = state.currentPage
            )

            result.fold(
                onSuccess = { newPhotos ->
                    _uiState.update {
                        it.copy(
                            photos = it.photos + newPhotos,
                            isLoading = false,
                            isPaginating = false,
                            canPaginate = newPhotos.size >= 20,
                            errorMessage = null
                        )
                    }

                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isPaginating = false,
                            errorMessage = error.message ?: "Something went wrong"
                        )
                    }

                }
            )
        }
    }
}