package com.example.demo.service.impl;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.movieException.MovieNotFoundException;
import com.example.demo.exception.userException.UserExistedException;
import com.example.demo.exception.userException.UserNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.MovieCardDto;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.dto.UserRegisterDto;
import com.example.demo.model.entity.Movie;
import com.example.demo.model.entity.User;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MovieService;
import com.example.demo.service.UserService;
import com.example.demo.util.Hash;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	MovieRepository movieRepository;
	@Autowired
	MovieService movieService;
	@Autowired
	UserMapper userMapper;

	@Override
	public UserDto getUserById(Integer userId) {
		Optional<User> optUser = userRepository.findById(userId);
		if(optUser.isEmpty()) {
			throw new UserNotFoundException("使用者不存在");
		}
		return userMapper.toDto(optUser.get());
	}
	

	@Override
	public String addUser(UserRegisterDto userRegisterDto) {
		String username = userRegisterDto.getUsername();
		String email = userRegisterDto.getEmail();
		String password = userRegisterDto.getPassword();
		return addUser(username, email, password);
	}
	
	@Override
	public String addUser(String username, String email, String password) {
		if(userRepository.findByUsername(username).isPresent()) {
			throw new UserExistedException("使用者名稱已使用");
		}
		if(userRepository.findByEmail(email).isPresent()) {
			throw new UserExistedException("此信箱已註冊");
		}
		String salt = Hash.getSalt();
		String passwordHash = Hash.getHash(password, salt);
		
		SecureRandom random = new SecureRandom();
		String token = new BigInteger(130, random).toString(32);
		
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPasswordHash(passwordHash);
		user.setSalt(salt);
		user.setEmailVerfToken(token);
		userRepository.save(user);
		return token;
	}

	@Override
	public Boolean emailConfirm(String username, String token) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(()-> new UserNotFoundException("帳號尚未註冊"));
		if (user.getEmailVerfToken().equals(token)) {
			user.setEmailVerf(true);			
			userRepository.save(user);
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean existsByUsername(String username){
		return userRepository.existsByUsername(username);
	}

	@Override
	public void updateUsername(Integer userId, String username) {
		if (existsByUsername(username)) {
			throw new UserExistedException("使用者名稱已被使用");
		}
		User user = userRepository.findById(userId).get();
		user.setUsername(username);		
		userRepository.save(user);
	}
	
	@Override
	public void updateUserPassword(Integer userId, String password) {
		User user = userRepository.findById(userId).get();
		String salt = Hash.getSalt();
		String passwordHash = Hash.getHash(password, salt);
		user.setPasswordHash(passwordHash);
		user.setSalt(salt);
		userRepository.save(user);
	}
	
	@Override
	public void updateUserNameAndPassword(Integer userId, String username, String password) {
		updateUsername(userId, username);
		updateUsername(userId, password);
	}

	@Override
	public void deleteUserById(Integer userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public Boolean toggleMovieForWatchlist(Integer userId, Integer movieId) {
		User user = userRepository.findByIdWithWatchlist(userId).orElseThrow(()->new UserNotFoundException());
		Movie movie = movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException());
		if (user.getWatchlist().contains(movie)) {
			user.getWatchlist().remove(movie);
			userRepository.save(user);
			return false;
		}else {
			user.getWatchlist().add(movie);
			userRepository.save(user);
			return true;
		}
	}

	@Override
	public List<MovieCardDto> getWatchlist(Integer userId) {
		List<Integer> movieIds = userRepository.findWatchListMoveIdById(userId);
		List<Movie> movies = movieRepository.findAllWithReviewsByIds(movieIds);
		List<MovieCardDto> dtos = movies.stream().map(m->movieService.toCardDto(m)).toList();
		return dtos;
	}
	
	
}
