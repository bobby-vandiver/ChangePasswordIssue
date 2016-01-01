package changepasswordissue

import grails.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.encoding.PasswordEncoder
import org.springframework.security.core.AuthenticationException
import test.User

@Transactional
class AuthenticationService {

    @Autowired
    AuthenticationManager authenticationManager

    @Autowired
    PasswordEncoder passwordEncoder

    boolean authenticateUserViaAuthenticationManager(String username, String password) {
        try {
            def authentication = new UsernamePasswordAuthenticationToken(username, password)
            authenticationManager.authenticate(authentication)
            return true
        }
        catch(AuthenticationException e) {
            log.warn("Failed to authenticate user [$username]: ${e.message}")
            return false
        }
    }

    boolean authenticateUserViaPasswordEncoder(String username, String password) {
        def user = User.findByUsername(username)
        return user && passwordEncoder.isPasswordValid(user.password, password, null)
    }
}
