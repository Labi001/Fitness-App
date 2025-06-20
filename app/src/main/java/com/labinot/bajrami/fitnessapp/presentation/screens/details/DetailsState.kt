package com.example.measuremate.presentation.details

import com.labinot.bajrami.fitnessapp.domain.model.BodyPart
import com.labinot.bajrami.fitnessapp.domain.model.BodyPartValue
import com.labinot.bajrami.fitnessapp.domain.model.TimeRange
import java.time.LocalDate

data class DetailsState(
    val bodyPart: BodyPart? = null,
    val textFieldValue: String = "",
    val recentlyDeletedBodyPartValue: BodyPartValue? = null,
    val date: LocalDate = LocalDate.now(),
    val timeRange: TimeRange = TimeRange.LAST7DAYS,
    val allBodyPartValues: List<BodyPartValue> = emptyList(),
    val graphBodyPartValues: List<BodyPartValue> = emptyList()
)
