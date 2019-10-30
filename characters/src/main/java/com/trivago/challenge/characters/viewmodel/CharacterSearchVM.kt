package com.trivago.challenge.characters.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trivago.challenge.characters.model.CharacterSearchModel
import com.trivago.challenge.characters.networking.CharacterSearchContract
import com.trivago.challenge.networking.RemoteResponse
import com.trivago.challenge.view.extensions.hide
import com.trivago.challenge.view.extensions.show
import com.trivago.challenge.viewmodel.BaseVM
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class CharacterSearchVM(private val repo: CharacterSearchContract.Repo) : BaseVM() {

    private val _characters = MutableLiveData<List<CharacterSearchModel>>()
    val characters: LiveData<List<CharacterSearchModel>> by lazy { _characters }

    private var nextPageUrl: String? = ""
    private var processing: Boolean = false

    private val _paginationLoading = MutableLiveData<Boolean>()
    val paginationLoading: LiveData<Boolean> by lazy { _paginationLoading }

    private val initialAPI = "people"

    fun initialLoad() {
        if (_characters.value != null && !_characters.value.isNullOrEmpty()) return

        _loading.show()
        getCharacters(url = initialAPI, resetItems = true)
    }

    private fun getCharacters(url: String, resetItems: Boolean) {
        if (processing) return

        processing = true
        handleCharactersObs(repo.characters(url), resetItems)
    }

    /**
     * Handle the characters returned from the API
     *
     * resetItems clears the current contents.
     * */
    private fun handleCharactersObs(
        charactersObs: Single<RemoteResponse<List<CharacterSearchModel>>>,
        resetItems: Boolean
    ) {
        charactersObs
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { response ->
                nextPageUrl = response.next
                return@map response.results
            }
            .map { searchModels ->
                appendOrSetResults(resetItems, _characters.value, searchModels)
            }
            .subscribe({
                _loading.hide()
                _paginationLoading.hide()
                _characters.postValue(it)
                processing = false
            }, {
                handleError(it)
                processing = false
            })
            .addTo(disposable)
    }

    private fun appendOrSetResults(
        resetItems: Boolean,
        existingData: List<CharacterSearchModel>?,
        newData: List<CharacterSearchModel>
    ): List<CharacterSearchModel> {
        val finalData = mutableListOf<CharacterSearchModel>()
        if (resetItems || existingData.isNullOrEmpty())
            finalData.addAll(newData)
        else {
            finalData.addAll(existingData)
            finalData.addAll(newData)
        }
        return finalData
    }

    fun loadNextPage() {
        nextPageUrl?.run {
            _paginationLoading.show()
            getCharacters(this, false)
        }
    }

    fun searchCharacter(query: String?) {
        if (query.isNullOrEmpty()) return

        _loading.show()

        when (query.toIntOrNull()) {

            // Filter by Name
            null -> handleCharactersObs(repo.searchCharacter(query), true)

            // Filter by Birth Year
            else -> filterCharactersByBirthYear(
                repo.characters(initialAPI),
                true,
                query + "BBY",
                mutableListOf()
            )

        }
    }

    private fun filterCharactersByBirthYear(
        charactersObs: Single<RemoteResponse<List<CharacterSearchModel>>>,
        resetItems: Boolean,
        birthYear: String?,
        existingData: List<CharacterSearchModel>?
    ) {
        charactersObs
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { response ->
                nextPageUrl = response.next
                return@map response.results
            }
            .map { it -> it.filter { it.birthYear.equals(birthYear, true) } }
            .map { searchModels ->
                appendOrSetResults(resetItems, existingData, searchModels)
            }
            .subscribe({

                // Before sending to UI, find Character in next pages as well
                if (nextPageUrl != null) {

//                    if (it.isNotEmpty())
//                        _characters.postValue(it)

                    nextPageUrl?.run {
                        filterCharactersByBirthYear( repo.characters(this), false, birthYear, it)
                    }

                } else {

                    // We've reached at end of data, so let's send this data to UI
                    _loading.hide()
                    _paginationLoading.hide()
                    _characters.postValue(it)
                    processing = false
                }

            }, {
                handleError(it)
                processing = false
            })
            .addTo(disposable)
    }

    fun refreshCharacters() {
        _loading.show()
        getCharacters(url = initialAPI, resetItems = true)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun setCharacter(characters: List<CharacterSearchModel>?) {
        _characters.postValue(characters)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun getInitialApi() = initialAPI

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun setNextPageUrl(url: String?) {
        nextPageUrl = url
    }
}

