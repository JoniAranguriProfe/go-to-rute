package com.educacionit.gotorute.contract

interface LoginContract {
    interface LoginView : BaseContract.IBaseView {
        fun openMapsScreen()
    }

    interface ILoginPresenter<T : BaseContract.IBaseView> : BaseContract.IBasePresenter<T> {
        fun loginWithEmailAndPassword(email: String, password: String)
        fun loginWithProvider(provider: String)
    }

    interface LoginModel {
        fun loginWithEmailAndPassword(email: String, password: String): Boolean
        fun loginWithProvider(provider: String)
    }
}