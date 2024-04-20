package com.educacionit.gotorute.contract

interface FragmentBaseContract {
    interface IFragmentBaseView<T : BaseContract.IBaseView> {
        fun getParentView(): T?
        fun initPresenter()

    }

    interface IFragmentBasePresenter<T : IFragmentBaseView<*>> {
        fun attachView(view: T)
    }
}