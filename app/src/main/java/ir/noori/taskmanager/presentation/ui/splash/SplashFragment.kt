package ir.noori.taskmanager.presentation.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.noori.taskmanager.R

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val splashViewModel: SplashViewModel by viewModels()
    private val nav by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            splashViewModel.events.collect { event ->
                when(event){
                    SplashViewModel.SplashEvent.NavigationToLogin -> {
                        nav.navigate(R.id.loginFragment) {
                            popUpTo(R.id.splashFragment) {
                                inclusive = true
                            }
                        }
                    }
                    is SplashViewModel.SplashEvent.NavigateToHome -> {
                        nav.navigate(R.id.taskListFragment) {
                            popUpTo(R.id.splashFragment) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

}