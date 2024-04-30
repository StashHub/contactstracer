package com.drgayno.contactstracer.db.export

import com.drgayno.contactstracer.db.DatabaseRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.supercsv.io.CsvListWriter
import org.supercsv.prefs.CsvPreference
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset

class CsvExporter(private val repository: DatabaseRepository) {

    companion object {
        // represents the structure of the csv file
        val HEADERS: Array<String> = arrayOf(
            "Track Unique ID",
            "Duration (mm:ss)",
            "Average RSSI",
            "Median RSSI"
        )
    }

    fun export(): Single<ByteArray> {
        val stream = ByteArrayOutputStream()
        val csvWriter = CsvListWriter(
            OutputStreamWriter(stream, Charset.forName("utf-8")),
            CsvPreference.STANDARD_PREFERENCE
        )

        return repository.getAll()
            .subscribeOn(Schedulers.io())
            .map { entities ->
                csvWriter.use { writer ->
                    // write metadata
                    writer.writeHeader(*HEADERS)

                    // write entities
                    entities.forEach { entity ->
                        val mills = entity.timestampEnd - entity.timestampStart
                        val minutes = (mills / (1000 * 60)).toInt() % 60
                        val seconds = (mills / 1000).toInt() % 60.toLong()
                        writer.write(
                            entity.tuid,
                            "$minutes:$seconds",
                            entity.rssiAvg,
                            entity.rssiMed
                        )
                    }
                }
            }
            .map { stream.toByteArray() }
            .observeOn(AndroidSchedulers.mainThread())
    }
}
