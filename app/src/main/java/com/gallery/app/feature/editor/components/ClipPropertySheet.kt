package com.gallery.app.feature.editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gallery.app.core.domain.model.editor.ColorAdjustmentConfig
import com.gallery.app.core.domain.model.editor.EditorClip
import com.gallery.app.core.domain.model.editor.VideoFilterType

@Composable
fun ClipPropertySheet(
    selectedClip: EditorClip?,
    onVolumeChange: (Float) -> Unit,
    onSpeedChange: (Float) -> Unit,
    onFilterChange: (VideoFilterType) -> Unit,
    onColorAdjustmentsChange: (ColorAdjustmentConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    if (selectedClip == null) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E24))
            .padding(12.dp)
    ) {
        Text(
            text = "Clip Inspector (${selectedClip.type})",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // Filter chips list
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VideoFilterType.values().forEach { filter ->
                FilterChip(
                    selected = selectedClip.filterType == filter,
                    onClick = { onFilterChange(filter) },
                    label = { Text(filter.name.replace("_", " ")) }
                )
            }
        }

        // Volume Control Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Volume: ${(selectedClip.volume * 100).toInt()}%",
                color = Color.LightGray,
                fontSize = 12.sp,
                modifier = Modifier.width(90.dp)
            )
            Slider(
                value = selectedClip.volume,
                onValueChange = onVolumeChange,
                valueRange = 0f..1f,
                modifier = Modifier.weight(1f)
            )
        }

        // Speed Control Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Speed: ${"%.1f".format(selectedClip.speed)}x",
                color = Color.LightGray,
                fontSize = 12.sp,
                modifier = Modifier.width(90.dp)
            )
            Slider(
                value = selectedClip.speed,
                onValueChange = onSpeedChange,
                valueRange = 0.25f..4.0f,
                steps = 14,
                modifier = Modifier.weight(1f)
            )
        }

        // Brightness Control Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Brightness",
                color = Color.LightGray,
                fontSize = 12.sp,
                modifier = Modifier.width(90.dp)
            )
            Slider(
                value = selectedClip.colorAdjustments.brightness,
                onValueChange = {
                    onColorAdjustmentsChange(selectedClip.colorAdjustments.copy(brightness = it))
                },
                valueRange = -0.5f..0.5f,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
