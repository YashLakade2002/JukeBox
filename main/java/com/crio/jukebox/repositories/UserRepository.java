package com.crio.jukebox.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.crio.jukebox.entities.User;

public class UserRepository implements IUserRepository{
    IUserPlayListRepository userPlayListRepository;
    private final Map<String, User> userMap;
    private Integer autoIncrement = 0;

    public UserRepository(IUserPlayListRepository userPlayListRepository){
        this.userPlayListRepository = userPlayListRepository;
        userMap = new HashMap<String, User>();
    }
 
    @Override
    public User save(User entity){
        if(entity.getId() == null){
            autoIncrement++;
            User u = new User(Integer.toString(autoIncrement), entity.getName());
            userMap.put(u.getId(), u);
            return u;
        }
        userMap.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public List<User> findAll(){
        return userMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public User findByName(String name) {
        List<User> opuser = userMap.values().stream().filter(user -> user.getName().equals(name)).collect(Collectors.toList());
        return opuser.isEmpty() ? null : opuser.get(0);
    }


    @Override
    public boolean existsById(String id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void delete(User entity) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteById(String id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        return 0;
    }
}
