package com.example.videoclub

import com.google.gson.annotations.SerializedName

data class DiscoverMovies(
    @SerializedName("results")val results: List<Result>
)