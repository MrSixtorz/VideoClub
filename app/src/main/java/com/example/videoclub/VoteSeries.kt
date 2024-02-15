package com.example.videoclub

import com.google.gson.annotations.SerializedName

data class VoteSeries(
    @SerializedName("results") val SeriesVotos: List<SeriesVotos>,
)