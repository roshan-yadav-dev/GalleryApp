package com.gallery.app.core.domain.model

enum class SortBy {
    DATE,
    NAME,
    SIZE
}

enum class SortDirection {
    ASCENDING,
    DESCENDING
}

data class SortOrder(
    val sortBy: SortBy = SortBy.DATE,
    val direction: SortDirection = SortDirection.DESCENDING
)
