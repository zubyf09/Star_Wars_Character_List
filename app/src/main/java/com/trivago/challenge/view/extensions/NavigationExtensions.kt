package com.trivago.challenge.view.extensions

import android.content.Intent

private const val PACKAGE_NAME = "com.trivago.challenge"

fun intentTo(navigatableActivity: NavigatableActivity): Intent {
    return Intent(Intent.ACTION_VIEW).setClassName(
        PACKAGE_NAME,
        navigatableActivity.className)
}

interface NavigatableActivity {
    val className: String
}

object Activities {

    object Characters : NavigatableActivity {
//        override val className = "$PACKAGE_NAME.characters.view.CharacterActivity"
        override val className = "com.trivago.challenge.characters.view.CharacterActivity"
    }

}