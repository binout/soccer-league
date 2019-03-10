package io.github.binout.soccer.interfaces.rest

import io.github.binout.soccer.domain.season.Season

internal class SeasonName(param: String) {

    private val name: String

    init {
        if ("current" == param) {
            this.name = Season.currentSeasonName()
        } else {
            this.name = param
        }
    }

    fun name(): String {
        return name
    }
}
