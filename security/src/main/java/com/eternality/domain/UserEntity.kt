package com.eternality.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.function.Consumer
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "users")
class UserEntity: UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @Column(name = "user_id", nullable = false)
    var userId: Long? = null

    @Column(name = "user_name")
    var userName: String? = null

    @Column(name = "first_name")
    var firstName: String? = null

    @Column(name = "last_name")
    var lastName: String? = null

    @Column(name = "email")
    var email: String? = null

    @Column(name = "phone")
    var phone: String? = null

    @Column(name = "password")
    var pass: String? = null

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "role_id")]
    )
    val roles: List<RoleEntity> = ArrayList()

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val authorities: MutableSet<GrantedAuthority> = HashSet()
        roles.forEach{ it: RoleEntity ->
            authorities.add(SimpleGrantedAuthority(it.name))
        }
        return authorities
    }

    override fun getUsername(): String = this.userName!!
    override fun getPassword(): String = this.pass!!

    override fun isAccountNonExpired(): Boolean =true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}