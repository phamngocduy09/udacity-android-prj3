package com.udacity.project4.utils

import com.firebase.ui.auth.AuthUI

val LoginFirebase = AuthUI
    .getInstance()
    .createSignInIntentBuilder()
    .setAvailableProviders(
        listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
    ).build()