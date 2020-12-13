package edu.bth.ma.passthebomb.client.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class ObserveExtensions {
    companion object {
        public fun <T> LiveData<T>.observeOnce(observer: (T) -> Unit) {
            observeForever(object: Observer<T> {
                override fun onChanged(value: T) {
                    removeObserver(this)
                    observer(value)
                }
            })
        }

        public fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
            observe(owner, object: Observer<T> {
                override fun onChanged(value: T) {
                    removeObserver(this)
                    observer(value)
                }
            })
        }
    }

}