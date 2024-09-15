package de.awolf.trip.kmp.di

import de.awolf.trip.kmp.departures.di.departuresModule
import de.awolf.trip.kmp.core.di.coreModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(databaseModule, httpClientModule, coreModule) // core
        modules(departuresModule) // features
    }
}