package com.jeeps.laboratorioutpl.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class InvalidAccessDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alerta")
                .setMessage("No tienes permiso para entrar a esta sala en este momento")
                .setPositiveButton("Aceptar", (dialog, which) -> {});
        return builder.create();
    }
}
