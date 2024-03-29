package com.stonetree.corerepository.feature.idling

import androidx.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicInteger

class CoreRepositoryIdlingImp(private val resourceName: String) : IdlingResource {

    private val counter = AtomicInteger(0)

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String = resourceName

    override fun isIdleNow(): Boolean = counter.get() == 0

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        resourceCallback = callback
    }

    fun increment() = counter.getAndIncrement()

    @Throws(IllegalArgumentException::class)
    fun decrement() {
        val counterVal = counter.decrementAndGet()

        if (counterVal == 0) {
            resourceCallback?.onTransitionToIdle()
        }

        if (counterVal < 0) {
            throw IllegalArgumentException("Counter has been corrupted!")
        }
    }

    fun clear() {
        counter.set(0)
    }
}
