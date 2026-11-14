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
import com.example.loggin.databinding.FragmentRegistroBinding
import com.example.loggin.viewmodels.RegistroUiEvent
import com.example.loggin.viewmodels.RegistroViewModel
import kotlinx.coroutines.launch

class RegistroFragment : Fragment() {

    private var _binding: FragmentRegistroBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegistroViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        binding.tilUsername.error = state.usernameError?.let { getString(it) }
                        binding.tilEmail.error = state.emailError?.let { getString(it) }
                        binding.tilPassword.error = state.passwordError?.let { getString(it) }
                        binding.tilConfirmPassword.error = state.confirmPasswordError?.let { getString(it) }
                        binding.btnRegistrarse.isEnabled = state.isRegistroEnabled && !state.isLoading
                    }
                }

                launch {
                    viewModel.uiEvent.collect { event ->
                        when (event) {
                            is RegistroUiEvent.NavigateBackToLogin -> {
                                Toast.makeText(requireContext(), getString(R.string.registro_exitoso), Toast.LENGTH_SHORT).show()
                                view.findNavController().navigateUp()
                            }
                            is RegistroUiEvent.ShowError -> {
                                Toast.makeText(requireContext(), getString(event.messageResId), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        binding.nombreUsuario.doOnTextChanged { text, _, _, _ ->
            viewModel.onUsernameChanged(text.toString())
        }

        binding.emailRegistro.doOnTextChanged { text, _, _, _ ->
            viewModel.onEmailChanged(text.toString())
        }

        binding.passwordRegistro.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordChanged(text.toString())
        }

        binding.passwordConfirmar.doOnTextChanged { text, _, _, _ ->
            viewModel.onConfirmPasswordChanged(text.toString())
        }

        binding.btnRegistrarse.setOnClickListener {
            viewModel.onRegistroClicked()
        }

        binding.btnVolverRegistro.setOnClickListener {
            view.findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}