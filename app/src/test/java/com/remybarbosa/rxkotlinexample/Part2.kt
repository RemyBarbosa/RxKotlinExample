package com.remybarbosa.rxkotlinexample

import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.combineLatest
import io.reactivex.rxkotlin.zipWith
import org.junit.Test

/////////////
/// Part 2. : Operators
/// Map, filter, distinctUntilChanged, skip, skipUntil, takeWhile, zip, combineLatest and flatmap


class Part2 {

    @Test
    fun map() {
        Observable.just(1)
                .map { it -> "nombre : " + it }
                .subscribe { event ->
                    println(event)
                }
    }

    @Test
    fun filter() {
        val list = listOf(1, 2, 3, 3, 7, 7, 3, 9, 5, 5, 2, 9)
        Observable.fromIterable(list)
                .filter { it.rem(2) != 0 }
                .subscribe { event ->
                    println(event)
                }
    }

    @Test
    fun distinctUntilChanged() {
        val list = listOf(1, 2, 3, 3, 7, 7, 3, 9, 5, 5, 2, 9)
        Observable.fromIterable(list)
                .distinctUntilChanged()
                .subscribe { event ->
                    println(event)
                }
    }

    @Test
    fun skip() {
        val list = listOf(1, 2, 3, 3, 7, 7, 3, 9, 5, 5, 2, 9)
        Observable.fromIterable(list)
                .skip(3)
                .subscribe { event ->
                    println(event)
                }
    }

    @Test
    fun skipWhile() {
        val list = listOf(1, 2, 3, 3, 7, 7, 3, 9, 5, 5, 2, 9)
        Observable.fromIterable(list)
                .skipWhile { it < 5 }
                .subscribe { event ->
                    println(event)
                }
    }

    @Test
    fun takeWhile() {
        val list = listOf(1, 2, 3, 3, 7, 7, 3, 9, 5, 5, 2, 9)
        Observable.fromIterable(list)
                .takeWhile { it < 5 }
                .subscribe { event ->
                    println(event)
                }
    }

    @Test
    fun zip() {
        val list = listOf(1, 2, 3, 3, 7, 7, 3, 9, 5, 5, 2, 9)
        val list2 = listOf(10, 20, 20, 90)
        val observable1 = Observable.fromIterable(list)
        val observable2 = Observable.fromIterable(list2)

        Observables.zip(observable1, observable2)
                .subscribe { event ->
                    println(event)
                }
    }

    @Test
    fun combineLatest() {
        val list = listOf(1, 2, 3, 3, 7, 7, 3, 9, 5, 5, 2, 9)
        val list2 = listOf("10", "20", "20", "90")
        val observable1 = Observable.fromIterable(list)
        val observable2 = Observable.fromIterable(list2)

        Observables.combineLatest(observable1, observable2)
                .subscribe { event ->
                    println(event)
                }
    }
}
