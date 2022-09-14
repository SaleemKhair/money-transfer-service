package org.bsf.transfers.common


interface Validable {
    fun validate()
}

interface Persistable<T> {
    fun persist(): T
}