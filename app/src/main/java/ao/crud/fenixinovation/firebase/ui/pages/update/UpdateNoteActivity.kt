package ao.crud.fenixinovation.firebase.ui.pages.update

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import ao.crud.fenixinovation.firebase.R
import ao.crud.fenixinovation.firebase.ui.pages.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var edtTitle: AppCompatEditText
    private lateinit var edtDescription: AppCompatEditText
    private lateinit var btnRegisterNote: AppCompatButton

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_note)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        progressDialog = ProgressDialog(this)

        edtTitle = findViewById(R.id.edtTitle)
        edtDescription = findViewById(R.id.edtDescription)
        btnRegisterNote = findViewById(R.id.btnRegisterNote)

        val id = intent.getStringExtra("id")
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")

        edtTitle.setText(title)
        edtDescription.setText(description)

        btnRegisterNote.setOnClickListener {
            if (edtTitle.text.toString().isNotEmpty() && edtDescription.text.toString().isNotEmpty()) {
                updateNoteOnDatabase(
                        id!!,
                        edtTitle.text.toString(),
                        edtDescription.text.toString(),
                )
            } else {
                Toast.makeText(
                        this,
                        "Please fill the inputs texts.",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateNoteOnDatabase(id: String, title: String, description: String) {
        showAlert()
        val bodyHash: HashMap<String, Any> = hashMapOf()
        bodyHash["title"] = title
        bodyHash["description"] = description

        firestore.collection("Notes").document(
                id
        ).update(bodyHash).addOnSuccessListener {
            hideAlert()
            Toast.makeText(this, "Sucessos to edit note.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }.addOnFailureListener {
            hideAlert()
            Toast.makeText(this, "Error to edit note.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAlert() {
        progressDialog.setTitle("Por favor aguarde...")
        progressDialog.setMessage("Editando Nota...!...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }

    private fun hideAlert() {
        progressDialog.hide()
    }
}