package com.hits.bankemployee.users.mapper

import androidx.compose.material3.MaterialTheme
import com.hits.bankemployee.R
import com.hits.bankemployee.core.domain.entity.ProfileEntity
import com.hits.bankemployee.core.domain.entity.RegisterRequestEntity
import com.hits.bankemployee.users.model.CreateUserDialogModel
import com.hits.bankemployee.users.model.UserRole
import com.hits.bankemployee.users.model.toRoleType
import com.hits.bankemployee.users.model.toUserRole
import com.hits.bankemployee.users.model.userlist.UserModel

class UsersScreenModelMapper {

    fun map(usersPage: List<ProfileEntity>): List<UserModel> {
        return usersPage.map { profile ->
            UserModel(
                id = profile.id,
                isBlocked = profile.isBanned,
                fullName = "${profile.firstName} ${profile.lastName}",
                status = when (profile.isBanned) {
                    true -> "Заблокирован"
                    false -> profile.role.toUserRole().title
                },
                actionIconResId = when (profile.isBanned) {
                    true -> R.drawable.ic_unblock
                    false -> R.drawable.ic_block
                },
                foregroundColorProvider = {
                    when (profile.isBanned) {
                        true -> MaterialTheme.colorScheme.onSurfaceVariant
                        false -> MaterialTheme.colorScheme.onPrimaryContainer
                    }
                },
                backgroundColorProvider = {
                    when (profile.isBanned) {
                        true -> MaterialTheme.colorScheme.surfaceVariant
                        false -> MaterialTheme.colorScheme.primaryContainer
                    }
                },
            )
        }
    }

    fun map(dialog: CreateUserDialogModel, role: UserRole): RegisterRequestEntity {
        return RegisterRequestEntity(
            firstName = dialog.firstName,
            lastName = dialog.lastName,
            email = dialog.email,
            password = dialog.password,
            role = role.toRoleType(),
        )
    }
}