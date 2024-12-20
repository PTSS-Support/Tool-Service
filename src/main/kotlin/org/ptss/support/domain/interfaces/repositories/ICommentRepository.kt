package org.ptss.support.domain.interfaces.repositories

import org.ptss.support.domain.models.Comment

interface ICommentRepository {
    fun create(comment: Comment): String
}