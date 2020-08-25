package com.lamine.realestatemanager

import com.lamine.realestatemanager.utils.Utils
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {
    @Test
    fun convertDollarToEuroTest() {
        assertEquals(8.0, Utils.convertDollarToEuro(10.0), 0.01)
    }


}