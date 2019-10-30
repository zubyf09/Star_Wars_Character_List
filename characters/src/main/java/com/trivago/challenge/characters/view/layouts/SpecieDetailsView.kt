package com.trivago.challenge.characters.view.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.trivago.challenge.view.extensions.gone
import com.trivago.challenge.utils.isValid
import com.trivago.challenge.characters.R
import kotlinx.android.synthetic.main.view_character_species_details.view.*

/**
 * Custom view to display specie details like specie name, language,
 * homeworld and homeworld's population
 * */
class SpecieDetailsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater
                .from(context)
                .inflate(R.layout.view_character_species_details, this, true)
    }

    fun specieDetails(detailsModel: com.trivago.challenge.characters.model.SpeciesDetailsModel) {
        if (detailsModel.species.isValid())
            tvSpeciesName.text = detailsModel.species
        else tvSpeciesName.gone()

        if (detailsModel.language.isValid())
            tvSpeciesLanguage.text = String.format(context.getString(R.string.speak), detailsModel.language)
        else tvSpeciesLanguage.gone()

        if (detailsModel.homeworld.isValid())
            tvHomeworld.text = String.format(context.getString(R.string.live_in), detailsModel.homeworld)
        else tvHomeworld.gone()

        if (detailsModel.population.isValid())
            tvPopulation.text = String.format(context.getString(R.string.population), detailsModel.population)
        else tvPopulation.gone()
    }

}