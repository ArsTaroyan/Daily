package am.a_t.dailyapp.presentation.ui.createNewTask

import am.a_t.dailyapp.R
import am.a_t.dailyapp.databinding.FragmentCreateNewTaskBinding
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.utils.ListColor
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class CreateNewTaskFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: FragmentCreateNewTaskBinding
    private val viewModel: CreateNewTaskViewModel by viewModels()
    private var itemColor: ListColor = ListColor.RED
    private val calendar = Calendar.getInstance()
    private val formatterTime = SimpleDateFormat("hh:mm", Locale.US)
    private val formatterDate = SimpleDateFormat("MM-dd-yyyy", Locale.US)
    private var isCreate = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNewTaskBinding.inflate(inflater, container, false)

        initView()
        initClickListeners()

        return binding.root
    }

    private fun initView() {
        lifecycleScope.launch(Dispatchers.Main) {
            with(binding) {
                tvTaskTime.text = getCustomDateString("hh:mm")
                tvTaskDate.text = getCustomDateString("MM-dd-yyyy")

                if (btnRemind.isChecked) {
                    isVisibility()
                } else {
                    isGone()
                }
            }
        }
    }

    private fun isGone() {
        with(binding) {
            viewDate.visibility = View.GONE
            viewTime.visibility = View.GONE

            icDate.visibility = View.GONE
            icTime.visibility = View.GONE

            tvTaskDate.visibility = View.GONE
            tvTaskTime.visibility = View.GONE

            tvTaskEnd.visibility = View.GONE
            tvDateEnd.visibility = View.GONE
        }
    }

    private fun isVisibility() {
        with(binding) {
            viewDate.visibility = View.VISIBLE
            viewTime.visibility = View.VISIBLE

            icDate.visibility = View.VISIBLE
            icTime.visibility = View.VISIBLE

            tvTaskDate.visibility = View.VISIBLE
            tvTaskTime.visibility = View.VISIBLE

            tvTaskEnd.visibility = View.VISIBLE
            tvDateEnd.visibility = View.VISIBLE
        }
    }


    private fun initClickListeners() {
        with(binding) {

            btnBackNewTask.setOnClickListener {
                findNavController().navigate(R.id.action_createNewTaskFragment_to_mainFragment)
            }

            btnColorRedTask.setOnClickListener {
                itemColor = ListColor.RED
                btnCreateTask.setBackgroundResource(R.drawable.btn_red)
            }

            btnColorPurpleTask.setOnClickListener {
                itemColor = ListColor.PURPLE
                btnCreateTask.setBackgroundResource(R.drawable.btn_purple)
            }

            btnColorBlueTask.setOnClickListener {
                itemColor = ListColor.BLUE
                btnCreateTask.setBackgroundResource(R.drawable.btn_blue)
            }

            btnColorOrangeTask.setOnClickListener {
                itemColor = ListColor.ORANGE
                btnCreateTask.setBackgroundResource(R.drawable.btn_orange)
            }

            btnRemind.setOnClickListener {
                initView()
            }

            btnCreateTask.setOnClickListener {
                createTask()
            }

            viewDate.setOnClickListener {
                showDatePicker()
            }

            viewTime.setOnClickListener {
                showTimePicker()
            }

        }
    }

    private fun showTimePicker() {
        TimePickerDialog(
            requireContext(),
            this,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun showDatePicker() {
        DatePickerDialog(
            requireContext(),
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun createTask() {
        with(binding) {
            if (edTaskName.text.toString().isNotEmpty() &&
                edDescriptionName.text.toString().isNotEmpty()
            ) {
                addTask()
                val create = CreateNewTaskFragmentDirections.actionCreateNewTaskFragmentToMainFragment(1)
                findNavController().navigate(create)
            } else {
                val create = CreateNewTaskFragmentDirections.actionCreateNewTaskFragmentToMainFragment(2)
                findNavController().navigate(create)
            }
        }
    }

    private fun addTask() {
        with(binding) {
            if (btnRemind.isChecked) {
                addTaskIsChecked()
            } else {
                addTaskIsNotChecked()
            }
        }
    }

    private fun addTaskIsNotChecked() {
        with(binding) {
            viewModel.addTask(
                Task(
                    0,
                    getCustomDateString("hh:mm"),
                    null,
                    false,
                    edTaskName.text.toString(),
                    getCustomDateString("MM-dd-yyyy"),
                    itemColor,
                    edDescriptionName.text.toString()
                )
            )
        }
    }

    private fun addTaskIsChecked() {
        with(binding) {
            viewModel.addTask(
                Task(
                    0,
                    tvTaskTime.text.toString(),
                    calendar,
                    true,
                    edTaskName.text.toString(),
                    tvTaskDate.text.toString(),
                    itemColor,
                    edDescriptionName.text.toString()
                )
            )
        }
    }

    private fun getCustomDateString(pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.format(formatter)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        displayFormattedDate(calendar.timeInMillis)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }
        displayFormattedDate(calendar.timeInMillis)
    }

    private fun displayFormattedDate(timeInMillis: Long) {
        lifecycleScope.launch {
            with(binding) {
                tvTaskTime.text = formatterTime.format(timeInMillis)
                tvTaskDate.text = formatterDate.format(timeInMillis)
            }
        }
    }
}