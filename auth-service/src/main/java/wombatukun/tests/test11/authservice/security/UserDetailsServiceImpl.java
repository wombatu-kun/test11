package wombatukun.tests.test11.authservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wombatukun.tests.test11.authservice.dao.entities.User;
import wombatukun.tests.test11.authservice.dao.repositories.UserRepository;

@Service
@RequiredArgsConstructor
@Qualifier("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException(email);
		}
		return new UserDetailsImpl(user);
	}
}
