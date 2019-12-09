package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.domain.User;

import java.sql.SQLException;
import java.util.Collection;

public final class UserService {
	private UserDao userDao = UserDao.getInstance();
	private static UserService userService = new UserService();

	public UserService() {
	}

	public static UserService getInstance(){
		return UserService.userService;
	}

	public Collection<User> findAll() throws SQLException{
		return userDao.findAll();
	}

	public User find(Integer id)throws SQLException {
		return userDao.find(id);
	}
	public User findByUsername(String username)throws SQLException {
		return userDao.findByUsername(username);
	}

	public boolean updateUser(User user)throws SQLException{
		userDao.delete(user);
		return userDao.add(user);
	}

	public boolean addUser(User user)throws SQLException{
		return userDao.add(user);
	}

	public boolean deleteUser(Integer id) throws SQLException{
		User user = this.find(id);
		return this.deleteUser(user);
	}

	public boolean deleteUser(User user){
		return userDao.delete(user);
	}

	public User login(String username, String password) throws SQLException {
		Collection<User> users = this.findAll();
		for (User user : users) {
			if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
				userDao.login(username, password);
			}
		}
		return userDao.login(username, password);
	}
}
