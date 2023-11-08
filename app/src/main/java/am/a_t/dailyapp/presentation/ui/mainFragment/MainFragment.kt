package am.a_t.dailyapp.presentation.ui.mainFragment

import am.a_t.dailyapp.R
import am.a_t.dailyapp.data.preferences.Preference
import am.a_t.dailyapp.data.preferences.Preference.Companion.TASK
import am.a_t.dailyapp.data.preferences.Preference.Companion.TASK_ID
import am.a_t.dailyapp.data.preferences.Preference.Companion.TYPE
import am.a_t.dailyapp.databinding.DialogCreateNewTaskBinding
import am.a_t.dailyapp.databinding.DialogNewListBinding
import am.a_t.dailyapp.databinding.FragmentMainBinding
import am.a_t.dailyapp.domain.module.ListTodo
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.utils.ListColor
import am.a_t.dailyapp.domain.utils.ListType
import am.a_t.dailyapp.extension.convertGsonToString
import am.a_t.dailyapp.extension.convertStringToGson
import am.a_t.dailyapp.presentation.adapter.ListTodoAdapter
import am.a_t.dailyapp.presentation.adapter.TaskAdapter
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
import androidx.navigation.fragment.findNavController
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

    private var isEdit = false
    private var isCreate = false
    private var isFilter = false
    private var isAlarmEdit = true
    private var isDateSuccess = false
    private var isDateNotPast = false
    private var isTimeNotPast = false
    private var isDateAndTimeNow = false
    private lateinit var snackBar: Snackbar
    private var listTask = emptyList<Task>()
    private var isDateOrTime: Boolean = true
    private var filterFromDate: String? = null
    private var listTodo = emptyList<ListTodo>()
    private val calendar = Calendar.getInstance()
    private lateinit var alertDialog: AlertDialog
    private lateinit var taskAdapter: TaskAdapter
    private var itemColor: ListColor = ListColor.RED
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var listTodoAdapter: ListTodoAdapter
    private lateinit var myDialogTodo: DialogNewListBinding
    private var mDay = getCustomDateString("dd").toInt()
    private var mHour = getCustomDateString("HH").toInt()
    private var mMonth = getCustomDateString("MM").toInt()
    private lateinit var myDialogTask: DialogCreateNewTaskBinding
    private var mMinute = getCustomDateString("mm").toInt()
    private var mYear = getCustomDateString("yyyy").toInt()
    private val formatterTime = SimpleDateFormat("HH:mm", Locale.US)
    private val preference: Preference by lazy { Preference(requireContext()) }
    private val formatterDate = SimpleDateFormat("MM-dd-yyyy", Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        initView()
        initAdapter(inflater, container)
        initViewModel(inflater, container)
        initClickListeners(inflater, container)

        return binding.root
    }

    // init View

    private fun initView() {
        lifecycleScope.launch(Dispatchers.Main) {
            with(binding) {
                tvFilterDate.setText(R.string.set_filter)
                readTypeData()
            }

            if (!preference.readType(TASK_ID).isNullOrEmpty()) {
                preference.readType(TASK_ID)?.convertStringToGson<Long>()
                    ?.let { viewModel.getTask(it) }
            }
        }
    }

    private suspend fun readTypeData() {
        with(binding) {
            btnType.text = preference.readType(TYPE)

            when (preference.readType(TYPE)) {
                ListType.LISTS.typeName -> {
                    recyclerViewAdapt(ListType.LISTS)
                }
                ListType.TASKS.typeName -> {
                    recyclerViewAdapt(ListType.TASKS)
                }
            }
        }
    }

    private fun showType() {
        with(binding) {
            if (btnType.text.toString() == ListType.LISTS.typeName) {
                saveType(ListType.TASKS.typeName, ListType.TASKS)
            } else {
                saveType(ListType.LISTS.typeName, ListType.LISTS)
            }
        }
    }

    private fun getCustomDateString(pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.format(formatter)
    }

    private fun cancelFilter() {
        lifecycleScope.launch {
            isFilter = false
            binding.tvFilterDate.setText(R.string.set_filter)

            when (preference.readType(TYPE)) {
                ListType.LISTS.typeName -> {
                    viewModel.getListTodoFilter(listTodo, null)
                }
                ListType.TASKS.typeName -> {
                    viewModel.getTaskFilter(listTask, null)
                }
            }
        }
    }

    private fun saveType(typeName: String, type: ListType) {
        lifecycleScope.launch {
            if (!preference.readType(TYPE).isNullOrEmpty()) {
                preference.removeType(TYPE)
            }
            preference.saveType(TYPE, typeName)
            binding.btnType.text = typeName

            recyclerViewAdapt(type)
        }
    }

    private suspend fun filterTaskOrListTodo(date: String) {
        isFilter = true
        filterFromDate = date

        when (preference.readType(TYPE)) {
            ListType.LISTS.typeName -> {
                viewModel.getListTodoFilter(listTodo, date)
            }
            ListType.TASKS.typeName -> {
                viewModel.getTaskFilter(listTask, date)
            }
        }
    }

    private fun warningDateAndTime(myDialogTask: DialogCreateNewTaskBinding) {
        with(myDialogTask) {
            lifecycleScope.launch {
                isDateSuccess = false
                btnCreateTask.setText(R.string.you_can_t_choose_time_in_the_past)
            }
        }
    }

    private fun successDateAndTime(myDialogTask: DialogCreateNewTaskBinding) {
        with(myDialogTask) {
            lifecycleScope.launch {
                isDateSuccess = true
                if (!isEdit) {
                    btnCreateTask.setText(R.string.create_task)
                } else {
                    btnCreateTask.setText(R.string.edit_task)
                }
            }
        }
    }

    // Adapters

    private fun initAdapter(inflater: LayoutInflater, container: ViewGroup?) {
        listTodoAdapter =
            ListTodoAdapter(
                requireContext(),
                inflater,
                container,
                viewModel
            ) { isEditList, isDelete, it ->
                when {
                    isDelete -> {
                        createCustomSnackBar(R.layout.snackbar_success_delete)
                    }
                    isEditList -> {
                        isEdit = true
                        initDialogListTodo(inflater, container, it)
                    }
                    else -> {
                        it?.id?.let { id ->
                            findNavController().navigate(
                                MainFragmentDirections.actionMainFragmentToTodoFragment(id)
                            )
                        }
                    }
                }
            }
        taskAdapter =
            TaskAdapter(requireContext(), inflater, container, viewModel) { isDelete, it ->
                if (isDelete) {
                    createCustomSnackBar(R.layout.snackbar_success_delete)
                } else {
                    isEdit = true
                    isDateOrTime = false
                    initDialogTask(inflater, container, it)
                }
            }
    }

    private fun recyclerViewAdapt(type: ListType) {
        with(binding) {
            if (type == ListType.LISTS) {
                rvList.layoutManager = LinearLayoutManager(requireContext())
                rvList.adapter = listTodoAdapter
                viewModel.getListTodoFilter(listTodo, null)
                tvFilterDate.setText(R.string.set_filter)
            } else {
                rvList.layoutManager = LinearLayoutManager(requireContext())
                rvList.adapter = taskAdapter
                viewModel.getTaskFilter(listTask, null)
                tvFilterDate.setText(R.string.set_filter)
            }
        }
    }

    // init ViewModel

    private fun initViewModel(inflater: LayoutInflater, container: ViewGroup?) {
        viewModel.getAllList()
        viewModel.getAllTask()

        lifecycleScope.launch {
            viewModel.getTask.collect {
                if (it != null) {
                    initDialogTask(
                        inflater,
                        container,
                        it
                    )
                }
            }
        }

        lifecycleScope.launch {
            viewModel.filterList.collectLatest {
                listTodoAdapter.submitList(it)
            }
        }

        lifecycleScope.launch {
            viewModel.filterTask.collectLatest {
                taskAdapter.submitList(it)
            }
        }

        lifecycleScope.launch {
            viewModel.listAllLiveData.first().collectLatest {
                listTodo = it
                if (!isFilter) {
                    listTodoAdapter.submitList(it)
                } else {
                    viewModel.getListTodoFilter(it, filterFromDate)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.taskAllLiveData.first().collectLatest {
                listTask = it
                if (!isFilter) {
                    taskAdapter.submitList(it)
                } else {
                    viewModel.getTaskFilter(it, filterFromDate)
                }
            }
        }
    }

    // init Click

    private fun initClickListeners(inflater: LayoutInflater, container: ViewGroup?) {
        with(binding) {

            btnCancelFilter.setOnClickListener {
                cancelFilter()
            }

            btnFilterDate.setOnClickListener {
                isDateOrTime = true
                showDatePicker()
            }

            btnAdd.setOnClickListener {
                addTaskOrListTodo(inflater, container)
            }

            btnType.setOnClickListener {
                showType()
            }
        }
    }

    // init Dialog

    private fun addTaskOrListTodo(inflater: LayoutInflater, container: ViewGroup?) {
        with(binding) {
            if (btnType.text.toString() == ListType.LISTS.typeName) {
                initDialogListTodo(inflater, container, null)
            } else {
                isDateOrTime = false
                initDialogTask(inflater, container, null)
            }
        }
    }

    // Dialog Tasks

    private fun initDialogTask(inflater: LayoutInflater, container: ViewGroup?, task: Task?) {
        myDialogTask = DialogCreateNewTaskBinding.inflate(inflater, container, false)
        with(myDialogTask) {
            alertDialog = AlertDialog.Builder(requireContext()).apply {
                setView(root)
                setCancelable(false)
            }.show()

            readTaskAlarm(myDialogTask, task)

            if (task != null) {
                edTaskName.setText(task.taskTitle)
                edDescriptionName.setText(task.taskDescription)
                tvTaskTime.text = task.taskEndTime
                tvTaskDate.text = task.taskDate
                itemColor = task.taskColor

                when (itemColor) {
                    ListColor.RED -> {
                        btnColorRedTask.isChecked = true
                        btnCreateTask.setBackgroundResource(R.drawable.btn_red)
                    }
                    ListColor.PURPLE -> {
                        btnColorPurpleTask.isChecked = true
                        btnCreateTask.setBackgroundResource(R.drawable.btn_purple)
                    }
                    ListColor.BLUE -> {
                        btnColorBlueTask.isChecked = true
                        btnCreateTask.setBackgroundResource(R.drawable.btn_blue)
                    }
                    ListColor.ORANGE -> {
                        btnColorOrangeTask.isChecked = true
                        btnCreateTask.setBackgroundResource(R.drawable.btn_orange)
                    }
                    else -> {
                        itemColor = ListColor.RED
                        btnColorRedTask.isChecked = true
                        btnCreateTask.setBackgroundResource(R.drawable.btn_red)
                    }
                }

                btnCreateTask.setText(R.string.edit_task)
            }

            dialogView(myDialogTask)

            dialogTaskButtonClickListeners(myDialogTask, alertDialog, task)

            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    private fun readTaskAlarm(myDialogTask: DialogCreateNewTaskBinding, task: Task?) {
        lifecycleScope.launch {
            if (!preference.readType(TASK_ID).isNullOrEmpty()) {
                taskDialogGone(myDialogTask)
                taskDialogVisibility(myDialogTask)
                setBtnColors(myDialogTask, task)
            }
        }
    }

    private fun setBtnColors(myDialogTask: DialogCreateNewTaskBinding, task: Task?) {
        with(myDialogTask) {
            when (task?.taskColor) {
                ListColor.RED -> {
                    btnDelete.setBackgroundResource(R.drawable.btn_red)
                    btnEdit.setBackgroundResource(R.drawable.btn_red)
                }
                ListColor.PURPLE -> {
                    btnDelete.setBackgroundResource(R.drawable.btn_purple)
                    btnEdit.setBackgroundResource(R.drawable.btn_purple)
                }
                ListColor.BLUE -> {
                    btnDelete.setBackgroundResource(R.drawable.btn_blue)
                    btnEdit.setBackgroundResource(R.drawable.btn_blue)
                }
                ListColor.ORANGE -> {
                    btnDelete.setBackgroundResource(R.drawable.btn_orange)
                    btnEdit.setBackgroundResource(R.drawable.btn_orange)
                }
                else -> {
                    itemColor = ListColor.RED
                    btnDelete.setBackgroundResource(R.drawable.btn_red)
                    btnEdit.setBackgroundResource(R.drawable.btn_red)
                }
            }
        }
    }

    private fun taskDialogGone(myDialogTask: DialogCreateNewTaskBinding) {
        with(myDialogTask) {
            edTaskName.isEnabled = false
            edDescriptionName.isEnabled = false
            btnCreateTask.visibility = View.GONE
            tvTaskColor.visibility = View.GONE
            radioGroup.visibility = View.GONE
            viewTime.visibility = View.GONE
            viewDate.visibility = View.GONE
            tvTaskTime.visibility = View.GONE
            icTime.visibility = View.GONE
            tvTaskDate.visibility = View.GONE
            icDate.visibility = View.GONE
        }
    }

    private fun taskDialogGoneVisibility(myDialogTask: DialogCreateNewTaskBinding) {
        with(myDialogTask) {
            edTaskName.isEnabled = true
            edDescriptionName.isEnabled = true
            btnCreateTask.visibility = View.VISIBLE
            tvTaskColor.visibility = View.VISIBLE
            radioGroup.visibility = View.VISIBLE
            viewTime.visibility = View.VISIBLE
            viewDate.visibility = View.VISIBLE
            tvTaskTime.visibility = View.VISIBLE
            icTime.visibility = View.VISIBLE
            tvTaskDate.visibility = View.VISIBLE
            icDate.visibility = View.VISIBLE
        }
    }

    private fun taskDialogVisibility(myDialogTask: DialogCreateNewTaskBinding) {
        with(myDialogTask) {
            if (edDescriptionName.text.isNullOrEmpty()) {
                edDescriptionName.visibility = View.GONE
                tvTaskDescriptionTitle.visibility = View.GONE
                viewSecondLine.visibility = View.GONE
            }
            btnDelete.visibility = View.VISIBLE
            btnEdit.visibility = View.VISIBLE
        }
    }

    private fun taskDialogVisibilityGone(myDialogTask: DialogCreateNewTaskBinding) {
        with(myDialogTask) {
            if (edDescriptionName.text.isNullOrEmpty()) {
                edDescriptionName.visibility = View.VISIBLE
                tvTaskDescriptionTitle.visibility = View.VISIBLE
                viewSecondLine.visibility = View.VISIBLE
            }
            btnDelete.visibility = View.GONE
            btnEdit.visibility = View.GONE
        }
    }

    private fun dialogTaskButtonClickListeners(
        myDialog: DialogCreateNewTaskBinding,
        alertDialog: AlertDialog,
        task: Task?
    ) {
        with(myDialog) {


            if (task == null) {
                itemColor = ListColor.RED
                btnColorRedTask.isChecked = true
            }


            btnEdit.setOnClickListener {
                isEdit = true
                isAlarmEdit = true
                isDateOrTime = false
                getDateAndTimeNow()
                taskDialogGoneVisibility(myDialogTask)
                taskDialogVisibilityGone(myDialogTask)
                removeTaskCoroutines(task)
            }

            btnDelete.setOnClickListener {
                deleteTask(task)
                alertDialog.dismiss()
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

            btnCreateTask.setOnClickListener {
                dateAndTimeNow()

                if (isDateAndTimeNow) {
                    if (!isEdit) {
                        addTask(myDialogTask, itemColor)
                    } else {
                        updateTask(myDialogTask, itemColor, task, true)
                    }

                    if (isCreate) {
                        createCustomSnackBar(R.layout.snackbar_warning)
                        isCreate = false
                    } else {
                        createCustomSnackBar(R.layout.snackbar_success)
                    }

                } else {
                    warningDateAndTime(myDialogTask)
                }

            }

            viewDate.setOnClickListener {
                showDatePicker()
            }

            viewTime.setOnClickListener {
                showTimePicker()
            }

            btnBackNewTask.setOnClickListener {
                removeAlarmTaskCoroutines(task)
                alertDialog.dismiss()
            }
        }
    }

    private fun removeAlarmTaskCoroutines(task: Task?) {
        lifecycleScope.launch {
            if (task?.taskColor != ListColor.GREY && isAlarmEdit) {
                updateTask(myDialogTask, ListColor.GREY, task, false)
                isAlarmEdit = false
            }
            removeTask()
        }
    }

    private suspend fun removeTask() {
        if (!preference.readType(TASK_ID).isNullOrEmpty()) {
            preference.removeType(TASK_ID)
        }

        if (!preference.readType(TASK).isNullOrEmpty()) {
            preference.removeType(TASK)
        }
    }

    private fun removeTaskCoroutines(task: Task?) {
        lifecycleScope.launch {
            if (task?.taskColor != ListColor.GREY && !isEdit) {
                updateTask(myDialogTask, ListColor.GREY, task, false)
            }
            removeTask()
        }
    }



    private fun deleteTask(task: Task?) {
        lifecycleScope.launch {
            task?.let { viewModel.removeTask(it) }
            removeTask()
        }
    }

    private fun addTask(myDialogTask: DialogCreateNewTaskBinding, itemColor: ListColor) {
        with(myDialogTask) {
            isCreate = if (edTaskName.text.toString().isNotEmpty()
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

    private fun updateTask(
        myDialogTask: DialogCreateNewTaskBinding,
        itemColor: ListColor,
        task: Task?,
        isAlarm: Boolean
    ) {
        with(myDialogTask) {
            if (edTaskName.text.toString().isNotEmpty()) {
                if (isAlarm) {
                    task?.copy(
                        taskEndTime = tvTaskTime.text.toString(),
                        taskCalendar = calendar,
                        taskIsAlarm = isAlarm,
                        taskTitle = edTaskName.text.toString(),
                        taskDate = tvTaskDate.text.toString(),
                        taskColor = itemColor,
                        taskDescription = edDescriptionName.text.toString()
                    )?.let {
                        viewModel.updateTask(
                            it
                        )
                        updateAlarm(it)
                    }
                } else {
                    task?.copy(
                        taskIsAlarm = isAlarm,
                        taskColor = itemColor
                    )?.let {
                        viewModel.updateTask(
                            it
                        )
                    }
                }
                cancelFilter()
            }

            isEdit = false

            alertDialog.dismiss()
        }
    }

    private fun updateAlarm(task: Task?) {
        lifecycleScope.launch {
            preference.saveType(TASK, task.convertGsonToString())
        }
    }

    private fun dialogView(myDialogTask: DialogCreateNewTaskBinding) {
        with(myDialogTask) {
            getDateAndTimeNow()
            tvTaskTime.text = getCustomDateString("HH:mm")
            tvTaskDate.text = getCustomDateString("MM-dd-yyyy")
        }
    }

    // Dialog ListTodos

    private fun initDialogListTodo(
        inflater: LayoutInflater,
        container: ViewGroup?,
        listTodo: ListTodo?
    ) {
        myDialogTodo = DialogNewListBinding.inflate(inflater, container, false)
        with(myDialogTodo) {
            alertDialog = AlertDialog.Builder(requireContext()).apply {
                setView(root)
                setCancelable(false)
            }.show()

            if (listTodo != null) {
                edTodoName.setText(listTodo.listTitle)
                itemColor = listTodo.listColor

                when (itemColor) {
                    ListColor.RED -> {
                        btnColorRedList.isChecked = true
                        btnCreateList.setBackgroundResource(R.drawable.btn_red)
                    }
                    ListColor.PURPLE -> {
                        btnColorPurpleList.isChecked = true
                        btnCreateList.setBackgroundResource(R.drawable.btn_purple)
                    }
                    ListColor.BLUE -> {
                        btnColorBlueList.isChecked = true
                        btnCreateList.setBackgroundResource(R.drawable.btn_blue)
                    }
                    ListColor.ORANGE -> {
                        btnColorOrangeList.isChecked = true
                        btnCreateList.setBackgroundResource(R.drawable.btn_orange)
                    }
                    else -> {
                        itemColor = ListColor.RED
                        btnColorRedList.isChecked = true
                        btnCreateList.setBackgroundResource(R.drawable.btn_red)
                    }
                }

                btnCreateList.setText(R.string.edit_list)
            }

            dialogTodoButtonClickListeners(myDialogTodo, alertDialog, listTodo)

            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    private fun dialogTodoButtonClickListeners(
        myDialog: DialogNewListBinding,
        alertDialog: AlertDialog,
        listTodo: ListTodo?
    ) {
        with(myDialog) {

            if (listTodo == null) {
                itemColor = ListColor.RED
                btnColorRedList.isChecked = true
            }

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
                when {
                    isEdit -> {
                        updateListTodo(listTodo)
                    }
                    else -> {
                        addListTodo()
                    }
                }

                if (isCreate) {
                    createCustomSnackBar(R.layout.snackbar_warning)
                    isCreate = false
                } else {
                    createCustomSnackBar(R.layout.snackbar_success)
                }
            }

            btnCancelNewList.setOnClickListener {
                alertDialog.dismiss()
            }
        }
    }

    private fun addListTodo() {
        with(myDialogTodo) {
            isCreate = if (edTodoName.text.toString().isNotEmpty()) {
                viewModel.addList(
                    ListTodo(
                        0,
                        false,
                        edTodoName.text.toString(),
                        getCustomDateString("MM-dd-yyyy"),
                        itemColor,
                        arrayListOf()
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

    private fun updateListTodo(listTodo: ListTodo?) {
        with(myDialogTodo) {
            if (edTodoName.text.toString().isNotEmpty()) {
                listTodo?.copy(
                    listTitle = edTodoName.text.toString(),
                    listDate = getCustomDateString("MM-dd-yyyy"),
                    listColor = itemColor,
                    listTodo = arrayListOf()
                )?.let {
                    viewModel.updateList(
                        it
                    )
                }
                cancelFilter()
            }

            isEdit = false

            alertDialog.dismiss()
        }
    }

    // Time Dialog

    private fun showTimePicker() {
        TimePickerDialog(
            requireContext(),
            this,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
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
            successDateAndTime(myDialogTask)
        } else if (getCustomDateString("HH").toInt() > hourOfDay ||
            (getCustomDateString("HH").toInt() == hourOfDay && getCustomDateString("mm").toInt() > minute)
        ) {
            mHour = hourOfDay
            mMinute = minute
            displayFormattedTime(myDialogTask, calendar.timeInMillis)
            warningDateAndTime(myDialogTask)
        } else {
            mHour = hourOfDay
            mMinute = minute
            displayFormattedTime(myDialogTask, calendar.timeInMillis)
            successDateAndTime(myDialogTask)
        }
    }

    private fun displayFormattedTime(myDialogTask: DialogCreateNewTaskBinding, timeInMillis: Long) {
        lifecycleScope.launch {
            with(myDialogTask) {
                tvTaskTime.text = formatterTime.format(timeInMillis)
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

    // Date Dialog

    private fun showDatePicker() {
        DatePickerDialog(
            requireContext(),
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
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
                mYear = year
                mMonth = (month + 1)
                mDay = dayOfMonth
                displayFormattedDate(calendar.timeInMillis)
                warningDateAndTime(myDialogTask)
            } else if (getCustomDateString("yyyy").toInt() == year &&
                getCustomDateString("MM").toInt() == (month + 1) &&
                getCustomDateString("dd").toInt() == dayOfMonth
            ) {
                isDateNotPast = false
                getTimeNow()
                if (isTimeNotPast) {
                    mYear = year
                    mMonth = (month + 1)
                    mDay = dayOfMonth
                    displayFormattedDate(calendar.timeInMillis)
                    successDateAndTime(myDialogTask)
                } else {
                    mYear = year
                    mMonth = (month + 1)
                    mDay = dayOfMonth
                    displayFormattedDate(calendar.timeInMillis)
                    warningDateAndTime(myDialogTask)
                }
            } else {
                isDateNotPast = true
                mYear = year
                mMonth = (month + 1)
                mDay = dayOfMonth
                displayFormattedDate(calendar.timeInMillis)
                successDateAndTime(myDialogTask)
            }
        }
    }

    private fun displayFormattedDate(timeInMillis: Long) {
        lifecycleScope.launch {
            if (isDateOrTime) {
                with(binding) {
                    tvFilterDate.text = formatterDate.format(timeInMillis)
                    filterTaskOrListTodo(formatterDate.format(timeInMillis))
                }
            } else {
                with(myDialogTask) {
                    tvTaskDate.text = formatterDate.format(timeInMillis)
                }
            }
        }
    }

    // Date and Time

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

    // init SnackBar

    private fun createCustomSnackBar(@LayoutRes layoutRes: Int) {
        snackBar = Snackbar.make(binding.root, "Custom", Snackbar.LENGTH_LONG).apply {
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