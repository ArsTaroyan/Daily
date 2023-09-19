package am.a_t.dailyapp.presentation.ui.createNewTask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import am.a_t.dailyapp.databinding.FragmentCreateNewTaskBinding
import androidx.fragment.app.viewModels

class CreateNewTaskFragment : Fragment() {
    private lateinit var binding: FragmentCreateNewTaskBinding
    private val viewModel: CreateNewTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNewTaskBinding.inflate(inflater, container, false)

        return binding.root
    }

}