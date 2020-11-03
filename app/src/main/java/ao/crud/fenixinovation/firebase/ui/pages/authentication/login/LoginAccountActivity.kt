package ao.crud.fenixinovation.firebase.ui.pages.authentication.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import ao.crud.fenixinovation.firebase.R
import ao.crud.fenixinovation.firebase.ui.pages.MainActivity
import ao.crud.fenixinovation.firebase.ui.pages.authentication.register.RegisterAccountActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginAccountActivity : AppCompatActivity() {

    private lateinit var edtEmail: AppCompatEditText
    private lateinit var edtPassword: AppCompatEditText
    private lateinit var inputEmail: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var btnLogin: AppCompatButton
    private lateinit var textRegisterAccount: AppCompatTextView

    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_account)

        auth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)
        btnLogin = findViewById(R.id.btnLogin)
        textRegisterAccount = findViewById(R.id.textRegisterAccount)

        btnLogin.setOnClickListener {
            when {
                edtEmail.text.toString().isEmpty() -> {
                    inputEmail.error = "Preencha o Email"
                }
                edtPassword.text.toString().isEmpty() -> {
                    inputPassword.error = "Preencha a password"
                }
                else -> {
                    loginAccount(edtEmail.text.toString(), edtPassword.text.toString())
                }
            }
        }

        textRegisterAccount.setOnClickListener {
            startActivity(Intent(this, RegisterAccountActivity::class.java))
            finish()
        }
    }

    private fun loginAccount(email: String, password: String) {
        showAlert()
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener { successResponse ->
            if (successResponse.user != null) {
                hideAlert()
                Toast.makeText(this, "Sucessos ao aceder conta.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

        }.addOnFailureListener {
            hideAlert()
            Toast.makeText(this, "Erro ao aceder conta.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAlert() {
        progressDialog.setTitle("Por favor aguarde...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }

    private fun hideAlert() {
        progressDialog.hide()
    }
}