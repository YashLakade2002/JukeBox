package com.crio.jukebox.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.crio.jukebox.entities.Album;
import com.crio.jukebox.entities.Song;
import com.crio.jukebox.entities.Artist;
import com.crio.jukebox.repositories.ISongRepository;
import com.crio.jukebox.repositories.IAlbumRepository;


public class SongService implements ISongService{

    private final ISongRepository songRepository;
    private final IAlbumRepository albumRepository;

    public SongService(ISongRepository songRepository, IAlbumRepository albumRepository){
        this.songRepository = songRepository;
        this.albumRepository = albumRepository;
    }
    
    public void loadSong(String fileName){        
        try{  
            File file = new File(fileName);
            FileReader fr = new FileReader(file); 
            BufferedReader br = new BufferedReader(fr);  
            String line;  
            while((line = br.readLine())!=null) {  
                String[] arrOfStr = line.split(",");
                // SONG_NAME    GENRE   ALBUM_NAME  ALBUM_ARTIST 
                List<String> arrOfArtist = new ArrayList<>();
                String[] tempArrOfArtist =  arrOfStr[4].split("#");
                for(String str : tempArrOfArtist) {
                    arrOfArtist.add(str);
                }

                Song song = new Song(arrOfStr[0], arrOfStr[1], arrOfArtist);
                song.setAlbumName(arrOfStr[2]);
                songRepository.save(song);

                Album album = albumRepository.findByName(arrOfStr[2]);
                if(album != null){
                    List<Song> songList = album.getSongList();
                    songList.add(song);
                }else{
                    List<Song> songList = new ArrayList<>();
                    songList.add(song);
                    album = new Album(arrOfStr[2], songList, arrOfStr[3]);
                    albumRepository.save(album);
                }
            }          
            fr.close();    
        }  
        catch(IOException e) {  
            e.printStackTrace();  
        }  
    }

}
