package sibsutis.sed.sedsibsutis.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "user_secret", schema = "public")
public class UserSecretEntity {

//    FIXME: email не может гарантировать уникальность!!!
    @Id
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "key_public", updatable = false, nullable = false)
    private byte[] keyPublic;

    @Column(name = "key_private", updatable = false, nullable = false)
    private byte[] keyPrivate;

}
