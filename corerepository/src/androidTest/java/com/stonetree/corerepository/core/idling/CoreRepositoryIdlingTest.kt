package com.stonetree.corerepository.core.idling

import com.stonetree.corerepository.feature.idling.CoreRepositoryIdling.getResource
import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Test

class CoreRepositoryIdlingTest {

    @Before
    fun setup() {
        getResource().clear()
    }

    @Test
    fun test_increment_shouldReturnIdle(){
        getResource().increment()
        assertFalse(getResource().isIdleNow)
    }

    @Test
    fun test_decrement_shouldReturnNotIdle(){
        getResource().increment()
        getResource().decrement()
        assertTrue(getResource().isIdleNow)
    }

    @Test
    fun test_name_shouldReturnGlobal(){
        assertEquals("GLOBAL",getResource().name)
    }

    @Test(expected = IllegalArgumentException::class)
    fun test_decrementWithoutIncrement_shouldReturnThrowException(){
        getResource().decrement()
    }
}