package com.example.ap_ment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "friend_requests")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(name = "receiver_id")
    private Integer receiverId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendRequest that)) return false;
        return getId() == that.getId() && Objects.equals(getSender(), that.getSender()) && Objects.equals(getReceiverId(), that.getReceiverId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSender(), getReceiverId());
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiverId=" + receiverId +
                '}';
    }
}
