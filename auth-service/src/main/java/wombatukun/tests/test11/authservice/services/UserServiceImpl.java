package wombatukun.tests.test11.authservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import wombatukun.tests.test11.authservice.dao.entities.User;
import wombatukun.tests.test11.authservice.dao.projections.UserCount;
import wombatukun.tests.test11.authservice.dao.repositories.UserRepository;
import wombatukun.tests.test11.authservice.dto.UserDto;
import wombatukun.tests.test11.authservice.dto.UserForm;
import wombatukun.tests.test11.authservice.enums.Status;
import wombatukun.tests.test11.authservice.mappers.UserMapper;
import wombatukun.tests.test11.authservice.messaging.UserEventPublisher;
import wombatukun.tests.test11.common.exceptions.OperationNotPermittedException;
import wombatukun.tests.test11.common.exceptions.RegistrationNotAllowedException;
import wombatukun.tests.test11.common.exceptions.ResourceNotFoundException;
import wombatukun.tests.test11.common.security.Role;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEventPublisher userEventPublisher;

    @Transactional
    @Override
    public UserDto register(UserForm form) {
        if (userRepository.findByEmail(form.getEmail()) == null) {
            User user = userMapper.mapFormToEntity(form);
            user = userRepository.save(user);
            userEventPublisher.sendEvent(userMapper.mapEntityToEvent(user));
            return userMapper.mapEntityToDto(user);
        } else {
            throw new RegistrationNotAllowedException("Conflict email: " + form.getEmail());
        }
    }

    @Transactional
    @Override
    public UserDto updateStatus(Authentication authentication, Long id, Status status) {
        User user = findUserById(id);
        if (!user.getEmail().equalsIgnoreCase(authentication.getPrincipal().toString())) {
            user.setStatus(status);
            user = userRepository.save(user);
            userEventPublisher.sendEvent(userMapper.mapEntityToEvent(user));
            return userMapper.mapEntityToDto(user);
        } else {
            throw new OperationNotPermittedException("unable to update your own status");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getById(Long id) {
        User user = findUserById(id);
        return userMapper.mapEntityToDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserCount> countTotalsByRoles() {
        return userRepository.countTotalsByRoles();
    }

    @Transactional(readOnly = true)
    @Override
    public Long count(Role role) {
        return userRepository.countAllByRole(role);
    }

    private User findUserById(Long userId) {
        Assert.notNull(userId, "userId must not be null");
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user not found"));
    }
}
