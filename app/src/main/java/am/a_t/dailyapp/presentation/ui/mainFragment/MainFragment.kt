package am.a_t.dailyapp.presentation.ui.mainFragment

import am.a_t.dailyapp.R
import am.a_t.dailyapp.data.preferences.Preference
import am.a_t.dailyapp.data.preferences.Preference.Companion.TYPE
import am.a_t.dailyapp.databinding.DialogCreateNewTaskBinding
import am.a_t.dailyapp.databinding.DialogNewListBinding
import am.a_t.dailyapp.databinding.FragmentMainBinding
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.presentation.adapter.TaskAdapter
import am.a_t.dailyapp.presentation.adapter.TodoAdapter
import am.a_t.dailyapp.utils.ListColor
import am.a_t.dailyapp.utils.ListType
import android.app.AlertDialog
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class MainFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var taskAdapter: TaskAdapter
    private val preference: Preference by lazy { Preference(requireContext()) }
    private lateinit var myDialogTodo: DialogNewListBinding
    private lateinit var myDialogTask: DialogCreateNewTaskBinding
    private lateinit var alertDialog: AlertDialog
    private val calendar = Calendar.getInstance()
    private var listTask = emptyList<Task>()
    private var listTodo = emptyList<Todo>()
    private var isDateNotPast = false
    private var isTimeNotPast = false
    private var isDateAndTimeNow = false
    private val formatterTime = SimpleDateFormat("HH:mm", Locale.US)
    private var mYear = getCustomDateString("yyyy").toInt()
    private var mMonth = getCustomDateString("MM").toInt()
    private var mDay = getCustomDateString("dd").toInt()
    private var mHour = getCustomDateString("HH").toInt()
    private var mMinute = getCustomDateString("mm").toInt()
    private val formatterDate = SimpleDateFormat("MM-dd-yyyy", Locale.US)
    private var isCreate = false
    private var isFilter = false
    private var filterFromDate: String? = null
    private lateinit var snackbar: Snackbar
    private var isDateOrTime: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        initView()
        initAdapter(inflater, container)
        initViewModel()
        initClickListeners(inflater, container)

        return binding.root
    }

    private fun initView() {
        lifecycleScope.launch(Dispatchers.Main) {
            with(binding) {
                tvFilterDate.setText(R.string.set_filter)
                readTypeData()
            }
        }
    }

    private suspend fun readTypeData() {
        with(binding) {
            if (preference.readType(TYPE).isNullOrEmpty()) {
                btnType.text = ListType.TODOS.typeName
                recyclerViewAdapt(ListType.TODOS)
            } else {
                btnType.text = preference.readType(TYPE)

                when (preference.readType(TYPE)) {
                    ListType.TODOS.typeName -> {
                        recyclerViewAdapt(ListType.TODOS)
                    }
                    ListType.TASKS.typeName -> {
                        recyclerViewAdapt(ListType.TASKS)
                    }
                }
            }
        }
    }

    private fun initAdapter(inflater: LayoutInflater, container: ViewGroup?) {
        todoAdapter = TodoAdapter(requireContext(), inflater, container, viewModel) {
            createCustomSnackbar(R.layout.snackbar_success_delete)
        }
        taskAdapter = TaskAdapter(requireContext(), inflater, container, viewModel) {
            createCustomSnackbar(R.layout.snackbar_success_delete)
        }
    }

    private fun initViewModel() {
        viewModel.getAllTodo()
        viewModel.getAllTask()

        lifecycleScope.launchWhenResumed {
            viewModel.filterTodo.collectLatest {
                todoAdapter.submitList(it)
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.filterTask.collectLatest {
                taskAdapter.submitList(it)
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.todoAllLiveData.first().collectLatest {
                listTodo = it
                if (!isFilter) {
                    todoAdapter.submitList(it)
                } else {
                    viewModel.getTaskFilter(listTask, filterFromDate)
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.taskAllLiveData.first().collectLatest {
                listTask = it
                if (!isFilter) {
                    taskAdapter.submitList(it)
                } else {
                    viewModel.getTaskFilter(listTask, filterFromDate)
                }
            }
        }

    }

    private fun initClickListeners(inflater: LayoutInflater, container: ViewGroup?) {
        with(binding) {

            btnCancelFilter.setOnClickListener {
                cancelFilter()
            }

            btnFilterDate.setOnClickListener {
                isDateOrTime = true
                showDialogPicker()
            }

            btnAdd.setOnClickListener {
                addTaskOrTodo(inflater, container)
            }

            btnType.setOnClickListener {
                showType()
            }
        }
    }

    private fun showType() {
        with(binding) {
            lifecycleScope.launch(Dispatchers.Main) {
                if (btnType.text.toString() == ListType.TODOS.typeName) {
                    saveType(ListType.TASKS.typeName, ListType.TASKS)
                } else {
                    saveType(ListType.TODOS.typeName, ListType.TODOS)
                }
            }
        }
    }

    private fun addTaskOrTodo(inflater: LayoutInflater, container: ViewGroup?) {
        with(binding) {
            if (btnType.text.toString() == ListType.TODOS.typeName) {
                initDialogTodo(inflater, container)
            } else {
                isDateOrTime = false
                initDialogTask(inflater, container)
            }
        }
    }

    private fun cancelFilter() {
        lifecycleScope.launch {
            isFilter = false

            binding.tvFilterDate.setText(R.string.set_filter)

            when (preference.readType(TYPE)) {
                ListType.TODOS.typeName -> {
                    viewModel.getTodoFilter(listTodo, null)
                }
                ListType.TASKS.typeName -> {
                    viewModel.getTaskFilter(listTask, null)
                }
            }
        }
    }

    private fun showDialogPicker() {
        DatePickerDialog(
            requireContext(),
            this@MainFragment,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun initDialogTodo(inflater: LayoutInflater, container: ViewGroup?) {
        myDialogTodo = DialogNewListBinding.inflate(inflater, container, false)
        with(myDialogTodo) {
            alertDialog = AlertDialog.Builder(requireContext())
                .setView(root)
                .show()

            dialogTodoButtonClickListeners(myDialogTodo, alertDialog)

            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    private fun dialogTodoButtonClickListeners(
        myDialog: DialogNewListBinding,
        alertDialog: AlertDialog
    ) {
        var itemColor: ListColor = ListColor.RED

        with(myDialog) {

            btnColorRedList.setOnClickListener {
                itemColor = ListColor.RED
                btnCreateList.setBackgroundResource(R.drawable.btn_red)
            }

            btnColorPurpleList.setOnClickListener {
                itemColor = ListColor.PURPLE
                btnCreateList.setBackgroundResource(R.drawable.btn_purple)
            }

            btnColorBlueList.setOnClickListener {
                itemColor = ListColor.BLUE
                btnCreateList.setBackgroundResource(R.drawable.btn_blue)
            }

            btnColorOrangeList.setOnClickListener {
                itemColor = ListColor.ORANGE
                btnCreateList.setBackgroundResource(R.drawable.btn_orange)
            }

            btnCreateList.setOnClickListener {
                addTodo(itemColor)
                if (isCreate) {
                    createCustomSnackbar(R.layout.snackbar_warning)
                } else {
                    createCustomSnackbar(R.layout.snackbar_success)
                }
            }

            btnCancelNewList.setOnClickListener {
                alertDialog.dismiss()
            }
        }
    }

    private fun initDialogTask(inflater: LayoutInflater, container: ViewGroup?) {
        myDialogTask = DialogCreateNewTaskBinding.inflate(inflater, container, false)
        with(myDialogTask) {
            alertDialog = AlertDialog.Builder(requireContext())
                .setView(root)
                .show()

            dialogView(myDialogTask)
            dialogTaskButtonClickListeners(myDialogTask, alertDialog)

            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    private fun dialogView(myDialogTask: DialogCreateNewTaskBinding) {
        with(myDialogTask) {
            getDateAndTimeNow()
            tvTaskTime.text = getCustomDateString("HH:mm")
            tvTaskDate.text = getCustomDateString("MM-dd-yyyy")
        }
    }

    private fun dialogTaskButtonClickListeners(
        myDialog: DialogCreateNewTaskBinding,
        alertDialog: AlertDialog
    ) {
        var itemColor: ListColor = ListColor.RED

        with(myDialog) {

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

            btnCreateTask.setOnClickListener {
                dateAndTimeNow()

                if (isDateAndTimeNow) {
                    addTask(myDialogTask, itemColor)
                    if (isCreate) {
                        createCustomSnackbar(R.layout.snackbar_warning)
                    } else {
                        createCustomSnackbar(R.layout.snackbar_success)
                    }
                } else {
                    createCustomSnackbar(R.layout.snackbar_warning_date)
                }

            }

            viewDate.setOnClickListener {
                showDatePicker()
            }

            viewTime.setOnClickListener {
                showTimePicker()
            }

            btnBackNewTask.setOnClickListener {
                alertDialog.dismiss()
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

    private fun addTask(myDialogTask: DialogCreateNewTaskBinding, itemColor: ListColor) {
        with(myDialogTask) {
            isCreate = if (edTaskName.text.toString().isNotEmpty() &&
                edDescriptionName.text.toString().isNotEmpty()
            ) {
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
                cancelFilter()
                false
            } else {
                true
            }

            alertDialog.dismiss()
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

    private fun addTodo(itemColor: ListColor) {
        with(myDialogTodo) {
            isCreate = if (edTodoName.text.toString().isNotEmpty()) {
                viewModel.addTodo(
                    Todo(
                        0,
                        false,
                        edTodoName.text.toString(),
                        getCustomDateString("MM-dd-yyyy"),
                        itemColor,
                        null
                    )
                )
                cancelFilter()
                false
            } else {
                true
            }
            alertDialog.dismiss()
        }
    }

    private fun getCustomDateString(pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.format(formatter)
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

    private fun recyclerViewAdapt(type: ListType) {
        if (type == ListType.TODOS) {
            binding.rvList.layoutManager = LinearLayoutManager(requireContext())
            binding.rvList.adapter = todoAdapter
            viewModel.getTodoFilter(listTodo, null)
            binding.tvFilterDate.setText(R.string.set_filter)
        } else {
            binding.rvList.layoutManager = LinearLayoutManager(requireContext())
            binding.rvList.adapter = taskAdapter
            viewModel.getTaskFilter(listTask, null)
            binding.tvFilterDate.setText(R.string.set_filter)
        }
    }

    private suspend fun saveType(typeName: String, type: ListType) {

        if (!preference.readType(TYPE).isNullOrEmpty()) {
            preference.removeType(TYPE)
        }
        preference.saveType(TYPE, typeName)
        binding.btnType.text = typeName

        recyclerViewAdapt(type)

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        if (isDateOrTime) {
            displayFormattedDate(calendar.timeInMillis)
        } else {
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
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }

        if (isDateNotPast) {
            mHour = hourOfDay
            mMinute = minute
            displayFormattedTime(myDialogTask, calendar.timeInMillis)
        } else if (getCustomDateString("HH").toInt() > hourOfDay ||
            getCustomDateString("mm").toInt() > minute
        ) {
            createCustomSnackbar(R.layout.snackbar_warning_date)
        } else {
            mHour = hourOfDay
            mMinute = minute
            displayFormattedTime(myDialogTask, calendar.timeInMillis)
        }
    }

    private fun displayFormattedTime(myDialogTask: DialogCreateNewTaskBinding, timeInMillis: Long) {
        lifecycleScope.launch {
            with(myDialogTask) {
                tvTaskTime.text = formatterTime.format(timeInMillis)
            }
        }
    }

    private fun displayFormattedDate(timeInMillis: Long) {
        lifecycleScope.launch {
            if (isDateOrTime) {
                with(binding) {
                    tvFilterDate.text = formatterDate.format(timeInMillis)
                    filterTaskOrTodo(formatterDate.format(timeInMillis))
                }
            } else {
                with(myDialogTask) {
                    tvTaskDate.text = formatterDate.format(timeInMillis)
                }
            }
        }
    }

    private suspend fun filterTaskOrTodo(date: String) {
        isFilter = true
        filterFromDate = date

        when (preference.readType(TYPE)) {
            ListType.TODOS.typeName -> {
                viewModel.getTodoFilter(listTodo, date)
            }
            ListType.TASKS.typeName -> {
                viewModel.getTaskFilter(listTask, date)
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
}