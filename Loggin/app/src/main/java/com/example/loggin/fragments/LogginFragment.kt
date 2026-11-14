package com.example.loggin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.example.loggin.R
import com.example.loggin.databinding.LogginFragmentBinding
import com.example.loggin.viewmodels.LoginUiEvent
import com.example.loggin.viewmodels.LoginViewModel
import kotlinx.coroutines.launch

class LogginFragment : Fragment() {

    private var _binding: LogginFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LogginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            view.findNavController().navigateUp()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.uiState.collect { state ->
                        binding.emailLayout.error = state.emailError?.let { getString(it) }
                        binding.LayoutPassword.error = state.passwordError?.let { getString(it) }
                        binding.button.isEnabled = state.isLoginEnabled && !state.isLoading
                    }
                }

                launch {
                    viewModel.uiEvent.collect { event ->
                        when (event) {
                            is LoginUiEvent.NavigateToHome -> {
                                view.findNavController().navigate(R.id.action_logginFragment_to_homeFragment)
                            }
                            is LoginUiEvent.ShowError -> {
                                Toast.makeText(requireContext(), getString(event.messageResId), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        binding.editTextTextEmailAddress.doOnTextChanged { text, _, _, _ ->
            viewModel.onEmailChanged(text.toString())
        }

        binding.editTextTextPasswordAddress.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordChanged(text.toString())
        }

        binding.button.setOnClickListener {
            viewModel.onLoginClicked()
        }

        binding.button2.setOnClickListener {
            view.findNavController().navigate(R.id.action_logginFragment_to_registroFragment)
        }

        binding.olvidoContrasena.setOnClickListener {
            view.findNavController().navigate(R.id.action_logginFragment_to_recuperarContrasenaFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}