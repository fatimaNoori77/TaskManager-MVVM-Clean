package ir.noori.taskmanager.presentation.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.noori.taskmanager.R
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nav = findNavController()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                splashViewModel.events.collect { event ->
                    when (event) {
                        SplashEvent.NavigateToLogin -> {
                            nav.navigate("login_fragment") {
                                popUpTo(R.id.splashFragment) {
                                    inclusive = true
                                }
                            }
                        }
                        is SplashEvent.NavigateToHome -> {
                            nav.navigate( "task_list_fragment") {
                                popUpTo(R.id.splashFragment) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                splashViewModel.uiState.collect { state ->
                  when(state){
                      is SplashUiState.Authenticated -> TODO()
                      SplashUiState.Loading -> TODO()
                      SplashUiState.Unauthenticated -> TODO()
                  }
                }
            }
        }

    }
}