package com.catbro.weather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AlertDialogFragment extends DialogFragment
{
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                            .setTitle(getString(R.string.error_title))
                            .setMessage(getString(R.string.error_message))
                            .setPositiveButton(getString(R.string.error_btn_text),null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
