package ru.claus42.anothertodolistapp.domain.models.entities


interface ComparatorParameter

enum class SearchLayoutViewType {
    LINEAR, GRID
}

enum class DateSortType: ComparatorParameter {
    CREATION, CHANGED
}

enum class DateSortValue: ComparatorParameter {
    NEW_ONES_FIRST,
    OLD_ONES_FIRST
}

enum class DeadlineSortValue: ComparatorParameter {
    EARLIER_FIRST,
    LATER_FIRST,
    NO_DEADLINE_SORT
}

enum class ImportanceSortValue: ComparatorParameter {
    MOST_IMPORTANT_FIRST,
    LESS_IMPORTANT_FIRST,
    NO_IMPORTANCE_SORT
}