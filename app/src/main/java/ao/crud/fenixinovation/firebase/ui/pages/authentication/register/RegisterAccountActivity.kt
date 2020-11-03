package ao.crud.fenixinovation.firebase.ui.pages.authentication.register

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import ao.crud.fenixinovation.firebase.R
import ao.crud.fenixinovation.firebase.ui.pages.authentication.login.LoginAccountActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterAccountActivity : AppCompatActivity() {

    private lateinit var inputName: TextInputLayout
    private lateinit var inputEmail: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var edtName: AppCompatEditText
    private lateinit var edtEmail: AppCompatEditText
    private lateinit var edtPassword: AppCompatEditText
    private lateinit var btnRegisterAccount: AppCompatButton
    private lateinit var textLoginAccount: AppCompatTextView

    val auth = FirebaseAuth.getInstance()
    private lateinit var firestore: FirebaseFirestore

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_account)

        firestore = FirebaseFirestore.getInstance()
        progressDialog = ProgressDialog(this)

        inputName = findViewById(R.id.inputName)
        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)
        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnRegisterAccount = findViewById(R.id.btnRegisterAccount)
        textLoginAccount = findViewById(R.id.textLoginAccount)

        btnRegisterAccount.setOnClickListener {
            if (edtName.text.toString().isNotEmpty() && edtEmail.text.toString().isNotEmpty() && edtPassword.text.toString().isNotEmpty()) {
                registerAccount(
                        edtName.text.toString(),
                        edtEmail.text.toString(),
                        edtPassword.text.toString(),
                )
            } else {
                Toast.makeText(
                        this,
                        "Please fill the inputs texts.",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }

        textLoginAccount.setOnClickListener {
            startActivity(Intent(this, LoginAccountActivity::class.java))
            finish()
        }
    }

    private fun registerAccount(name: String, email: String, password: String) {
        showAlert()
        auth.createUserWithEmailAndPassword(
                email,
                password
        ).addOnCompleteListener {
            if (it.isComplete && it.isSuccessful) {
                val bodyHash: HashMap<String, Any> = hashMapOf()
                bodyHash["id"] = email
                bodyHash["email"] = email
                bodyHash["name"] = name
                bodyHash["password"] = password

                saveOnDatabase(bodyHash)
            }

        }.addOnFailureListener {
            hideAlert()
            Toast.makeText(
                    this,
                    "Error to create account.",
                    Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveOnDatabase(bodyHash: HashMap<String, Any>) {
        firestore.collection("Users").document("${bodyHash["id"]}").set(
                bodyHash
        ).addOnSuccessListener {
            hideAlert()
            Toast.makeText(
                    this,
                    "Success to save data of account",
                    Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, LoginAccountActivity::class.java))
            finish()

        }.addOnFailureListener {
            hideAlert()

            Toast.makeText(
                    this,
                    "Error to save data of account",
                    Toast.LENGTH_SHORT
            ).show()
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