package changepasswordissue

import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification
import test.User

@Integration
@Rollback
class ChangePasswordIntegrationSpec extends Specification {

    @Autowired
    AuthenticationService authenticationService

    final String username = 'joe'

    final String oldPassword = 'old'
    final String newPassword = 'new'

    void "change password -- authenticate via authentication manager"() {
        given:
        User user = new User(username: username, password: oldPassword).save()

        expect:
        authenticationService.authenticateUserViaAuthenticationManager(username, oldPassword)

        when:
        user.password = newPassword
        user.save()

        then:
        !authenticationService.authenticateUserViaAuthenticationManager(username, oldPassword)

        and:
        authenticationService.authenticateUserViaAuthenticationManager(username, newPassword)
    }

    void "change password -- authenticate via password encoder"() {
        given:
        User user = new User(username: username, password: oldPassword).save()

        expect:
        authenticationService.authenticateUserViaPasswordEncoder(username, oldPassword)

        when:
        user.password = newPassword
        user.save()

        then:
        !authenticationService.authenticateUserViaPasswordEncoder(username, oldPassword)

        and:
        authenticationService.authenticateUserViaPasswordEncoder(username, newPassword)
    }
}
