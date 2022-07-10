package com.legolax.salavip;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogoAlerta extends AppCompatDialogFragment {

    private EditText editTextPassword;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        editTextPassword = view.findViewById(R.id.passId);

        //alerta de dialogo para acceder a la screen de gameManager (MainActivity2)
        builder.setView(view).setTitle("VIP Administrador").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if ( editTextPassword.getText().toString().equals("43028")){
                    Intent siguientePantalla = new Intent(getContext(), MainActivity2.class);
                    startActivity(siguientePantalla);
                }else{
                    Toast.makeText(getContext(), "Contraseña Incorrecta", Toast.LENGTH_SHORT ).show();
                }

            }
        });



        return  builder.create();
    }

}
