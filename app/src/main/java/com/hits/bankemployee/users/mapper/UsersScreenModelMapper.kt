package com.hits.bankemployee.users.mapper

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
                firstName = profile.firstName,
                lastName = profile.lastName,
                isBlocked = profile.isBanned,
                role = profile.role.toUserRole(),
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