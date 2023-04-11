package com.example.retrofitapp.presentation.episode

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitapp.domain.model.episode.ResultsEpisode
import com.example.retrofitapp.data.repository.ApiResult
import com.example.retrofitapp.data.repository.RickAndMortyRepository

class EpisodeViewModel : ViewModel() {

    private var currentPage = 1
    private val rickAndMortyRepository = RickAndMortyRepository

    private val _episodesMutableLiveData = MutableLiveData<List<ResultsEpisode>>()
    val episodesMutableLiveData: LiveData<List<ResultsEpisode>> = _episodesMutableLiveData
    private var listOfItem = mutableListOf<ResultsEpisode>()

    init {
        getAllEpisodes()
    }

    fun getAllEpisodes() {
        rickAndMortyRepository.getAllEpisodes(currentPage) { result ->
            when (result) {
                is ApiResult.Success -> {
                    listOfItem = listOfItem.toMutableList()
                    listOfItem.addAll(result.value.results)
                    _episodesMutableLiveData.value = listOfItem
                    currentPage++
                }
                is ApiResult.Error -> {
                    val errorMessage = result.message
                    val throwable = result.throwable
                    Log.e(
                        "EpisodeViewModel",
                        "Error getting all episodes: $errorMessage",
                        throwable
                    )
                }
            }
        }
    }
}