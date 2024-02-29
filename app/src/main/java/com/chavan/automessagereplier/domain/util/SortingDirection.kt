package com.chavan.automessagereplier.domain.util

sealed class SortingDirection {
    object Up : SortingDirection()
    object Down : SortingDirection()
}