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
                if (btnRemind.isChecked) {
                    taskTime.text = getCustomTimeString()
                    taskDate.text = getCustomDateString()

                    viewDate.visibility = View.VISIBLE
                    viewTime.visibility = View.VISIBLE

                    icDate.visibility = View.VISIBLE
                    icTime.visibility = View.VISIBLE

                    taskDate.visibility = View.VISIBLE
                    taskTime.visibility = View.VISIBLE

                    taskEnd.visibility = View.VISIBLE
                    dateEnd.visibility = View.VISIBLE
                } else {
                    viewDate.visibility = View.GONE
                    viewTime.visibility = View.GONE

                    icDate.visibility = View.GONE
                    icTime.visibility = View.GONE

                    taskDate.visibility = View.GONE
                    taskTime.visibility = View.GONE

                    taskEnd.visibility = View.GONE
                    dateEnd.visibility = View.GONE
                }
            }
        }
    }


    private fun initClickListeners() {
        with(binding) {
            backNewTask.setOnClickListener {
                findNavController().navigate(R.id.action_createNewTaskFragment_to_mainFragment)
            }

            colorPurpleTask.setOnClickListener {
                itemColor = ListColor.PURPLE
            }

            colorRedTask.setOnClickListener {
                itemColor = ListColor.RED
            }

            colorBlueTask.setOnClickListener {
                itemColor = ListColor.BLUE
            }

            colorOrangeTask.setOnClickListener {
                itemColor = ListColor.ORANGE
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
            if (etTaskName.text.toString().isNotEmpty() &&
                etDescriptionName.text.toString().isNotEmpty()
            ) {
                if (btnRemind.isChecked && taskTime.text.toString()
                        .isNotEmpty() && taskDate.text.toString().isNotEmpty()
                ) {
                    viewModel.addTask(
                        Task(
                            0,
                            taskTime.text.toString(),
                            calendar,
                            true,
                            etTaskName.text.toString(),
                            taskDate.text.toString(),
                            itemColor,
                            etDescriptionName.text.toString()
                        )
                    )
                } else if (!btnRemind.isChecked) {
                    viewModel.addTask(
                        Task(
                            0,
                            getCustomTimeString(),
                            null,
                            false,
                            etTaskName.text.toString(),
                            getCustomDateAndTimeString(),
                            itemColor,
                            etDescriptionName.text.toString()
                        )
                    )
                }
                findNavController().navigate(R.id.action_createNewTaskFragment_to_mainFragment)
            }
        }
    }

    private fun getCustomDateAndTimeString(): String {
        val pattern = "MM-dd-yyyy hh:mm:ss"
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.format(formatter)
    }

    private fun getCustomDateString(): String {
        val pattern = "MM-dd-yyyy"
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.format(formatter)
    }

    private fun getCustomTimeString(): String {
        val pattern = "hh:mm"
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
        with(binding) {
            taskTime.text = formatterTime.format(timeInMillis)
            taskDate.text = formatterDate.format(timeInMillis)
        }
    }
}