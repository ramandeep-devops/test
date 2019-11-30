package com.codebrew.encober.utils

import androidx.core.util.PatternsCompat
import java.util.regex.Pattern

object ValidationUtils {

    // Alphanumeric, Underscore, Period and Hyphen only. This will accept whitespaces.

    private val USERNAME_PATTERN by lazy { Pattern.compile("^(?=.*[a-zA-Z0-9])[ \\w_.-]*$") }

    fun isEmailValid(email: String) = PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()

    fun isPasswordLengthValid(password: String) = password.length in 6..20

    fun isPhoneNumberLengthValid(phoneNumber: String) = phoneNumber.length in 5..15

    fun isOtpValid(otp: String) = otp.length == 4

    fun isAccountNumberValid(accountNo: String) = accountNo.length == 18

    fun isUsernameLengthValid(username: String) = username.length >= 6

    fun isUsernameCharactersValid(username: String) =
        USERNAME_PATTERN.matcher(username).matches() && !username.contains(" ")
}