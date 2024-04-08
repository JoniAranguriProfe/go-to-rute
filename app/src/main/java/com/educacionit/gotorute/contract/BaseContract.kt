package com.educacionit.gotorute.contract

import android.content.Context

interface BaseContract {
    interface IBaseView {
        fun showErrorMessage(message: String)

        fun getViewContext(): Context
    }

    interface IBasePresenter<T : IBaseView> {
        fun attachView(loginView: T)
    }

}