package com.example.ap_ment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "is_sign_up_by_google")
    private boolean isSignUpByGoogle;

    @Column(name = "role")
    private String role;

    @Column(name = "friendrequest_code")
    private String friendRequestCode;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id")
    private Set<FriendRequest> friendRequests = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_friend",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<User> friends = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return isSignUpByGoogle() == user.isSignUpByGoogle() && Objects.equals(getId(), user.getId()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getLastName(), user.getLastName()) && Objects.equals(getRole(), user.getRole()) && Objects.equals(getFriendRequestCode(), user.getFriendRequestCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getPassword(), getFirstName(), getLastName(), isSignUpByGoogle(), getRole(), getFriendRequestCode());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isSignUpByGoogle=" + isSignUpByGoogle +
                ", role='" + role + '\'' +
                ", friendRequestCode='" + friendRequestCode + '\'' +
                '}';
    }
}
