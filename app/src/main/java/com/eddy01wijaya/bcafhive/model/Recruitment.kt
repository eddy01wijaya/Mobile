package com.eddy01wijaya.bcafhive.model

data class Recruitment(
    val idRecruitment: Int,
    val recruitmentName: String,
    val requirements: String,
    val openDate: String,
    val closeDate: String,
    var approval: String
)
