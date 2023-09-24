package am.a_t.dailyapp.presentation.ui.createNewTask

import am.a_t.dailyapp.R
import am.a_t.dailyapp.databinding.FragmentCreateNewTaskBinding
import am.a_t.dailyapp.presentation.ui.mainFragment.MainViewModel
import am.a_t.dailyapp.utils.ListColor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

class CreateNewTaskFragment : Fragment() {
    private lateinit var binding: FragmentCreateNewTaskBinding
    private val viewModel: CreateNewTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNewTaskBinding.inflate(inflater, container, false)

        initClickListeners()
        return binding.root
    }



    private fun initClickListeners() {
        with(binding) {
            backNewTask.setOnClickListener {
                findNavController().navigate(R.id.action_createNewTaskFragment_to_mainFragment)
            }

            var itemColor: ListColor? = null

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

            if (etTaskName.text.toString().isNotEmpty() && itemColor != null) {
            }
        }

    }
}