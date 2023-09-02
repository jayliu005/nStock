package com.example.nstock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.nstock.repo.StockRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: StockRepo) : ViewModel() {

    private val TAG = "MainViewModel"

    fun getStock(stockId: Int) = repo.getStock(stockId)
        .catch {
            Timber.d(TAG, it.message.toString())
        }
        .asLiveData(viewModelScope.coroutineContext)
}