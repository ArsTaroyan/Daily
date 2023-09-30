package am.a_t.dailyapp.presentation.adapter

import am.a_t.dailyapp.R
import am.a_t.dailyapp.databinding.DialogDeleteBinding
import am.a_t.dailyapp.databinding.ItemTaskBinding
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.presentation.ui.mainFragment.MainViewModel
import am.a_t.dailyapp.utils.ListColor
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MyViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(task: Task) {
            with(binding) {
                tvTitle.isSelected = true
                tvTitle.text = task.taskTitle
                tvDate.text = getCustomDateString()

                if(tvTime.text.toString().isEmpty()) {
                    tvTime.visibility = View.GONE
                } else {
                    tvTime.text = task.taskEndTime
                    tvDate.text = task.taskDate
                }

                btnDelete.setOnClickListener {
                    removeDialog(inflater, container, task)
                }

                when (task.taskColor) {
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
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCustomDateString(): String {
        val pattern = "MM-dd-yyyy"
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val currentDateTime = LocalDateTime.now()
        val customDateString = currentDateTime.format(formatter)
        return customDateString
    }

    private fun removeDialog(inflater: LayoutInflater, container: ViewGroup?, task: Task) {
        myDialog = DialogDeleteBinding.inflate(inflater, container, false)

        with(myDialog) {
            alertDialog = AlertDialog.Builder(context)
                .setView(root)
                .show()

            when (task.taskColor) {
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
            }

            btnYesTodo.setOnClickListener {
                viewModel.removeTask(task)
                alertDialog.dismiss()
            }

            btnNoTodo.setOnClickListener {
                alertDialog.dismiss()
            }
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