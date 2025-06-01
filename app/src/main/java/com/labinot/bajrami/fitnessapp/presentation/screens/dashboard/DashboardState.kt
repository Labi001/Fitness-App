package com.labinot.bajrami.fitnessapp.presentation.screens.dashboard

import com.labinot.bajrami.fitnessapp.domain.model.BodyPart
import com.labinot.bajrami.fitnessapp.domain.model.User

data class DashboardState(
    val user: User? = null,
    val bodyParts: List<BodyPart> = emptyList(),
    val isSignOutButtonLoading: Boolean = false,
    val isSignInButtonLoading: Boolean = false,
)