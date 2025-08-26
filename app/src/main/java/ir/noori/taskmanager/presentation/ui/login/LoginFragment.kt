package ir.noori.taskmanager.presentation.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.noori.taskmanager.databinding.FragmentLoginBinding
import ir.noori.taskmanager.domain.model.LoginInput
import ir.noori.taskmanager.presentation.viewmodel.LoginState
import ir.noori.taskmanager.presentation.viewmodel.LoginViewModel

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
            val inputs = LoginInput(
                username,
                password
            )
            loginViewModel.login(inputs)
        }
    }

    private fun observers(){
        loginViewModel.loginResponse.observe(viewLifecycleOwner){state ->
            when(state){
                is LoginState.Error -> {
                    // show error message
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
                LoginState.Loading -> {
                    // show loading
                    Toast.makeText(context, "loading", Toast.LENGTH_SHORT).show()
                }
                is LoginState.Success -> {
                    // navigate to task list
                    navigateToDirectionFragment()
                }
            }
        }
    }

    fun navigateToDirectionFragment() {
        Log.i("TAG", "navigateToDirectionFragment: ")
//        childFragmentManager
//            .beginTransaction()
//            .replace(R.id.fragment_container,LoginFragment())
//            .addToBackStack(null)
//            .commit()
    }


}