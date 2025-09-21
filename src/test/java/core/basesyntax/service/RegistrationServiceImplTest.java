package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.exception.InvalidDataException;
import core.basesyntax.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {
    private static final String FIRST_LOGIN = "User123";
    private static final String FIRST_PASSWORD = "Password";
    private static final String SECOND_PASSWORD = "Password2";
    private static final String BAD_PASSWORD = "Pass";
    private static final int FIRST_AGE = 21;
    private static final int SECOND_AGE = 22;
    private static final int AGE_UNDER_18 = 17;
    private static final String SHORT_LOGIN = "user";

    private final RegistrationService registrationService =
            new RegistrationServiceImpl(new StorageDaoImpl());

    @BeforeEach
    void setUp() {
        Storage.people.clear();
    }

    @Test
    void register_validUser_ok() {
        User newUser = createUser(FIRST_LOGIN, FIRST_PASSWORD, FIRST_AGE);
        User registeredUser = registrationService.register(newUser);
        assertNotNull(registeredUser.getId());
        assertEquals(newUser.getLogin(), registeredUser.getLogin());
        assertEquals(newUser.getPassword(), registeredUser.getPassword());
        assertEquals(newUser.getAge(), registeredUser.getAge());
        assertTrue(Storage.people.contains(registeredUser));
    }

    @Test
    void register_existingLogin_notOk() {
        User existingUser = createUser(FIRST_LOGIN, FIRST_PASSWORD, FIRST_AGE);
        Storage.people.add(existingUser);

        User newUser = createUser(FIRST_LOGIN, SECOND_PASSWORD, SECOND_AGE);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(newUser));
    }

    @Test
    void register_nullUser_notOk() {
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(null));
    }

    @Test
    void register_nullLogin_notOk() {
        User user = createUser(null, FIRST_PASSWORD, FIRST_AGE);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_shortLogin_notOk() {
        User user = createUser(SHORT_LOGIN, FIRST_PASSWORD, FIRST_AGE);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_nullPassword_notOk() {
        User user = createUser(FIRST_LOGIN, null, FIRST_AGE);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_shortPassword_notOk() {
        User user = createUser(FIRST_LOGIN, BAD_PASSWORD, FIRST_AGE);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_ageUnder18_notOk() {
        User user = createUser(FIRST_LOGIN, BAD_PASSWORD, AGE_UNDER_18);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(user));

    }

    private User createUser(String login, String password, int age) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setAge(age);
        return user;
    }
}
