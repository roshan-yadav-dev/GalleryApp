package com.gallery.app.core.domain.usecase

import com.gallery.app.core.datastore.UserPreferences
import com.gallery.app.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<UserPreferences> {
        return settingsRepository.userPreferences
    }
}
