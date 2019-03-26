package com.challenge.aspire.utils

import android.content.Context
import com.challenge.aspire.R
import com.challenge.aspire.utils.extension.validWithPattern


object ValidateUtils {

    private const val NAME_MINIMUM_CHARACTER = 1
    private const val EMAIL_MINIMUM_CHARACTER = 3
    private const val PASSWORD_MINIMUM_CHARACTER = 8

    private val EMAIL_PATTERN = android.util.Patterns.EMAIL_ADDRESS
    //At least one number, one character, no special character
    private const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+){8,100}$"


    fun validateName(name: String, context: Context): String {
        if (name.isBlank()) {
            return context.getString(R.string.err_msg_name_empty)
        }
        if (name.length < NAME_MINIMUM_CHARACTER) {
            return String.format(context.getString(R.string.err_msg_less_than_character),
                    NAME_MINIMUM_CHARACTER)
        }
        return ""
    }

    fun validateEmail(email: String, context: Context): String {
        if (email.isBlank()) {
            return context.getString(R.string.err_msg_email_empty)
        }
        if (email.length < EMAIL_MINIMUM_CHARACTER) {
            return String.format(context.getString(R.string.err_msg_less_than_character),
                    EMAIL_MINIMUM_CHARACTER)
        }
        if (!email.validWithPattern(EMAIL_PATTERN)) {
            return context.getString(R.string.err_msg_please_enter_email_correct)
        }
        return ""
    }

    fun validatePassword(password: String, context: Context): String {
        if (password.isBlank()) {
            return context.getString(R.string.err_msg_password_empty)
        }
        if (password.length < PASSWORD_MINIMUM_CHARACTER) {
            return String.format(context.getString(R.string.err_msg_less_than_character),
                    PASSWORD_MINIMUM_CHARACTER)
        }
        if (!password.validWithPattern(PASSWORD_REGEX)) {
            return context.getString(
                    R.string.err_msg_please_enter_less_than_one_character_and_one_number)
        }
        return ""
    }

    fun validateConfirmPassword(confirmPassword: String, context: Context): String {
        if (confirmPassword.isBlank()) {
            return context.getString(R.string.err_msg_confirm_password_empty)
        }
        if (confirmPassword.length < PASSWORD_MINIMUM_CHARACTER) {
            return String.format(context.getString(R.string.err_msg_less_than_character),
                    PASSWORD_MINIMUM_CHARACTER)
        }
        if (!confirmPassword.validWithPattern(PASSWORD_REGEX)) {
            return context.getString(
                    R.string.err_msg_please_enter_less_than_one_character_and_one_number)
        }
        return ""
    }
}