package wombatukun.tests.test11.queryservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wombatukun.tests.test11.common.messaging.UserEvent;
import wombatukun.tests.test11.queryservice.dao.repositories.UserRepository;
import wombatukun.tests.test11.queryservice.dto.UserStatDto;
import wombatukun.tests.test11.queryservice.mappers.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserStatDto> getUsersStats() {
        return userRepository.getUsersStats()
                .stream()
                .map(userMapper::mapProjectionToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserStatDto> getCouriersStats() {
        return userRepository.getCouriersStats()
                .stream()
                .map(userMapper::mapProjectionToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void handleEvent(UserEvent event) {

    }

}
