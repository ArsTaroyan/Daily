package am.a_t.dailyapp.presentation.ui.createNewTask

import am.a_t.dailyapp.R
import am.a_t.dailyapp.databinding.FragmentCreateNewTaskBinding
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.utils.ListColor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateNewTaskFragment : Fragment() {
    private lateinit var binding: FragmentCreateNewTaskBinding
    private val viewModel: CreateNewTaskViewModel by viewModels()
    private var itemColor: ListColor? = null

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
                if (etTaskName.text.toString().isNotEmpty() &&
                    itemColor != null &&
                    etDescriptionName.text.toString().isNotEmpty()
                ) {
                    if (btnRemind.isChecked && taskTime.text.toString().isNotEmpty() && taskDate.text.toString().isNotEmpty()) {
                        viewModel.addTask(
                            Task(
                                0,
                                taskTime.text.toString(),
                                etTaskName.text.toString(),
                                taskDate.text.toString(),
                                itemColor,
                                etDescriptionName.text.toString()
                            )
                        )
                    } else if(!btnRemind.isChecked) {
                        viewModel.addTask(
                            Task(
                                0,
                                null,
                                etTaskName.text.toString(),
                                null,
                                itemColor,
                                etDescriptionName.text.toString()
                            )
                        )
                    }
                    findNavController().navigate(R.id.action_createNewTaskFragment_to_mainFragment)
                }
            }
        }
    }
}