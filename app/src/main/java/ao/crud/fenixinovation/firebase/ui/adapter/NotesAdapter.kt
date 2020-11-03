package ao.crud.fenixinovation.firebase.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import ao.crud.fenixinovation.firebase.R
import ao.crud.fenixinovation.firebase.data.models.NotesModel

class NotesAdapter(
        var iNotes: INotes,
        var iDeleteNotes: IDeleteNotes
) : RecyclerView.Adapter<NotesAdapter.NotesHolder>() {

    private var dataNotes: MutableList<NotesModel> = mutableListOf()

    inner class NotesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var titleHolder: AppCompatTextView = itemView.findViewById(R.id.notesTitle)
        private var descriptionHolder: AppCompatTextView = itemView.findViewById(R.id.notesDescription)
        var btnDeleteHolder: AppCompatButton = itemView.findViewById(R.id.btnDeleteNote)

        fun bindView(notes: NotesModel) {
            titleHolder.text = notes.title
            descriptionHolder.text = notes.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(
                R.layout.item_notes, parent, false
        )
        return NotesHolder(inflater)
    }

    override fun onBindViewHolder(holder: NotesHolder, index: Int) {
        holder.setIsRecyclable(false)
        holder.bindView(dataNotes[index])
        holder.itemView.setOnClickListener {
            iNotes.onClick(dataNotes[index])
        }

        holder.btnDeleteHolder.setOnClickListener {
            iDeleteNotes.onClick(dataNotes, index)
        }
    }

    override fun getItemCount(): Int = dataNotes.count()

    fun addAll(data: MutableList<NotesModel>) {
        dataNotes = data
        notifyDataSetChanged()
    }

    fun removeItem(data: MutableList<NotesModel>, index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyDataSetChanged()
    }

    interface INotes {
        fun onClick(notes: NotesModel)
    }

    interface IDeleteNotes {
        fun onClick(data: MutableList<NotesModel>, index: Int)
    }


}