package com.test.spacexlaunches.ui.details

import io.reactivex.Completable
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by alex-balandin on 2019-11-25
 */
@Singleton
class FileDownloadHelper @Inject constructor() {

    fun downloadAndSave(sourceUrl: String, targetPath: String): Completable {
        return Completable.create { emitter ->
            val targetDir = File(targetPath)
            if (!targetDir.exists()) {
                targetDir.mkdirs()
            }
            val parts = sourceUrl.split("/")
            val fileName = parts[parts.size - 1]

            val targetFile = File(targetDir, fileName)

            var inputStream: BufferedInputStream? = null
            var outputStream: ByteArrayOutputStream? = null
            var fileOutputStream: FileOutputStream? = null
            try {
                val url = URL(sourceUrl)
                inputStream = BufferedInputStream(url.openStream())
                outputStream = ByteArrayOutputStream()
                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }
                val byteArray = outputStream.toByteArray()

                fileOutputStream = FileOutputStream(targetFile)
                fileOutputStream.write(byteArray)

            } catch (e: Exception) {
                emitter.onError(e)

            } finally {
                outputStream?.close()
                inputStream?.close()
                fileOutputStream?.close()
            }

            emitter.onComplete()
        }
    }
}