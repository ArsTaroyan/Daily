package am.a_t.dailyapp.presentation.ui.mainFragment

import am.a_t.dailyapp.R
import am.a_t.dailyapp.presentation.adapter.TodoAdapter
import am.a_t.dailyapp.databinding.DialogNewListBinding
import am.a_t.dailyapp.databinding.FragmentMainBinding
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.utils.ListColor
import android.app.AlertDialog
import android.graphics.drawable.Drawable
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var myDialog: DialogNewListBinding
    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        initAdapter()
        initViewModel()
        initClickListeners(inflater, container)
        return binding.root
    }

    private fun initViewModel() {
        viewModel.getAllTodo()
        lifecycleScope.launch {
            viewModel.todoAllLiveData.first().collectLatest {
                todoAdapter.submitList(it)
            }
        }
    }

    private fun initDialog(inflater: LayoutInflater, container: ViewGroup?) {
        myDialog = DialogNewListBinding.inflate(inflater, container, false)
        alertDialog = AlertDialog.Builder(requireContext())
            .setView(myDialog.root)
            .show()


        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        setSingleChoiceClickListener(myDialog.colorBlueList, R.drawable.btn_blue_checked, R.drawable.btn_blue)
        setSingleChoiceClickListener(myDialog.colorCoralList, R.drawable.btn_red_checked, R.drawable.btn_red)
        setSingleChoiceClickListener(myDialog.colorOrangeList, R.drawable.btn_orange_checked, R.drawable.btn_orange)
        setSingleChoiceClickListener(myDialog.colorPurpleList, R.drawable.btn_purple_checked, R.drawable.btn_purple)

        //singleChoice(myDialog)

        myDialog.btnCreateList.setOnClickListener {
            viewModel.addTodo(
                Todo(
                    0,
                    false,
                    myDialog.etTodoName.text.toString(),
                    "",
                    ListColor.PURPLE
                )
            )
            alertDialog.dismiss()
        }

    }

    var selectedView: View? = null

    fun setSingleChoiceClickListener(view: View, drawableResource: Int, closedDrawRes: Int) {
        view.setOnClickListener {
            // Check if this view is already selected; if so, do nothing
            if (view != selectedView) {
                // Remove the background from the previously selected view
                selectedView?.setBackgroundResource(closedDrawRes)

                // Set the background for the newly selected view
                view.setBackgroundResource(drawableResource)

                // Update the selected view reference
               // selectedView = view
            }
        }
    }

// Usage:



//    private fun singleChoice(myDialog: DialogNewListBinding) {
//        with(myDialog){
//            colorBlueList.setOnClickListener {
//                colorBlueList.setBackgroundResource(R.drawable.btn_blue_checked)
//            }
//            colorCoralList.setOnClickListener {
//                colorCoralList.setBackgroundResource(R.drawable.btn_red_checked)
//            }
//            colorOrangeList.setOnClickListener {
//                colorOrangeList.setBackgroundResource(R.drawable.btn_orange_checked)
//            }
//            colorPurpleList.setOnClickListener {
//                colorPurpleList.setBackgroundResource(R.drawable.btn_purple_checked)
//            }
//        }
//    }


    private fun initAdapter() {
        todoAdapter = TodoAdapter(viewModel) {
            viewModel.removeTodo(it)
        }
        binding.rvTodo.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTodo.adapter = todoAdapter
    }

    private fun initClickListeners(inflater: LayoutInflater, container: ViewGroup?) {
        binding.btnLeft.setOnClickListener {
            initDialog(inflater, container)
        }
    }

}