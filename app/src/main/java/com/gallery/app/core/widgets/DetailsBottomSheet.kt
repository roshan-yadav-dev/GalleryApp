package com.gallery.app.core.widgets

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gallery.app.core.domain.model.MediaMetadata

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsBottomSheet(
    metadata: MediaMetadata,
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Details",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            DetailRow(label = "Filename", value = metadata.filename)
            DetailRow(label = "Path", value = metadata.filePath)
            DetailRow(label = "Size", value = metadata.fileSizeFormatted)
            DetailRow(label = "Resolution", value = metadata.resolution)
            DetailRow(label = "MIME Type", value = metadata.mimeType)
            DetailRow(label = "Created", value = metadata.dateCreated)
            DetailRow(label = "Modified", value = metadata.dateModified)

            if (metadata.cameraMake != null || metadata.cameraModel != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Camera Metadata",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                metadata.cameraMake?.let { DetailRow(label = "Make", value = it) }
                metadata.cameraModel?.let { DetailRow(label = "Model", value = it) }
                metadata.aperture?.let { DetailRow(label = "Aperture", value = it) }
                metadata.iso?.let { DetailRow(label = "ISO", value = it) }
                metadata.shutterSpeed?.let { DetailRow(label = "Shutter Speed", value = it) }
                metadata.focalLength?.let { DetailRow(label = "Focal Length", value = it) }
            }

            if (metadata.videoDuration != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Video Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                DetailRow(label = "Duration", value = metadata.videoDuration)
                metadata.videoCodec?.let { DetailRow(label = "Codec", value = it) }
                metadata.videoBitrate?.let { DetailRow(label = "Bitrate", value = it) }
                metadata.videoFrameRate?.let { DetailRow(label = "Frame Rate", value = it) }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    val summary = """
                        Filename: ${metadata.filename}
                        Path: ${metadata.filePath}
                        Size: ${metadata.fileSizeFormatted}
                        Resolution: ${metadata.resolution}
                        MIME: ${metadata.mimeType}
                        Created: ${metadata.dateCreated}
                    """.trimIndent()
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Media Metadata", summary)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "Metadata copied to clipboard", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Copy Metadata")
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text("Copy Metadata")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsBottomSheet(
    item: com.gallery.app.core.domain.model.MediaItem,
    onDismiss: () -> Unit
) {
    val sheetState = androidx.compose.material3.rememberModalBottomSheetState()
    val metadata = MediaMetadata(
        filename = item.displayName,
        filePath = item.path,
        fileSizeFormatted = com.gallery.app.core.common.FileUtils.formatFileSize(item.size),
        mimeType = item.mimeType,
        width = item.width,
        height = item.height,
        resolution = "${item.width} × ${item.height}",
        dateCreated = com.gallery.app.core.common.DateFormatter.formatDateTime(item.dateAdded * 1000),
        dateModified = com.gallery.app.core.common.DateFormatter.formatDateTime(item.dateModified * 1000),
        videoDuration = if (item.isVideo) com.gallery.app.core.common.DateFormatter.formatDuration(item.duration) else null
    )
    DetailsBottomSheet(
        metadata = metadata,
        sheetState = sheetState,
        onDismissRequest = onDismiss
    )
}


@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
