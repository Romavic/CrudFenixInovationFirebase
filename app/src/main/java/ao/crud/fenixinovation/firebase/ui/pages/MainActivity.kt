package ao.crud.fenixinovation.firebase.ui.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ao.crud.fenixinovation.firebase.R
import ao.crud.fenixinovation.firebase.data.models.NotesModel
import ao.crud.fenixinovation.firebase.ui.adapter.NotesAdapter
import ao.crud.fenixinovation.firebase.ui.pages.authentication.login.LoginAccountActivity
import ao.crud.fenixinovation.firebase.ui.pages.create.CreateNoteActivity
import ao.crud.fenixinovation.firebase.ui.pages.update.UpdateNoteActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerNotes: RecyclerView
    private lateinit var progressNotes: ProgressBar
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var firestore: FirebaseFirestore
    private var dataNotes: MutableList<NotesModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        fab = findViewById(R.id.fab)
        recyclerNotes = findViewById(R.id.recyclerNotes)
        progressNotes = findViewById(R.id.progressNotes)

        fab.setOnClickListener {
            startActivity(Intent(this, CreateNoteActivity::class.java))
        }

        recyclerNotes.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false
        )

        requestNotes(recyclerNotes, firestore)
    }

    private fun requestNotes(recyclerNotes: RecyclerView?, firestore: FirebaseFirestore) {
        notesAdapter = NotesAdapter(
                iNotes = object : NotesAdapter.INotes {
                    override fun onClick(notes: NotesModel) {
                        val intent = Intent(this@MainActivity, UpdateNoteActivity::class.java)
                        intent.putExtra("id", notes.id)
                        intent.putExtra("title", notes.title)
                        intent.putExtra("description", notes.description)
                        startActivity(intent, intent.extras)
                    }
                },
                iDeleteNotes = object : NotesAdapter.IDeleteNotes {
                    override fun onClick(data: MutableList<NotesModel>, index: Int) {
                        progressNotes.visibility = View.VISIBLE
                        firestore.collection("Notes").document(data[index].id.toString()).delete()
                                .addOnSuccessListener {
                                    progressNotes.visibility = View.GONE
                                    notesAdapter.removeItem(data, index)
                                    Toast.makeText(this@MainActivity, "Success to delete note.", Toast.LENGTH_SHORT).show()

                                }.addOnFailureListener {
                                    progressNotes.visibility = View.GONE
                                    Toast.makeText(this@MainActivity, "Error to delete note.", Toast.LENGTH_SHORT).show()
                                }

                    }
                }
        )
        firestore.collection("Notes").whereEqualTo("email", auth.currentUser!!.email)
                .get()
                .addOnSuccessListener { response ->
                    progressNotes.visibility = View.GONE
                    response.documents.forEach {
                        dataNotes.add(it.toObject(NotesModel::class.java)!!)
                        notesAdapter.addAll(dataNotes)
                    }
                }.addOnFailureListener {
                    progressNotes.visibility = View.GONE
                    Toast.makeText(this, "Error to process notes.", Toast.LENGTH_SHORT).show()
                }
        recyclerNotes?.adapter = notesAdapter
    }

    override fun onStart() {
        authenticationStatus()
        super.onStart()
    }

    private fun authenticationStatus() {
        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginAccountActivity::class.java))
        }
    }
}