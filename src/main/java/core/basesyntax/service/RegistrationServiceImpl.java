package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.exception.InvalidDataException;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private static final int MIN_LOGIN_LENGTH = 6;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MIN_AGE = 18;

    private final StorageDao storageDao;

    public RegistrationServiceImpl(StorageDao storageDao) {
        this.storageDao = storageDao;
    }

    @Override
    public User register(User user) {
        if (user == null) {
            throw new InvalidDataException("User can't be null");
        }
        if (user.getLogin() == null || user.getLogin().length() < MIN_LOGIN_LENGTH) {
            throw new InvalidDataException(
                    "Login must be at least " + MIN_LOGIN_LENGTH + " characters"
            );
        }
        if (user.getPassword() == null || user.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new InvalidDataException(
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters"
            );
        }
        if (user.getAge() == null || user.getAge() < MIN_AGE) {
            throw new InvalidDataException(
                    "Age is below minimum age(" + MIN_AGE + ")"
            );
        }
        if (storageDao.get(user.getLogin()) != null) {
            throw new InvalidDataException(
                    "User with login " + user.getLogin() + " already exists"
            );
        }
        return storageDao.add(user);
    }
}
