package com.example.photosapp

import com.example.photosapp.data.model.Photo
import com.example.photosapp.data.repository.FlickrRepository
import com.example.photosapp.viewmodel.PhotoListViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoListViewModelTest {

    private lateinit var viewModel: PhotoListViewModel
    private lateinit var repository: FlickrRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        val mockPhotos = List(20) { index ->
            Photo(
                id = "$index",
                title = "TestPhoto $index",
                owner = "testOwner",
                secret = "abc123",
                server = "55555",
                farm = 66,
                isPublic = true,
                isFriend = false,
                isFamily = false,
                imageUrl = "https://live.staticflickr.com/55555/${index}_abc123.jpg"
            )
        }
        coEvery { repository.getPhotos(any(), any()) } returns Result.success(mockPhotos)
        viewModel = PhotoListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `query is updated correctly`() = runTest {
        viewModel.searchPhotos("tacos")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("tacos", viewModel.uiState.value.currentQuery)
    }

    @Test
    fun `error message is shown on failure`() = runTest {
        coEvery { repository.getPhotos(any(), any()) } returns
                Result.failure(Exception("No internet connection"))
        viewModel.searchPhotos("tacos")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("No internet connection", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `page increments on loadMore`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.loadMore()
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(2, viewModel.uiState.value.currentPage)
    }
}