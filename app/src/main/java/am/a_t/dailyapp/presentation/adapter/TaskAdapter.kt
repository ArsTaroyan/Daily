package am.a_t.dailyapp.presentation.adapter

import am.a_t.dailyapp.databinding.ItemTaskBinding
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.presentation.ui.mainFragment.MainViewModel
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val viewModel: MainViewModel, private val click: (Task) -> Unit) :
    ListAdapter<Task, TaskAdapter.MyViewHolder>(DiffUtilItemCallBack()) {

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
                    click(task)
                }
            }
        }
    }

    class DiffUtilItemCallBack : DiffUtil.ItemCallback<Task>() {

        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem == newItem

    }
}