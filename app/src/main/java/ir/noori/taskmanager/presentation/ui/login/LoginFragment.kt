package ir.noori.taskmanager.presentation.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.noori.taskmanager.R
import ir.noori.taskmanager.databinding.FragmentLoginBinding
import ir.noori.taskmanager.domain.model.LoginRequest

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        observers()
    }

    private fun initBinding() {
        binding.enterButton.setOnClickListener {
            val username = binding.editUsername.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()

            loginViewModel.login(LoginRequest(username, password))
        }
    }

    private fun observers(){
        loginViewModel.loginResponse.observe(viewLifecycleOwner){state ->
            binding.progressBar.visibility = View.GONE
            when(state){
                is LoginState.Error -> {
                    // show error message
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
                LoginState.Loading -> {
                    // show loading
                    binding.progressBar.visibility = View.VISIBLE
                }
                is LoginState.Success -> {
                    // navigate to task list
                    navigateToDirectionFragment()
                }
            }
        }
    }

    fun navigateToDirectionFragment() {
        findNavController().navigate("task_list_fragment") {
            popUpTo(R.id.loginFragment) {
                inclusive = true
            }
        }
    }

}