package com.educacionit.gotorute.login.presenter

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Patterns
import com.educacionit.gotorute.contract.BaseContract
import com.educacionit.gotorute.contract.LoginContract

class LoginPresenter(private val loginModel: LoginContract.LoginModel) :
    LoginContract.ILoginPresenter<LoginContract.LoginView> {
    private lateinit var loginView: LoginContract.LoginView
    override fun loginWithEmailAndPassword(email: String, password: String) {
        if (!isValidEmail(email)) {
            loginView.showErrorMessage("El formato del email no es valido")
            return
        }
        val loginResult = loginModel.loginWithEmailAndPassword(email, password)
        if (!loginResult) {
            loginView.showErrorMessage("No se pudo loguear, pero por ahora puede continuar")
        }
        saveSession()
        loginView.openMapsScreen()
    }

    private fun saveSession() {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(loginView.getViewContext())
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(FAKE_LOGIN_PREFERENCES_KEY, true)
        editor.apply()
    }

    private fun checkSession(context: Context?) = context?.let {
        PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(FAKE_LOGIN_PREFERENCES_KEY, false)
    } ?: false

    private fun isValidEmail(email: String): Boolean {
        return (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    override fun loginWithProvider(provider: String) {
        // To be completed
    }

    override fun attachView(loginView: LoginContract.LoginView) {
        this.loginView = loginView
        if (checkSession(loginView.getViewContext())) {
            this.loginView.openMapsScreen()
        }
    }

    companion object {
        const val FAKE_LOGIN_PREFERENCES_KEY = "FAKE_LOGIN_PREFERENCES_KEY"
    }
}