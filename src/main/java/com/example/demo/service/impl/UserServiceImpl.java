package com.example.demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.demo.exception.userException.PasswordTokenWrongException;
import com.example.demo.exception.userException.UserException;
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
import org.springframework.web.multipart.MultipartFile;

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

	private final String uploadDir = "uploads/account/";

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

	//驗證email
	@Override
	public void emailConfirm(String email, String token) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("帳號尚未註冊"));
		if (user.getEmailVerf()) {
			return;
		}
		if (user.getEmailVerfToken() == null) {
			throw new UserException("驗證碼已失效，請聯繫客服");
		}
		if (!user.getEmailVerfToken().equals(token)) {
			throw new UserException("驗證失敗，請確認網址");
		}
		user.setEmailVerf(true);
		user.setEmailVerfToken(null);
		userRepository.save(user);
	}

	//確認email驗證狀態
	@Override
	public Boolean checkEmailConfirm(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(()->new UserNotFoundException("帳號尚未註冊"));
		return user.getEmailVerf();
	}

	@Override
	public Boolean existsByUsername(String username){
		return userRepository.existsByUsername(username);
	}

	@Override
	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	//建立重設密碼token
	@Override
	public String setPasswordToken(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(UserNotFoundException::new);
		SecureRandom random = new SecureRandom();
		String token = new BigInteger(130, random).toString(32);
		user.setPasswordToken(token);
		userRepository.save(user);
		return token;
	}

	//重設密碼
	@Override
	public void resetPassword(String email, String token, String newPassword) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(UserNotFoundException::new);
		if(!token.equals(user.getPasswordToken())){
			throw new PasswordTokenWrongException("重設密碼連結錯誤");
		}
		String salt = Hash.getSalt();
		String passwordHash = Hash.getHash(newPassword, salt);
		user.setPasswordHash(passwordHash);
		user.setSalt(salt);
		user.setPasswordToken(null);
		userRepository.save(user);
	}

	//修改帳號名稱
	@Override
	public void updateUsername(Integer userId, String newUsername) {
		if (newUsername.isBlank()) return;
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		String oldUsername = user.getUsername();
		if (oldUsername.equals(newUsername)){
			return;
		}
		if (existsByUsername(newUsername)) {
			throw new UserExistedException("帳號名稱已被使用");
		}
		user.setUsername(newUsername);
		userRepository.save(user);
	}

	//修改帳號圖片
	@Override
	public void updateImage(Integer userId, MultipartFile image) throws IOException {
		if (image == null) return;
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		//建立新圖片
		String fileName = image.getOriginalFilename();
		String extension = "";
		if (fileName !=null && fileName.contains(".")){
			extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		}else{
			extension = ".jpg";
		}
		String savedFileName = userId + "_" + UUID.randomUUID().toString() + extension;
		Path filepath = Paths.get(uploadDir, savedFileName);
		try{
			Files.createDirectories(filepath.getParent());
			Files.write(filepath, image.getBytes());
		} catch(IOException e){
			throw new RuntimeException("圖片新增失敗");
		}
		//刪除舊圖片
		String oldImagePath = user.getImagePath();
		if (oldImagePath != null && !oldImagePath.equals("uploads/account/0_defaultImg.jpg")){
			File oldFile = new File(oldImagePath);
			if (oldFile.exists()) {
				if(!oldFile.delete()){
					System.out.println("圖片刪除失敗");
				}
			}
		}
		user.setImagePath(filepath.toString());
		userRepository.save(user);
	}
	
	@Override
	public void updateUserPassword(Integer userId, String password) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		String salt = Hash.getSalt();
		String passwordHash = Hash.getHash(password, salt);
		user.setPasswordHash(passwordHash);
		user.setSalt(salt);
		userRepository.save(user);
	}

	@Override
	public void deleteUserById(Integer userId) {
		userRepository.deleteById(userId);
	}

	//收藏電影
	@Override
	public Boolean toggleMovieForWatchlist(Integer userId, Integer movieId) {
		User user = userRepository.findByIdWithWatchlist(userId).orElseThrow(UserNotFoundException::new);
		Movie movie = movieRepository.findById(movieId).orElseThrow(MovieNotFoundException::new);
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

	//收藏電影清單
	@Override
	public List<MovieCardDto> getWatchlist(Integer userId) {
		List<Integer> movieIds = userRepository.findWatchlistMovieIds(userId);
		List<Movie> movies = movieRepository.findAllWithReviewsByIds(movieIds);
    return movieService.toCardDtoList(movies, userId);
	}
	
}
