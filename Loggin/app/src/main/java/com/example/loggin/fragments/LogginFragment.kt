package com.example.loggin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.loggin.R

class LogginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.loggin_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnRegistrarse = view.findViewById<Button>(R.id.button2)
        val tvOlvidoContrasena = view.findViewById<TextView>(R.id.olvido_contrasena)

        btnRegistrarse.setOnClickListener {
            view.findNavController().navigate(R.id.action_logginFragment_to_registroFragment)
        }

        tvOlvidoContrasena.setOnClickListener {
            view.findNavController().navigate(R.id.action_logginFragment_to_recuperarContrasenaFragment)
        }
    }
}