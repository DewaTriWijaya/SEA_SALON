package com.dewa.sea.data.model

data class DataServices(
    val title: String,
    val imageService: String,
    val imageReferences: List<String> = emptyList(),
)