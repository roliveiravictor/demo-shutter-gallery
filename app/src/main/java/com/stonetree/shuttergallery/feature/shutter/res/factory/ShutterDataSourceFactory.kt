package com.stonetree.shuttergallery.feature.shutter.res.factory

import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.*
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.stonetree.shuttergallery.feature.shutter.model.Image
import com.stonetree.shuttergallery.feature.shutter.res.source.ShutterDataSource

class ShutterDataSourceFactory
    : DataSource.Factory<Long, Image>()
{
    val data = MutableLiveData<ShutterDataSource>()

    @VisibleForTesting(otherwise = PRIVATE)
    var source: ShutterDataSource? = null

    override fun create(): DataSource<Long, Image> {
        source = ShutterDataSource()
        data.postValue(source)
        return source as ShutterDataSource
    }
}