package com.crio.jukebox.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.crio.jukebox.entities.Album;

public class AlbumRepository implements IAlbumRepository{
    private final Map<String, Album> albumMap;
    private Integer autoIncrement = 0;

    public AlbumRepository(){ 
        albumMap = new HashMap<String, Album>();
    }

    public AlbumRepository(Map<String, Album> albumMap){
        this.albumMap = albumMap;
        this.autoIncrement = albumMap.size();
    }
    

    @Override
    public Album save(Album entity){
        if(entity.getId() == null){
            autoIncrement++;
            Album s = new Album(Integer.toString(autoIncrement), entity.getName(), entity.getSongList(), entity.getOwnerName());
            albumMap.put(s.getId(), s);
            return s;
        }
        albumMap.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public List<Album> findAll(){
        return albumMap.values().stream().collect(Collectors.toList());
    }


    @Override
    public Album findByName(String name) {
        List<Album> albums = albumMap.values().stream().filter(album -> album.getName().equals(name)).collect(Collectors.toList());
        return albums.isEmpty() ? null : albums.get(0);
    }


    @Override
    public Optional<Album> findById(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean existsById(String id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void delete(Album entity) {
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
