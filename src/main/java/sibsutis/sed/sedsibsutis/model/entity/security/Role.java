package sibsutis.sed.sedsibsutis.model.entity.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;


/**
 * Role enum
 * @author kitosina
 * @version 0.1
 * @see GrantedAuthority
 */
public enum Role implements GrantedAuthority {
    USER,
    ADMIN;

    /**
     * Automatically generated method for authorities and role system in Spring Security
     * @see Override
     * @see org.springframework.security.authentication.jaas.AuthorityGranter
     */
    @Override
    public String getAuthority() {
        return name();
    }
}
