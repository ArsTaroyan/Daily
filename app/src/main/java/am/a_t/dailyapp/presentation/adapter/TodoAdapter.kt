package am.a_t.dailyapp.presentation.adapter

import am.a_t.dailyapp.R
import am.a_t.dailyapp.databinding.DialogDeleteBinding
import am.a_t.dailyapp.databinding.DialogNewListBinding
import am.a_t.dailyapp.databinding.FragmentMainBinding
import am.a_t.dailyapp.databinding.ItemTodoBinding
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.presentation.ui.mainFragment.MainViewModel
import am.a_t.dailyapp.utils.ListColor
import android.app.AlertDialog
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(private val viewModel: MainViewModel, private val click: (Todo) -> Unit) :
    ListAdapter<Todo, TodoAdapter.MyViewHolder>(DiffUtilItemCallBack()) {

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

    inner class MyViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            with(binding) {
                if (!todo.todoIsChecked) {
                    tvTitle.text = todo.todoTitle
                    tvDate.text = todo.todoDate
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
                        view.setBackgroundResource(R.drawable.btn_red)
                    }
                    ListColor.BLUE -> {
                        view.setBackgroundResource(R.drawable.btn_blue)
                    }
                    ListColor.ORANGE -> {
                        view.setBackgroundResource(R.drawable.btn_orange)
                    }
                    ListColor.PURPLE -> {
                        view.setBackgroundResource(R.drawable.btn_purple)
                    }
                }
                isDelete.isChecked = todo.todoIsChecked
                btnDelete.setOnClickListener {
                    //initDialog()
                }
                isDelete.setOnClickListener {
                       viewModel.updateTodo(todo.copy(todoIsChecked = isDelete.isChecked))
                }
            }
        }
    }

    private fun initDialog(inflater: LayoutInflater, container: ViewGroup?) {
        myDialog = DialogDeleteBinding.inflate(inflater, container, false)
       // alertDialog = AlertDialog.Builder(requireContext())
//            .setView(myDialog.root)
//            .show()

        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        myDialog.btnYesTodo

            alertDialog.dismiss()

    }

    class DiffUtilItemCallBack : DiffUtil.ItemCallback<Todo>() {

        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean =
            oldItem == newItem

    }
}