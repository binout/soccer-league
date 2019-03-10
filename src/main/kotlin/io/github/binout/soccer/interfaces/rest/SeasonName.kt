package io.github.binout.soccer.interfaces.rest

import io.github.binout.soccer.domain.season.Season

internal class SeasonName(param: String) {

    val name: String = when (param) {
        "current" -> Season.currentSeasonName()
        else -> param
    }

}
