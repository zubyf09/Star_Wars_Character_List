package com.trivago.challenge.characters.networking

import com.trivago.challenge.networking.RemoteResponse
import com.trivago.challenge.characters.model.CharacterSearchModel
import io.reactivex.Single

class CharacterSearchRepo(private val service: CharacterService) :
    CharacterSearchContract.Repo {

    override fun characters(url: String)
            : Single<RemoteResponse<List<CharacterSearchModel>>> = service.getCharacters(url)

    override fun searchCharacter(query: String)
            : Single<RemoteResponse<List<CharacterSearchModel>>> = service.searchCharacter(query)

}