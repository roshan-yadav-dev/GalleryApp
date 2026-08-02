package com.gallery.app.feature.editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gallery.app.core.domain.model.editor.ExportProgress
import com.gallery.app.core.domain.model.editor.ExportStatus

@Composable
fun ExportProgressDialog(
    exportProgress: ExportProgress,
    onDismiss: () -> Unit
) {
    if (exportProgress.status == ExportStatus.IDLE) return

    Dialog(onDismissRequest = {
        if (exportProgress.status == ExportStatus.SUCCESS || exportProgress.status == ExportStatus.ERROR) {
            onDismiss()
        }
    }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2A2D34), RoundedCornerShape(12.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = when (exportProgress.status) {
                        ExportStatus.EXPORTING -> "Exporting Video..."
                        ExportStatus.SUCCESS -> "Export Complete!"
                        ExportStatus.ERROR -> "Export Failed"
                        ExportStatus.IDLE -> ""
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (exportProgress.status == ExportStatus.EXPORTING) {
                    LinearProgressIndicator(
                        progress = { exportProgress.percentage / 100f },
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${exportProgress.percentage.toInt()}%",
                        color = Color.LightGray
                    )
                }

                if (exportProgress.status == ExportStatus.ERROR) {
                    Text(
                        text = exportProgress.errorMessage ?: "Unknown error occurred",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (exportProgress.status == ExportStatus.SUCCESS) {
                    Text(
                        text = "Video saved to Movies/GalleryAPP_Edited",
                        color = Color.Green,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (exportProgress.status == ExportStatus.SUCCESS || exportProgress.status == ExportStatus.ERROR) {
                    Button(onClick = onDismiss) {
                        Text("Done")
                    }
                }
            }
        }
    }
}
