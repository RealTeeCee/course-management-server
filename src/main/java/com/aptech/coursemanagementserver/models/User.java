package com.aptech.coursemanagementserver.models;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.aptech.coursemanagementserver.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "Users")
// implement interface UserDetails make user become spring user
public class User implements UserDetails {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private long id;
    @Column(columnDefinition = "varchar(100)")
    private String first_name;
    @Column(columnDefinition = "varchar(100)")
    private String last_name;
    @Column(unique = true, columnDefinition = "varchar(100)")
    private String email;
    private String password;

    @CreationTimestamp
    @Builder.Default
    private Instant created_at = Instant.now();
    @UpdateTimestamp
    private Instant updated_at;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "user")
    @Builder.Default
    Set<Enrollment> enrollments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "user")
    @Builder.Default
    Set<Question> questions = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "user")
    @Builder.Default
    Set<Answer> answers = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.ADMIN;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities(); // Spring will use the list of authorities of each user
    }

    @Override
    public String getUsername() { // login by email => username: email
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() { // Make sure account nonexpired
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
