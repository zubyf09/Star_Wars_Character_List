package com.trivago.challenge.characters.view


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.trivago.challenge.characters.R
import com.trivago.challenge.characters.model.CharacterDetailsModel
import com.trivago.challenge.characters.model.CharacterSearchModel
import com.trivago.challenge.characters.view.layouts.FilmDetailsView
import com.trivago.challenge.characters.view.layouts.SpecieDetailsView
import com.trivago.challenge.characters.viewmodel.CharacterDetailsVM
import com.trivago.challenge.utils.isValid
import com.trivago.challenge.view.BaseFragment
import com.trivago.challenge.view.extensions.visible
import kotlinx.android.synthetic.main.actionbar_toolbar.*
import kotlinx.android.synthetic.main.fragment_character_details.*
import org.koin.android.ext.android.inject

class CharacterDetailsFragment : BaseFragment() {

    override val layout = R.layout.fragment_character_details

    override val viewModel: CharacterDetailsVM by inject()

    private var selectedCharacter: CharacterSearchModel? = null

    companion object {
        const val TAG = "CharacterDetailsFragment"
        const val CHARACTER = "character"
        fun newInstance(character: CharacterSearchModel) =
            CharacterDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CHARACTER, character)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(CHARACTER))
                selectedCharacter = it.getParcelable(CHARACTER)
        }

        if (selectedCharacter == null) {
            showToast(R.string.something_went_wrong)
            popBack()
            return
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar(toolbar, selectedCharacter?.name)

        //Disable swiperefreshlayout's swipe functionality
        srlDetails.isEnabled = false

        //Set the details passed from previous fragment
        tvName.text = selectedCharacter?.name
        tvYOB.text = selectedCharacter?.birthYear

        selectedCharacter?.url?.run {
            //Trigger character details load
            viewModel.getCharacterDetails(this)
                .observe(this@CharacterDetailsFragment, Observer { details ->
                    handleCharacterDetails(details)
                })
        }

    }

    /**
     * Set the character details to the UI
     * */
    private fun handleCharacterDetails(details: CharacterDetailsModel) {

        tvName.text = details.name

        tvYOB.text = details.birthYear

        if (details.heightCentimeters.isValid()) {
            tvHeightLabel.visible()
            tvHeight.visible()
            tvHeight.text = String.format(getString(R.string.cms), details.heightCentimeters)
        }

        if (details.heightFtInches != null) {
            tvHeightFeet.visible()
            tvHeightFeet.text = String.format(
                getString(R.string.feet_inches),
                details.heightFtInches.first, details.heightFtInches.second
            )
        }

        details.specieDetails?.run {
            tvSpeciesLabel.visible()
            llSpeciesDetails.visible()
            llSpeciesDetails.removeAllViews()
            forEach {
                val specieDetailsView = SpecieDetailsView(parentActivity)
                specieDetailsView.specieDetails(it)
                llSpeciesDetails.addView(specieDetailsView)
            }
        }

        details.filmDetails?.run {
            tvFilmsLabel.visible()
            llFilms.visible()
            forEach {
                val filmDetailsView = FilmDetailsView(parentActivity)
                filmDetailsView.filmDetails(it)
                llFilms.addView(filmDetailsView)
            }
        }
    }

    override fun hideLoading() {
        srlDetails.isRefreshing = false
    }

    override fun showLoading() {
        srlDetails.isRefreshing = true
    }
}
