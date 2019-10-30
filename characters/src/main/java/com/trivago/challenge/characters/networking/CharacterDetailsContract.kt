package com.trivago.challenge.characters.networking

import com.trivago.challenge.characters.model.CharacterDetailsModel
import com.trivago.challenge.characters.model.FilmResponseModel
import com.trivago.challenge.characters.model.HomeworldResponseModel
import com.trivago.challenge.characters.model.SpeciesResponseModel
import io.reactivex.Single

interface CharacterDetailsContract {

    interface Repo {
        /**
         * Get character details [CharacterDetailsModel] from the passed url
         * */
        fun getCharacterDetails(url: String): Single<CharacterDetailsModel>

        /**
         * Get specie details [SpeciesResponseModel] from the passed url
         * */
        fun getSpecieDetails(url: String?): Single<SpeciesResponseModel>

        /**
         * Get film details [FilmResponseModel] from the passed url
         * */
        fun getFilmDetails(url: String?): Single<FilmResponseModel>

        /**
        * Get homeworld details [HomeworldResponseModel] from the passed url
        * */
        fun getHomeworldDetails(url: String?): Single< HomeworldResponseModel?>
    }

}