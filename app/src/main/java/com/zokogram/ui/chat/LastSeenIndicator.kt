package com.zokogram.ui.chat

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LastSeenIndicator(
    lastSeenTimestamp: Long?,
    modifier: Modifier = Modifier
) {
    val text = if (lastSeenTimestamp == null || lastSeenTimestamp == 0L) {
        "Был(а) давно"
    } else {
        val dateFormatted = SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault())
            .format(Date(lastSeenTimestamp))
        "Был(а) в сети: $dateFormatted"
    }

    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}
