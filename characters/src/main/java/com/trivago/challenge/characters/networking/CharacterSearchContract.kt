package com.trivago.challenge.characters.networking

import com.trivago.challenge.networking.RemoteResponse
import com.trivago.challenge.characters.model.CharacterSearchModel
import io.reactivex.Single

interface CharacterSearchContract {
    interface Repo {

        /**
         * Get all the characters with pagination
         * */
        fun characters(url: String): Single<RemoteResponse<List<CharacterSearchModel>>>

        /**
         * Get searched characters with pagination
         * */
        fun searchCharacter(query: String): Single<RemoteResponse<List<CharacterSearchModel>>>
    }
}