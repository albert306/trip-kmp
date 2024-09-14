package de.awolf.trip.kmp.di

import de.awolf.trip.kmp.departures.di.departuresModule
import de.awolf.trip.kmp.search.di.searchModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(databaseModule, httpClientModule) // core
        modules(departuresModule, searchModule) // features
    }
}