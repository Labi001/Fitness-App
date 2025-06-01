package com.example.measuremate.presentation.add_item

import com.labinot.bajrami.fitnessapp.domain.model.BodyPart


data class AddItemState(
    val textFieldValue: String = "",
    val selectedBodyPart: BodyPart? = null,
    val bodyParts: List<BodyPart> = emptyList()
)
