package com.crio.jukebox.repositories;

import java.util.List;
import com.crio.jukebox.entities.UserPlayList;

public interface IUserPlayListRepository extends CRUDRepository<UserPlayList, String>{
    public List<UserPlayList> findAllUserPlayList();    
}
