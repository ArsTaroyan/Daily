package am.a_t.dailyapp.presentation.ui.mainFragment

import am.a_t.dailyapp.R
import am.a_t.dailyapp.data.preferences.Preference
import am.a_t.dailyapp.data.preferences.Preference.Companion.TYPE
import am.a_t.dailyapp.databinding.DialogNewListBinding
import am.a_t.dailyapp.databinding.FragmentMainBinding
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.presentation.adapter.TaskAdapter
import am.a_t.dailyapp.presentation.adapter.TodoAdapter
import am.a_t.dailyapp.utils.ListColor
import am.a_t.dailyapp.utils.ListType
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import kotlin.collections.ArrayList


@AndroidEntryPoint
class MainFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var taskAdapter: TaskAdapter
    private val preference: Preference by lazy { Preference(requireContext()) }
    private lateinit var myDialog: DialogNewListBinding
    private lateinit var alertDialog: AlertDialog
    private val calendar = Calendar.getInstance()
    private var listTask = emptyList<Task>()
    private var listTodo = emptyList<Todo>()
    private val formatterDate = SimpleDateFormat("MM-dd-yyyy", Locale.US)
    private var isCreate = false
    private lateinit var snackbar: Snackbar
    private val args: MainFragmentArgs by navArgs()

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

            if (args.create == 2) {
                createCustomSnackbar(R.layout.snackbar_warning)
            } else if (args.create == 1) {
                createCustomSnackbar(R.layout.snackbar_success)
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

        lifecycleScope.launch {
            viewModel.filterTodo.collectLatest {
                todoAdapter.submitList(it)
            }
        }

        lifecycleScope.launch {
            viewModel.filterTask.collectLatest {
                taskAdapter.submitList(it)
            }
        }

        lifecycleScope.launch {
            viewModel.todoAllLiveData.first().collectLatest {
                listTodo = it
                todoAdapter.submitList(it)
            }
        }

        lifecycleScope.launch {
            viewModel.taskAllLiveData.first().collectLatest {
                listTask = it
                taskAdapter.submitList(it)
            }
        }
    }

    private fun initClickListeners(inflater: LayoutInflater, container: ViewGroup?) {
        with(binding) {

            btnCancelFilter.setOnClickListener {
                cancelFilter()
            }

            btnFilterDate.setOnClickListener {
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
                initDialog(inflater, container)
            } else {
                findNavController().navigate(R.id.action_mainFragment_to_createNewTaskFragment)
            }
        }
    }

    private fun cancelFilter() {
        lifecycleScope.launch {

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

    private fun initDialog(inflater: LayoutInflater, container: ViewGroup?) {
        myDialog = DialogNewListBinding.inflate(inflater, container, false)
        with(myDialog) {
            alertDialog = AlertDialog.Builder(requireContext())
                .setView(root)
                .show()

            dialogTodoButtonClickListeners(myDialog, alertDialog)

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

    private fun addTodo(itemColor: ListColor) {
        with(myDialog) {
            isCreate = if (edTodoName.text.toString().isNotEmpty()) {
                viewModel.addTodo(
                    Todo(
                        0,
                        false,
                        edTodoName.text.toString(),
                        getCustomDateString("MM-dd-yyyy"),
                        itemColor
                    )
                )
                binding.tvFilterDate.setText(R.string.set_filter)
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
        snackbar = Snackbar.make(binding.viewBack, "Custom", Snackbar.LENGTH_LONG).apply {
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
        displayFormattedDate(calendar.timeInMillis)
    }

    private fun displayFormattedDate(timeInMillis: Long) {
        lifecycleScope.launch {
            with(binding) {
                tvFilterDate.text = formatterDate.format(timeInMillis)
                filterTaskOrTodo(listTask, listTodo, formatterDate.format(timeInMillis))
            }
        }
    }

    private suspend fun filterTaskOrTodo(listTask: List<Task>, listTodo: List<Todo>, date: String) {
        when (preference.readType(TYPE)) {
            ListType.TODOS.typeName -> {
                viewModel.getTodoFilter(listTodo, date)
            }
            ListType.TASKS.typeName -> {
                viewModel.getTaskFilter(listTask, date)
            }
        }
    }
}