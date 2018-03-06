package com.dimanem.android.nearbyplaces.repository.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.dimanem.android.nearbyplaces.entities.Resource
import com.dimanem.android.nearbyplaces.repository.api.ApiResponse
import java.util.*

/**
 * Created by dimanemets on 11/02/2018.
 */
abstract class NetworkBoundResource<ResultType, RequestType>() {

    private var appExecutors: AppExecutors? = null
    private var result = MediatorLiveData<Resource<ResultType>>()

    @MainThread
    constructor(appExecutors: AppExecutors) : this() {
        this.appExecutors = appExecutors
        result.value = Resource.loading(null)
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData -> setValue(Resource.success(newData)) }
            }
        }
    }

    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (!Objects.equals(result.value, newValue)) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData -> setValue(Resource.loading(newData)) }
        result.addSource<ApiResponse<RequestType>>(apiResponse) { response ->
            result.removeSource<ApiResponse<RequestType>>(apiResponse)
            result.removeSource(dbSource)

            if (response != null && response.isSuccessful) {
                appExecutors?.diskIO?.execute({
                    saveCallResult(processResponse(response))
                    appExecutors?.mainThread?.execute({
                        // we specially request a new live data,
                        // otherwise we will get immediately last cached value,
                        // which may not be updated with latest results received from network.
                        result.addSource(loadFromDb()) {
                            newData -> setValue(Resource.success(newData))
                        }
                    })
                })
            } else {
                onFetchFailed()
                result.addSource(dbSource) { newData ->
                    setValue(Resource.error(response?.errorMessage, newData))
                }
            }
        }
    }

    protected fun onFetchFailed() {}

    @WorkerThread
    protected fun processResponse(response: ApiResponse<RequestType>?): RequestType? {
        return response?.body
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType?)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}
