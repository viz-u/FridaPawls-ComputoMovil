package mx.edu.itesca.fridapawls_2026.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtils {

    /**
     * Convierte timestamp (Long) a hora legible tipo "10:30 AM"
     */
    fun formatTime(time: Long): String {
        if (time <= 0) return ""

        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(time))
    }

    /**
     * Devuelve timestamp actual
     */
    fun now(): Long {
        return System.currentTimeMillis()
    }

    /**
     * Convierte timestamp a formato corto tipo "Hoy", "Ayer", etc (básico)
     */
    fun formatRelative(time: Long): String {
        if (time <= 0) return ""

        val now = System.currentTimeMillis()
        val diff = now - time

        val oneDay = 24 * 60 * 60 * 1000

        return when {
            diff < oneDay -> "Hoy"
            diff < 2 * oneDay -> "Ayer"
            else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(Date(time))
        }
    }
}