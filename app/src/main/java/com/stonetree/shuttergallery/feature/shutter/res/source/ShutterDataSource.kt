package com.stonetree.shuttergallery.feature.shutter.res.source

import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.*
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.stonetree.corerepository.core.extensions.enqueue
import com.stonetree.corerepository.core.model.NetworkState
import com.stonetree.corerepository.core.model.NetworkState.Companion.LOADED
import com.stonetree.corerepository.core.model.NetworkState.Companion.LOADING
import com.stonetree.shuttergallery.feature.shutter.model.Image
import com.stonetree.shuttergallery.feature.shutter.model.ShutterModel
import com.stonetree.shuttergallery.feature.shutter.res.repository.ShutterRepository
import retrofit2.Call
import retrofit2.Response

class ShutterDataSource: PageKeyedDataSource<Long, Image>() {

    private val network = MutableLiveData<NetworkState>()

    private val repository: ShutterRepository = ShutterRepository.getInstance()

    fun getNetwork(): MutableLiveData<NetworkState> {
        return network
    }

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, Image>
    ) {
        network.postValue(LOADING)
        val request: Call<ShutterModel> = repository.api.get(1, params.requestedLoadSize)
        request.enqueue {
            onResponse = { response ->
                response.body()?.data?.let { images ->
                    callback.onResult(images, null, 2L)
                    network.postValue(LOADED)
                }
            }

            onFailure = { error ->
                network.postValue(NetworkState.error(error?.let { it.message }))
            }
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Image>) {
        val request: Call<ShutterModel> = repository.api.get(params.key, params.requestedLoadSize)
        request.enqueue {
            onResponse = { response ->
                response.body()?.apply {
                    callback.onResult(data, getNextKey(this, params))
                }
            }

            onFailure = { error ->
                network.postValue(NetworkState.error(error?.let { it.message }))
            }
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Image>) {

    }

    @VisibleForTesting(otherwise = PRIVATE)
    fun getNextKey(
        model: ShutterModel,
        params: LoadParams<Long>
    ): Long? {
        return if(model.totalCount == params.key)
                null
            else
                params.key + 1
    }
}
