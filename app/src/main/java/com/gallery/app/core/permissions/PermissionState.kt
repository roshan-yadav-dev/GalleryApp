package com.gallery.app.core.permissions

sealed class PermissionState {
    object Granted : PermissionState()
    object Denied : PermissionState()
    object PermanentlyDenied : PermissionState()
}
