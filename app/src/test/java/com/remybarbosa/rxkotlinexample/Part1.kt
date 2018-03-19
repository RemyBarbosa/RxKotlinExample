package com.remybarbosa.rxkotlinexample

import io.reactivex.Observable
import org.junit.Test

/////////////
/// Part 1. : My first simple observables
/// Just, from, empty, error.

class Part1 {

    @Test
    fun just_oneNext() {
        Observable.just("hi !")
                .subscribe { event ->
                    print(event)
                }
    }

    @Test
    fun from_multipleNext() {
        Observable.fromArray("a little beer ?", "why just one ?", "GOOOOOOOO")
                .subscribe { event ->
                        println(event)
                }
    }

    @Test
    fun empty_Complete() {
        Observable.empty<String>()
                .subscribe { event ->
                        print(event)
                }
    }

    @Test
    fun error_Error() {
        Observable.error<Int>(Exception("Oups"))
                .subscribe { event ->
                        print(event)
                }
    }
}
