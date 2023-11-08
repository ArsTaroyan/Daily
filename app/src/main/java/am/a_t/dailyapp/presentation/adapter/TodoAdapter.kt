package am.a_t.dailyapp.presentation.adapter

import am.a_t.dailyapp.R
import am.a_t.dailyapp.databinding.DialogDeleteBinding
import am.a_t.dailyapp.databinding.ItemTodoBinding
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.domain.utils.ListColor
import am.a_t.dailyapp.presentation.ui.todoFragment.TodoViewModel
import android.app.AlertDialog
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(
    private val context: Context,
    private var inflater: LayoutInflater,
    private var container: ViewGroup?,
    private val viewModel: TodoViewModel,
    private val click: (Boolean, Todo?) -> Unit,
) :
    ListAdapter<Todo, TodoAdapter.MyViewHolder>(DiffUtilItemCallBackTodo()) {

    private lateinit var myDialog: DialogDeleteBinding
    private lateinit var alertDialog: AlertDialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemTodoBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MyViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: Todo) {

            with(binding) {

                tvTitle.isSelected = true
                tvDate.text = todo.todoDate
                btnList.visibility = View.GONE

                if (!todo.todoIsChecked) {
                    tvTitle.text = todo.todoTitle
                } else {
                    val spannableString = SpannableString(todo.todoTitle)
                    val strikethroughSpan = StrikethroughSpan()
                    spannableString.setSpan(
                        strikethroughSpan,
                        0,
                        todo.todoTitle.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    tvTitle.text = spannableString
                }

                when (todo.todoColor) {
                    ListColor.RED -> {
                        viewTodo.setBackgroundResource(R.drawable.btn_red)
                    }
                    ListColor.BLUE -> {
                        viewTodo.setBackgroundResource(R.drawable.btn_blue)
                    }
                    ListColor.ORANGE -> {
                        viewTodo.setBackgroundResource(R.drawable.btn_orange)
                    }
                    ListColor.PURPLE -> {
                        viewTodo.setBackgroundResource(R.drawable.btn_purple)
                    }
                    else -> {
                        viewTodo.setBackgroundResource(R.drawable.btn_red)
                    }
                }

                btnIsChecked.isChecked = todo.todoIsChecked

                btnDelete.setOnClickListener {
                    removeDialog(inflater, container, todo)
                }

                btnIsChecked.setOnClickListener {
                    viewModel.updateTodo(todo.copy(todoIsChecked = btnIsChecked.isChecked))
                }

                btnEdit.setOnClickListener {
                    click(false, todo)
                }

            }
        }
    }

    private fun removeDialog(inflater: LayoutInflater, container: ViewGroup?, todo: Todo) {

        myDialog = DialogDeleteBinding.inflate(inflater, container, false)

        with(myDialog) {

            alertDialog = AlertDialog.Builder(context)
                .setView(root)
                .show()

            when (todo.todoColor) {
                ListColor.RED -> {
                    btnYesTodo.setBackgroundResource(R.drawable.btn_red)
                    btnNoTodo.setBackgroundResource(R.drawable.btn_red)
                }
                ListColor.BLUE -> {
                    btnYesTodo.setBackgroundResource(R.drawable.btn_blue)
                    btnNoTodo.setBackgroundResource(R.drawable.btn_blue)
                }
                ListColor.ORANGE -> {
                    btnYesTodo.setBackgroundResource(R.drawable.btn_orange)
                    btnNoTodo.setBackgroundResource(R.drawable.btn_orange)
                }
                ListColor.PURPLE -> {
                    btnYesTodo.setBackgroundResource(R.drawable.btn_purple)
                    btnNoTodo.setBackgroundResource(R.drawable.btn_purple)
                }
                else -> {
                    btnYesTodo.setBackgroundResource(R.drawable.btn_red)
                    btnNoTodo.setBackgroundResource(R.drawable.btn_red)
                }
            }

            btnYesTodo.setOnClickListener {
                viewModel.removeTodo(todo)
                click(true, null)
                alertDialog.dismiss()
            }

            btnNoTodo.setOnClickListener {
                alertDialog.dismiss()
            }

        }

        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}

class DiffUtilItemCallBackTodo : DiffUtil.ItemCallback<Todo>() {

    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean =
        oldItem == newItem

}