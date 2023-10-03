package am.a_t.dailyapp.presentation.ui.createNewTask

import am.a_t.dailyapp.R
import am.a_t.dailyapp.databinding.FragmentCreateNewTaskBinding
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.utils.ListColor
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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
    private val formatterTime = SimpleDateFormat("HH:mm", Locale.US)
    private val formatterDate = SimpleDateFormat("MM-dd-yyyy", Locale.US)
    private lateinit var snackbar: Snackbar
    private var isDateNotPast = false
    private var isTimeNotPast = false
    private var isDateAndTimeNow = false
    private var mYear = getCustomDateString("yyyy").toInt()
    private var mMonth = getCustomDateString("MM").toInt()
    private var mDay = getCustomDateString("dd").toInt()
    private var mHour = getCustomDateString("HH").toInt()
    private var mMinute = getCustomDateString("mm").toInt()

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
                tvTaskTime.text = getCustomDateString("HH:mm")
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
        lifecycleScope.launch {
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
    }

    private fun isVisibility() {
        lifecycleScope.launch {
            with(binding) {
                viewDate.visibility = View.VISIBLE
                viewTime.visibility = View.VISIBLE

                icDate.visibility = View.VISIBLE
                icTime.visibility = View.VISIBLE

                tvTaskDate.visibility = View.VISIBLE
                tvTaskTime.visibility = View.VISIBLE

                tvTaskEnd.visibility = View.VISIBLE
                tvDateEnd.visibility = View.VISIBLE

                getDateAndTimeNow()
            }
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

                if (btnRemind.isChecked) {
                    dateAndTimeNow()
                    if (isDateAndTimeNow) {
                        createTask()
                    } else {
                        createCustomSnackbar(R.layout.snackbar_warning_date)
                    }
                } else {
                    createTask()
                }

            }

            viewDate.setOnClickListener {
                showDatePicker()
            }

            viewTime.setOnClickListener {
                showTimePicker()
            }

        }
    }

    private fun getTimeNow() {
        isTimeNotPast = when {
            getCustomDateString("HH").toInt() < mHour -> {
                true
            }
            getCustomDateString("HH").toInt() > mHour -> {
                false
            }
            getCustomDateString("HH").toInt() == mHour && getCustomDateString("mm").toInt() > mMinute -> {
                false
            }
            else -> {
                true
            }
        }
    }

    private fun getDateAndTimeNow() {
        lifecycleScope.launch {
            mYear = getCustomDateString("yyyy").toInt()
            mMonth = getCustomDateString("MM").toInt()
            mDay = getCustomDateString("dd").toInt()
            mHour = getCustomDateString("HH").toInt()
            mMinute = getCustomDateString("mm").toInt()
        }
    }

    private fun dateAndTimeNow() {
        isDateAndTimeNow = when {
            getCustomDateString("yyyy").toInt() > mYear -> {
                false
            }
            getCustomDateString("yyyy").toInt() < mYear -> {
                true
            }
            getCustomDateString("MM").toInt() > mMonth -> {
                false
            }
            getCustomDateString("MM").toInt() < mMonth -> {
                true
            }
            getCustomDateString("dd").toInt() > mDay -> {
                false
            }
            getCustomDateString("dd").toInt() < mDay -> {
                true
            }
            getCustomDateString("HH").toInt() > mHour -> {
                false
            }
            getCustomDateString("HH").toInt() < mHour -> {
                true
            }
            getCustomDateString("HH").toInt() == mHour && getCustomDateString("mm").toInt() > mMinute -> {
                false
            }
            else -> {
                true
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
                val create =
                    CreateNewTaskFragmentDirections.actionCreateNewTaskFragmentToMainFragment(1)
                findNavController().navigate(create)
            } else {
                createCustomSnackbar(R.layout.snackbar_warning)
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
                    getCustomDateString("HH:mm"),
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

        if (getCustomDateString("yyyy").toInt() > year ||
            getCustomDateString("MM").toInt() > (month + 1) ||
            getCustomDateString("dd").toInt() > dayOfMonth
        ) {
            isDateNotPast = false
            createCustomSnackbar(R.layout.snackbar_warning_date)
        } else if (getCustomDateString("yyyy").toInt() == year &&
            getCustomDateString("MM").toInt() == (month + 1) &&
            getCustomDateString("dd").toInt() == dayOfMonth
        ) {
            getTimeNow()
            if (isTimeNotPast) {
                isDateNotPast = false
                mYear = year
                mMonth = (month + 1)
                mDay = dayOfMonth
                displayFormattedDate(calendar.timeInMillis)
            } else {
                createCustomSnackbar(R.layout.snackbar_warning_date)
            }
        } else {
            isDateNotPast = true
            mYear = year
            mMonth = (month + 1)
            mDay = dayOfMonth
            displayFormattedDate(calendar.timeInMillis)
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }

        if (isDateNotPast) {
            mHour = hourOfDay
            mMinute = minute
            displayFormattedTime(calendar.timeInMillis)
        } else if (getCustomDateString("HH").toInt() > hourOfDay ||
            getCustomDateString("mm").toInt() > minute
        ) {
            createCustomSnackbar(R.layout.snackbar_warning_date)
        } else {
            mHour = hourOfDay
            mMinute = minute
            displayFormattedTime(calendar.timeInMillis)
        }
    }

    private fun displayFormattedDate(timeInMillis: Long) {
        lifecycleScope.launch {
            with(binding) {
                tvTaskDate.text = formatterDate.format(timeInMillis)
            }
        }
    }

    private fun displayFormattedTime(timeInMillis: Long) {
        lifecycleScope.launch {
            with(binding) {
                tvTaskTime.text = formatterTime.format(timeInMillis)
            }
        }
    }

    private fun createCustomSnackbar(@LayoutRes layoutRes: Int) {
        snackbar = Snackbar.make(binding.root, "Custom", Snackbar.LENGTH_LONG).apply {
            setAction("Retry") {
                dismiss()
            }
            view.setBackgroundColor(Color.TRANSPARENT)
            val view = layoutInflater.inflate(layoutRes, null)
            (this.view as Snackbar.SnackbarLayout).addView(view)
            show()
        }
    }
}