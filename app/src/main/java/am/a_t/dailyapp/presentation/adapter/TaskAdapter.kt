package am.a_t.dailyapp.presentation.adapter

import am.a_t.dailyapp.R
import am.a_t.dailyapp.databinding.DialogDeleteBinding
import am.a_t.dailyapp.databinding.ItemTaskBinding
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.utils.AlarmReceiver
import am.a_t.dailyapp.domain.utils.ListColor
import am.a_t.dailyapp.presentation.ui.mainFragment.MainViewModel
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
    private val click: (Boolean, Task?) -> Unit
) :
    ListAdapter<Task, TaskAdapter.MyViewHolder>(DiffUtilItemCallBackTask()) {
    private lateinit var myDialog: DialogDeleteBinding
    private lateinit var alertDialog: AlertDialog
    private var alarmManager: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

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

                tvTitle.isSelected = true
                tvTitle.text = task.taskTitle
                tvTime.text = task.taskEndTime
                tvDate.text = task.taskDate

                btnDelete.setOnClickListener {
                    removeDialog(inflater, container, task)
                }

                when (task.taskColor) {
                    ListColor.RED -> {
                        viewTask.setBackgroundResource(R.drawable.btn_red)
                    }
                    ListColor.BLUE -> {
                        viewTask.setBackgroundResource(R.drawable.btn_blue)
                    }
                    ListColor.ORANGE -> {
                        viewTask.setBackgroundResource(R.drawable.btn_orange)
                    }
                    ListColor.PURPLE -> {
                        viewTask.setBackgroundResource(R.drawable.btn_purple)
                    }
                }

                btnEdit.setOnClickListener {
                    click(false, task)
                }

            }

            if (task.taskIsAlarm) {
                initAlarm(task)
                viewModel.updateTask(task.copy(taskIsAlarm = false))
            }
        }
    }

    private fun initAlarm(task: Task) {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            intent.putExtra("title", task.taskTitle)
            intent.putExtra("description", task.taskDescription)
            intent.putExtra("id", task.id)
            PendingIntent.getBroadcast(
                context,
                task.id.toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
        task.taskCalendar?.timeInMillis?.let {
            alarmManager?.setExact(
                AlarmManager.RTC_WAKEUP,
                it,
                alarmIntent
            )
        }
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

class DiffUtilItemCallBackTask : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
        oldItem == newItem

}