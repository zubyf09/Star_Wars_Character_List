package com.trivago.challenge.characters.dh

import com.trivago.challenge.characters.networking.*
import com.trivago.challenge.characters.viewmodel.CharacterDetailsVM
import com.trivago.challenge.characters.viewmodel.CharacterSearchVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

/*Dependency holder for Character module*/
object CharacterDH {

    fun init() {
        loadKoinModules(
            characterDetailsModule(),
            characterSearchModule(),
            characterModule()
        )
    }

    //Details module
    private fun characterDetailsModule(): Module = module {
        viewModel { CharacterDetailsVM(get()) }
        single {
            characterDetailsContract(
                get()
            )
        }
    }

    private fun characterDetailsContract(service: CharacterService)
            : CharacterDetailsContract.Repo =
        CharacterDetailsRepo(service)

    //Search module
    private fun characterSearchModule(): Module = module {
        viewModel { CharacterSearchVM(get()) }
        single {
            characterSearchContract(
                get()
            )
        }
    }

    private fun characterSearchContract(service: CharacterService)
            : CharacterSearchContract.Repo =
        CharacterSearchRepo(service)


    //Character module
    private fun characterModule(): Module = module {
        single { characterService(get()) }
    }

    private fun characterService(retrofit: Retrofit): CharacterService = retrofit.create()

}