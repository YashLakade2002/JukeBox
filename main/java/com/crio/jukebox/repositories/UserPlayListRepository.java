package com.crio.jukebox.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.crio.jukebox.entities.UserPlayList;

public class UserPlayListRepository implements IUserPlayListRepository{
    private final Map<String, UserPlayList> userPlayListMap;
    private Integer autoIncrement = 0;

    public UserPlayListRepository(){
        userPlayListMap = new HashMap<String, UserPlayList>();
    }

    public UserPlayListRepository(Map<String, UserPlayList> userPlayListMap){
        this.userPlayListMap = userPlayListMap;
        this.autoIncrement = userPlayListMap.size();
    }

    @Override
    public UserPlayList save(UserPlayList entity){
        if(entity.getId() == null){
            autoIncrement++;
            UserPlayList s = new UserPlayList(Integer.toString(autoIncrement), entity.getUser(), entity.getPlayLists());
            userPlayListMap.put(s.getId(), s);
            return s;
        }
        userPlayListMap.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public List<UserPlayList> findAllUserPlayList(){
        return userPlayListMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<UserPlayList> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<UserPlayList> findById(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean existsById(String id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void delete(UserPlayList entity) {
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


 
