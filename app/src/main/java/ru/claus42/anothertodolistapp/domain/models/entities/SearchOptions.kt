package ru.claus42.anothertodolistapp.domain.models.entities

enum class SearchLayoutViewType {
    LINEAR, GRID
}

enum class DateSortType {
    CREATION, CHANGED
}

enum class DateSortValue {
    NEW_ONES_FIRST,
    OLD_ONES_FIRST
}

enum class DeadlineSortValue {
    EARLIER_FIRST,
    LATER_FIRST,
    NO_DEADLINE_SORT
}

enum class ImportanceSortValue {
    MOST_IMPORTANT_FIRST,
    LESS_IMPORTANT_FIRST,
    NO_IMPORTANCE_SORT
}