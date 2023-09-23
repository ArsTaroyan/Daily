package am.a_t.dailyapp.presentation.adapter

import am.a_t.dailyapp.databinding.DialogDeleteBinding
import am.a_t.dailyapp.databinding.ItemTaskBinding
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.presentation.ui.mainFragment.MainViewModel
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val context: Context,
    private var inflater: LayoutInflater,
    private var container: ViewGroup?,
    private val viewModel: MainViewModel,
    private val click: (Task) -> Unit
) :
    ListAdapter<Task, TaskAdapter.MyViewHolder>(DiffUtilItemCallBack()) {

    private lateinit var myDialog: DialogDeleteBinding
    private lateinit var alertDialog: AlertDialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemTaskBinding
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

    inner class MyViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            with(binding) {
                tvTitle.text = task.taskTitle
                tvDate.text = task.taskDate
                tvTime.text = task.taskEndTime
                btnDelete.setOnClickListener {
                    removeDialog(inflater, container, task)
                }
            }
        }
    }

    private fun removeDialog(inflater: LayoutInflater, container: ViewGroup?, task: Task) {
        myDialog = DialogDeleteBinding.inflate(inflater, container, false)
        alertDialog = AlertDialog.Builder(context)
            .setView(myDialog.root)
            .show()

        myDialog.btnYesTodo.setOnClickListener {
            viewModel.removeTask(task)
            alertDialog.dismiss()
        }

        myDialog.btnNoTodo.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    }

    class DiffUtilItemCallBack : DiffUtil.ItemCallback<Task>() {

        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem == newItem

    }
}