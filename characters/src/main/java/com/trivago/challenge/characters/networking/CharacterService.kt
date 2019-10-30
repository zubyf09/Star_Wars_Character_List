package com.trivago.challenge.characters.networking

import com.trivago.challenge.networking.RemoteResponse
import com.trivago.challenge.characters.model.CharacterSearchModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface CharacterService {

    @GET
    fun getCharacters(@Url url: String): Single<RemoteResponse<List<CharacterSearchModel>>>

    @GET("people")
    fun searchCharacter(@Query("search") query: String): Single<RemoteResponse<List<CharacterSearchModel>>>

    @GET
    fun getCharacterDetails(@Url url: String): Single<com.trivago.challenge.characters.model.CharacterDetailsModel>

    @GET
    fun getCharacterSpecies(@Url url: String?): Single<com.trivago.challenge.characters.model.SpeciesResponseModel>

    @GET
    fun getCharacterHomeworld(@Url url: String?): Single<com.trivago.challenge.characters.model.HomeworldResponseModel?>

    @GET
    fun getCharacterFilms(@Url url: String?): Single<com.trivago.challenge.characters.model.FilmResponseModel>

}
