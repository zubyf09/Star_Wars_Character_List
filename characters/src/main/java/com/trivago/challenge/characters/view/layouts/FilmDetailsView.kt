package com.trivago.challenge.characters.view.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.trivago.challenge.utils.isValid
import com.trivago.challenge.characters.R
import kotlinx.android.synthetic.main.view_character_film_details.view.*

/**
 * Custom view to display film details
 * */
class FilmDetailsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater
                .from(context)
                .inflate(R.layout.view_character_film_details, this, true)
    }

    fun filmDetails(details: com.trivago.challenge.characters.model.FilmDetailsModel) {
        if (details.title.isValid())
            tvFilmName.text = details.title

        if (details.releaseDate.isValid())
            tvFilmReleaseDate.text = String.format(context.getString(R.string.released_on), details.releaseDate)

        if (details.openingCrawl.isValid())
            tvFilmCrawl.text = details.openingCrawl
    }

}