package ir.noori.taskmanager.presentation.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                splashViewModel.events.collect { event ->
                    when (event) {
                        SplashEvent.NavigateToLogin -> {
                            nav.navigate(R.id.action_splashFragment_to_loginFragment) {
                                popUpTo(R.id.splashFragment) {
                                    inclusive = true
                                }
                            }
                        }
                        is SplashEvent.NavigateToHome -> {
                            nav.navigate(R.id.action_splashFragment_to_taskListFragment) {
                                popUpTo(R.id.splashFragment) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
//            }
        }
    }
}