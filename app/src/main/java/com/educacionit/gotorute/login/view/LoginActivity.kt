package com.educacionit.gotorute.login.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.educacionit.gotorute.R
import com.educacionit.gotorute.contract.LoginContract
import com.educacionit.gotorute.home.view.HomeActivity
import com.educacionit.gotorute.login.model.LoginRepository
import com.educacionit.gotorute.login.presenter.LoginPresenter
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity(), LoginContract.LoginView {
    private lateinit var loginPresenter: LoginContract.ILoginPresenter<LoginContract.LoginView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        initPresenter()
        initViews()
    }

    private fun initViews() {
        val passwordInput = findViewById<TextInputLayout>(R.id.input_layout_password)
        val emailInput = findViewById<TextInputLayout>(R.id.input_layout_email)
        val signInButton = findViewById<AppCompatButton>(R.id.sign_in_button)
        signInButton.setOnClickListener {
            loginPresenter.loginWithEmailAndPassword(
                emailInput.editText?.text.toString(),
                passwordInput.editText?.text.toString()
            )
        }
    }

    override fun initPresenter() {
        val loginModel = LoginRepository()
        loginPresenter = LoginPresenter(loginModel)
        loginPresenter.attachView(this)
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun getViewContext(): Context {
        return this
    }

    override fun openMapsScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, 0)
        finish()
    }
}