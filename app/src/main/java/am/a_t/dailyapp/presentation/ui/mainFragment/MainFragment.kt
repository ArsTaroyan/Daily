package am.a_t.dailyapp.presentation.ui.mainFragment

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
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var myDialog: DialogNewListBinding
    private lateinit var alertDialog: AlertDialog

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

//    private fun initAdapterTask(inflater: LayoutInflater, container: ViewGroup?) {
//        taskAdapter = TaskAdapter(requireContext(), inflater, container, viewModel) {
//
//        }
//        binding.rvTodo.layoutManager = LinearLayoutManager(requireContext())
//        binding.rvTodo.adapter = taskAdapter
//    }

    private fun initView() {
        binding.btnRight.text = ListType.TODOS.typeName
    }

    private fun initViewModel() {
        recyclerViewAdapt(ListType.TODOS)
        viewModel.getAllTodo()

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

            dialogButtonClickListeners(myDialog, alertDialog)

            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }


    private fun dialogButtonClickListeners(
        myDialog: DialogNewListBinding,
        alertDialog: AlertDialog
    ) {
        var itemColor: ListColor? = null
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
                if (etTodoName.text.toString().isNotEmpty() && itemColor != null) {
                    viewModel.addTodo(
                        Todo(
                            0,
                            false,
                            etTodoName.text.toString(),
                            "",
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
                initDialog(inflater, container)
            }

            btnRight.setOnClickListener {
                if (btnRight.text.toString() == ListType.TODOS.typeName) {
                    btnRight.text = ListType.TASKS.typeName
                    recyclerViewAdapt(ListType.TASKS)
                } else {
                    btnRight.text = ListType.TODOS.typeName
                    recyclerViewAdapt(ListType.TODOS)
                }
            }
        }
    }

}