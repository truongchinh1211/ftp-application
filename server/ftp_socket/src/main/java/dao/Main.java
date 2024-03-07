/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.File;
import model.User;

/**
 *
 * @author lamanhhai
 */
public class Main {
    public static void main(String[] args) {
        
//        UserDao userDao = new UserDao();
//        
//        User user = new User();
//        user.setId(2);
//        user.setUsername("nguyenvanabc@gmail.com");
//        
//        boolean res = userDao.save(user);
//        System.out.println("Kiểm tra: " + res);
        
//        User user2 = userDao.getUserById(user.getId());
//        
//        List<User> users = userDao.getAllUsers();
//        users.forEach(u -> System.out.println(u.getUsername()));
//
//        userDao.update(user);
        
        FileDao fileDao = new FileDao();
        File file = new File();
        file.setPath("/test");
        
        User user = new User();
        user.setId(2);
        
        file.setUser(user);
        
        boolean isSuccess = fileDao.save(file);
        System.out.println("Kiem tra file: " + isSuccess);
    }
}
