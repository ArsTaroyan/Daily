package am.a_t.dailyapp.presentation.ui.todoFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import am.a_t.dailyapp.R
import am.a_t.dailyapp.databinding.DialogNewListBinding
import am.a_t.dailyapp.databinding.FragmentTodoBinding
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.domain.utils.ListColor
import am.a_t.dailyapp.presentation.adapter.TodoAdapter
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.widget.DatePicker
import androidx.annotation.LayoutRes
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

@AndroidEntryPoint
class TodoFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private val args by navArgs<TodoFragmentArgs>()
    private lateinit var binding: FragmentTodoBinding
    private val mViewModel: TodoViewModel by viewModels()
    private lateinit var snackBar: Snackbar
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var alertDialog: AlertDialog
    private lateinit var myDialogTodo: DialogNewListBinding
    private var isCreate = false
    private var isFilter = false
    private var todoList = emptyList<Todo>()
    private var filterFromDate: String? = null
    private val calendar = Calendar.getInstance()
    private var itemColor: ListColor = ListColor.RED
    private val formatterDate = SimpleDateFormat("MM-dd-yyyy", Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoBinding.inflate(inflater, container, false)

        initView()
        initAdapter(inflater, container)
        initViewModel()
        initClickListeners(inflater, container)

        return binding.root
    }

    //init View

    private fun initView() {
        lifecycleScope.launch(Dispatchers.Main) {
            with(binding) {
                tvFilterDate.setText(R.string.set_filter)
            }
        }
    }

    private fun cancelFilter() {
        mViewModel.getTodoFilter(todoList, null)
    }

    private fun filterTaskOrListTodo(date: String) {
        filterFromDate = date
        mViewModel.getTodoFilter(todoList, date)
    }

    // init Adapter

    private fun initAdapter(inflater: LayoutInflater, container: ViewGroup?) {
        todoAdapter =
            TodoAdapter(requireContext(), inflater, container, mViewModel) { isDelete, it ->
                if (isDelete) {
                    createCustomSnackBar(R.layout.snackbar_success_delete)
                } else {

                }
            }
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.adapter = todoAdapter
        mViewModel.getTodoFilter(todoList, null)
        binding.tvFilterDate.setText(R.string.set_filter)
    }

    // init ViewModel

    private fun initViewModel() {
        mViewModel.getAllTodo(args.listTodoId)

        lifecycleScope.launch {
            mViewModel.filterTodo.collectLatest {
                todoAdapter.submitList(it)
            }
        }

        lifecycleScope.launch {
            mViewModel.todoAllLiveData.first().collectLatest {
                todoList = it
                if (!isFilter) {
                    todoAdapter.submitList(it)
                } else {
                    mViewModel.getTodoFilter(it, filterFromDate)
                }
            }
        }
    }

    private fun getCustomDateString(pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.format(formatter)
    }

    // init Click

    private fun initClickListeners(inflater: LayoutInflater, container: ViewGroup?) {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigate(R.id.action_todoFragment_to_mainFragment)
            }

            btnFilterDate.setOnClickListener {
                showDatePicker()
            }

            btnCancelFilter.setOnClickListener {
                cancelFilter()
            }

            btnAdd.setOnClickListener {
                initDialogListTodo(inflater, container)
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

    // Dialog Date

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
        displayFormattedDate(calendar.timeInMillis)
    }

    private fun displayFormattedDate(timeInMillis: Long) {
        with(binding) {
            tvFilterDate.text = formatterDate.format(timeInMillis)
            filterTaskOrListTodo(formatterDate.format(timeInMillis))
        }
    }

    // Dialog ListTodos

    private fun initDialogListTodo(inflater: LayoutInflater, container: ViewGroup?) {
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
                addTodo()

                if (isCreate) {
                    createCustomSnackBar(R.layout.snackbar_warning)
                } else {
                    createCustomSnackBar(R.layout.snackbar_success)
                }
            }

            btnCancelNewList.setOnClickListener {
                alertDialog.dismiss()
            }
        }
    }

    private fun addTodo() {
        with(myDialogTodo) {
            isCreate = if (edTodoName.text.toString().isNotEmpty()) {
                mViewModel.addTodo(
                    Todo(
                        0,
                        args.listTodoId,
                        false,
                        edTodoName.text.toString(),
                        getCustomDateString("MM-dd-yyyy"),
                        itemColor
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
}