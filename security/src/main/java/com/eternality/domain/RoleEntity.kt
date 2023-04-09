package com.eternality.domain

import org.hibernate.annotations.Immutable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "roles")
class RoleEntity {

    @Id
    @Column(name = "role_id")
    var roleId: Long? = null

    @Column(name = "name")
    var name: String? = null
}
