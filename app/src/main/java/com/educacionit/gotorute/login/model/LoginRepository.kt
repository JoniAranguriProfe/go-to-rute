package com.educacionit.gotorute.login.model

import com.educacionit.gotorute.contract.LoginContract

class LoginRepository : LoginContract.LoginModel {
    override fun loginWithEmailAndPassword(email: String, password: String): Boolean {
        // To be completed
        return false
    }

    override fun loginWithProvider(provider: String) {
        // To be completed
    }
}