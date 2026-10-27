package com.example.loggin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.loggin.R

class RecuperarContrasenaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recuperar_contrasena, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnVolver = view.findViewById<Button>(R.id.btn_volver_recuperar)

        // Vuelve a la pantalla anterior del flujo (Login)
        btnVolver.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}