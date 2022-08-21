package com.melda.bankingproject.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.melda.bankingproject.models.MyUser;
import com.melda.bankingproject.repository.IUserRepository;

@Service
public class UserService implements UserDetailsService , IUserService {

	@Autowired
	private IUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MyUser user = this.userRepository.selectByUsername(username);
		if (user != null) {
			List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
			String[] parts = user.getAuthorities().split(",");
			for (String part : parts) {
				list.add(new SimpleGrantedAuthority(part));
			}
			return User.builder().username(user.getUsername()).password(user.getPassword()).disabled(!user.isEnabled())
					.authorities(list).accountExpired(false).accountLocked(false).credentialsExpired(false).build();
		} else {
			throw new UsernameNotFoundException(username + " Not Found");
		}
	}

	@Override
	public MyUser enableDisableUser(int id,boolean enabled) {
		MyUser user=this.userRepository.selectById(id);
		if(user!=null) {
			user.setEnabled(enabled);
			this.userRepository.enableDisableUser(user);
			return user;
		}
		return null;
	}

	@Override
	public MyUser createUser(String username, String email, String password) {
		if(this.userRepository.selectByUsername(username)==null && this.userRepository.selectByEmail(email)==null) {
			String passwordEncode=new BCryptPasswordEncoder().encode(password);
			MyUser user=new MyUser();
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(passwordEncode);
			user.setEnabled(false);
			user.setAuthorities("CREATE_ACCOUNT");
			this.userRepository.createUser(user);
			return this.userRepository.selectByUsername(username);
		}
		return null;
	}

}
