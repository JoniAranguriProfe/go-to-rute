package com.educacionit.gotorute.contract

import android.content.Context

interface BaseContract {
    interface IBaseView {
        fun showErrorMessage(message: String)

        fun getViewContext(): Context

        fun initPresenter()

    }

    interface IBasePresenter<T : IBaseView> {
        fun attachView(view: T)
    }

}