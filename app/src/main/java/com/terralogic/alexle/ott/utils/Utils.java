package com.terralogic.alexle.ott.utils;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

/**
 * Created by alex.le on 01-Aug-17.
 */

public final class Utils {
    public static boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isRequiredFieldsFilled(EditText... fields) {
        for (EditText field : fields) {
            if (TextUtils.isEmpty(field.getText())) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidPassword(EditText fieldPassword, EditText fieldConfirmPassword) {
        return fieldPassword.getText().toString().equals(fieldConfirmPassword.getText().toString());
    }
}
