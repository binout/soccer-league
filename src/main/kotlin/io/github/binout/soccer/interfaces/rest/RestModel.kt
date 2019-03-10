package io.github.binout.soccer.interfaces.rest

import java.net.URI
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.ArrayList

open class RestModel {

    private val links: MutableList<RestLink>

    init {
        links = ArrayList()
    }

    fun getLinks(): List<RestLink> {
        return links
    }

    fun addLinks(link: RestLink) {
        this.links.add(link)
    }
}

class RestLink internal constructor() {

    var rel: String? = null
    var href: String? = null

    constructor(rel: String, href: String) : this() {
        this.rel = rel
        this.href = href
    }

    constructor(href: String) : this("self", href) {}

    constructor(uri: URI) : this("self", uri.toString()) {}
}

class RestDate(date: String) {

    private val year: Int
    private val month: Month
    private val day: Int

    init {
        val accessor = DateTimeFormatter.ISO_LOCAL_DATE.parse(date)
        year = accessor.get(ChronoField.YEAR)
        month = Month.of(accessor.get(ChronoField.MONTH_OF_YEAR))
        day = accessor.get(ChronoField.DAY_OF_MONTH)
    }

    fun year(): Int {
        return year
    }

    fun month(): Month {
        return month
    }

    fun day(): Int {
        return day
    }

    fun asLocalDate(): LocalDate {
        return LocalDate.of(year(), month(), day())
    }
}

class RestMatch internal constructor() : RestModel() {

    var date: String? = null
    private val players: MutableList<String>
    private val subs: MutableList<String>

    init {
        players = ArrayList()
        subs = ArrayList()
    }

    constructor(date: LocalDate) : this() {
        this.date = DateTimeFormatter.ISO_DATE.format(date)
    }

    fun getPlayers(): List<String> {
        return players
    }

    fun addPlayer(player: String) {
        this.players.add(player)
    }

    fun getSubs(): List<String> {
        return subs
    }

    fun addSub(player: String) {
        this.subs.add(player)
    }
}

class RestMatchDate internal constructor() : RestModel() {

    var date: String? = null
    private val presents: MutableList<String>
    var isCanBePlanned: Boolean = false

    init {
        presents = ArrayList()
    }

    constructor(date: LocalDate) : this() {
        this.date = DateTimeFormatter.ISO_DATE.format(date)
    }

    fun getPresents(): List<String> {
        return presents
    }

    fun addPresent(player: String) {
        this.presents.add(player)
    }
}

class RestPlayer internal constructor() : RestModel() {

    var name: String? = null
    var email: String? = null
    var isPlayerLeague: Boolean? = null
    var isGoalkeeper: Boolean? = null

    constructor(name: String) : this() {
        this.name = name
    }
}

class RestSeason internal constructor() : RestModel() {

    var name: String? = null

    constructor(name: String) : this() {
        this.name = name
    }
}

class RestStat internal constructor() {

    var player: String? = null
    var nbMatches: Int = 0
    var nbFriendlyMatches: Int = 0
    var nbLeagueMatches: Int = 0

    constructor(player: String) : this() {
        this.player = player
    }
}