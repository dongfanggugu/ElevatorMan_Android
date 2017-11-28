package com.honyum.elevatorMan.mvp

/**
 * Created by Star on 2017/11/24.
 */
interface BaseView<in T> {

    fun setPresenter(presenter: T)

}