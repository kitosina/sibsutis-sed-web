package sibsutis.sed.sedsibsutis.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_secret", schema = "public")
public class UserSecret {

    @Id
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "key_public", updatable = false, nullable = false)
    private byte[] keyPublic;

    @Column(name = "key_private", updatable = false, nullable = false)
    private byte[] keyPrivate;

}
