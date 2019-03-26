package com.challenge.aspire.data.model

import android.net.Uri
import java.io.File

data class Image (
        var name: String? = null,
        var uri: Uri? = null,
        var file: File? = null
)