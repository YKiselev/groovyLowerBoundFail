package com.github.ykiselev

import groovy.transform.TypeChecked
import org.junit.Test

@TypeChecked
class ChainTest {

    static class A<I, O> {

        def <C extends A<? super O, ?>> C andThen(C next) {
            return next
        }

    }

    static <I, O, C extends A<? super O, ?>> C chain(A<I, O> self, C next) {
        self.andThen(next)
        return next
    }

    @Test
    void "should chain"() {
        def a1 = new A<String, Integer>()
        def a2 = new A<Integer, Double>()
        def a3 = new A<Double, String>()
        def a4 = new A<String, Double>()
        def a5 = new A<Number, Object>()

        a1.andThen(a2) // ok
        a2.andThen(a3) // ok
        a3.andThen(a4) // ok
        a4.andThen(a5) // ok (even without "? super O")

        chain(a1, a2).andThen(a3) // ok
        chain(chain(chain(chain(a1, a2), a3), a4), a5) // ok

        a1.andThen(a2)
                .andThen(a3) // static type checker error
                .andThen(a4)
                .andThen(a5)
    }
}