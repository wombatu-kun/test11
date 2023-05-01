package wombatukun.tests.test11.queryservice.dao.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wombatukun.tests.test11.common.enums.UserStatus;
import wombatukun.tests.test11.common.security.Role;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Column(name = "suspended_at")
    private Date suspendedAt;
    @Column(name = "deleted_at")
    private Date deletedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> usersOrders = new ArrayList<>();
    @OneToMany(mappedBy = "courier", fetch = FetchType.LAZY)
    private List<Order> couriersOrders = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
