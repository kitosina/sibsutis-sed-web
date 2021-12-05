package sibsutis.sed.sedsibsutis.model.entity.security;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Set;

/**
 * Users representation class
 * @author kitosina
 * @version 0.2
 * @see Data
 * @see AllArgsConstructor
 * @see NoArgsConstructor
 * @see Table
 * @see Entity
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@ToString
@Table(name = "users", schema = "security")
public class UserSystemEntity {

    /**
     * An id field for DB identification
     * @see Id
     * @see GeneratedValue
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * password field for DB
     * @see Column
     */
    @Column
    private String password;

    /**
     * email field for DB
     * @see Column
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * User role field for using in authorities in Spring security
     * creates and wires with User_role field.
     * user identifies by user_id field in table
     *
     * @see CollectionTable
     * @see Role
     * @see Enumerated
     * @see JoinColumn
     * @see ElementCollection
     */
    @ElementCollection(targetClass = Role.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "user_role", schema = "security", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

}
