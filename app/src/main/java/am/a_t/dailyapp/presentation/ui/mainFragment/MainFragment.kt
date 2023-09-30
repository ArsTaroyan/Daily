package am.a_t.dailyapp.presentation.ui.mainFragment

import am.a_t.dailyapp.R
import am.a_t.dailyapp.data.preferences.Preference
import am.a_t.dailyapp.data.preferences.Preference.Companion.TYPE
import am.a_t.dailyapp.databinding.DialogNewListBinding
import am.a_t.dailyapp.databinding.FragmentMainBinding
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.presentation.adapter.TaskAdapter
import am.a_t.dailyapp.presentation.adapter.TodoAdapter
import am.a_t.dailyapp.utils.ListColor
import am.a_t.dailyapp.utils.ListType
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var taskAdapter: TaskAdapter
    private val preference: Preference by lazy { Preference(requireContext()) }
    private lateinit var myDialog: DialogNewListBinding
    private lateinit var alertDialog: AlertDialog
//    private lateinit var snackbar: Snackbar

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
            if (preference.readType(TYPE).isNullOrEmpty()) {
                binding.btnRight.text = ListType.TODOS.typeName
                recyclerViewAdapt(ListType.TODOS)
            } else {
                binding.btnRight.text = preference.readType(TYPE)

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

    private fun initViewModel() {
        viewModel.getAllTodo()
        viewModel.getAllTask()

        lifecycleScope.launch {
            viewModel.todoAllLiveData.first().collectLatest {
                todoAdapter.submitList(it)
            }
        }

        lifecycleScope.launch {
            viewModel.taskAllLiveData.first().collectLatest {
                taskAdapter.submitList(it)
            }
        }
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
            colorPurpleList.setOnClickListener {
                itemColor = ListColor.PURPLE
            }

            colorRedList.setOnClickListener {
                itemColor = ListColor.RED
            }

            colorBlueList.setOnClickListener {
                itemColor = ListColor.BLUE
            }

            colorOrangeList.setOnClickListener {
                itemColor = ListColor.ORANGE
            }

            btnCreateList.setOnClickListener {
                if (etTodoName.text.toString().isNotEmpty()) {
                    viewModel.addTodo(
                        Todo(
                            0,
                            false,
                            etTodoName.text.toString(),
                            getCustomDateAndTimeString(),
                            itemColor
                        )
                    )
                    alertDialog.dismiss()
                }
            }

            cancelNewList.setOnClickListener {
                alertDialog.dismiss()
            }
        }
    }

    private fun getCustomDateAndTimeString(): String {
        val pattern = "MM-dd-yyyy hh:mm:ss"
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.format(formatter)
    }

//    private fun createCustomSnackbar(@LayoutRes layoutRes: Int, button: AppCompatTextView) {
//        snackbar = Snackbar.make(button, "Custom", Snackbar.LENGTH_LONG).apply {
//            setAction("Retry") {
//                dismiss()
//            }
//            view.setBackgroundColor(Color.TRANSPARENT)
//            val view = layoutInflater.inflate(layoutRes, null)
//            (this.view as Snackbar.SnackbarLayout).addView(view)
//            show()
//        }
//
//    }

    private fun initAdapter(inflater: LayoutInflater, container: ViewGroup?) {
        todoAdapter = TodoAdapter(requireContext(), inflater, container, viewModel) {

        }
        taskAdapter = TaskAdapter(requireContext(), inflater, container, viewModel) {

        }
    }

    private fun recyclerViewAdapt(type: ListType) {
        if (type == ListType.TODOS) {
            binding.rvTodo.layoutManager = LinearLayoutManager(requireContext())
            binding.rvTodo.adapter = todoAdapter
        } else {
            binding.rvTodo.layoutManager = LinearLayoutManager(requireContext())
            binding.rvTodo.adapter = taskAdapter
        }
    }

    private fun initClickListeners(inflater: LayoutInflater, container: ViewGroup?) {
        with(binding) {
            btnLeft.setOnClickListener {
                if (btnRight.text.toString() == ListType.TODOS.typeName) {
                    initDialog(inflater, container)
                } else {
                    findNavController().navigate(R.id.action_mainFragment_to_createNewTaskFragment)
                }
            }

            btnRight.setOnClickListener {
                lifecycleScope.launch(Dispatchers.Main) {
                    if (btnRight.text.toString() == ListType.TODOS.typeName) {
                        saveType(ListType.TASKS.typeName, ListType.TASKS)
                    } else {
                        saveType(ListType.TODOS.typeName, ListType.TODOS)
                    }
                }
            }
        }
    }

    private suspend fun saveType(typeName: String, type: ListType) {

        if (!preference.readType(TYPE).isNullOrEmpty()) {
            preference.removeType(TYPE)
        }
        preference.saveType(TYPE, typeName)
        binding.btnRight.text = typeName

        recyclerViewAdapt(type)

    }
}