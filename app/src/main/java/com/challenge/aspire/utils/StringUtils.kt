package com.challenge.aspire.utils

object StringUtils {

    fun isBlank(content: String?): Boolean {
        return content == null || content.isEmpty() || "" == content
    }

    fun isNotEmpty(content: String?): Boolean {
        return !isBlank(content)
    }

}