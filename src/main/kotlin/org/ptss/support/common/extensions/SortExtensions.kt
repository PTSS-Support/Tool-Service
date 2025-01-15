package org.ptss.support.common.extensions

import io.quarkus.panache.common.Sort
import org.ptss.support.domain.enums.SortOrder

fun SortOrder.toSortDirection(): Sort.Direction =
    if (this == SortOrder.DESC) Sort.Direction.Descending else Sort.Direction.Ascending

fun SortOrder.toComparisonOperator(): String =
    if (this == SortOrder.DESC) "<" else ">"