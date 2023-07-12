package com.crio.jukebox.services;

import java.util.ArrayList;
import java.util.List;
import com.crio.jukebox.dtos.PlayListSongDto;
import com.crio.jukebox.dtos.UserPlayedSongDto;

import com.crio.jukebox.entities.User;
import com.crio.jukebox.entities.PlayList;
import com.crio.jukebox.entities.Song;
import com.crio.jukebox.entities.SongPlayingOrder;
import com.crio.jukebox.entities.SongPlayingStatus;
import com.crio.jukebox.entities.UserPlayList;
import com.crio.jukebox.services.SongService;
import com.crio.jukebox.services.UserService;

import com.crio.jukebox.exceptions.SongNotFoundException;
import com.crio.jukebox.exceptions.UserNotFoundException;
import com.crio.jukebox.exceptions.PlayListNotFoundException;
import com.crio.jukebox.exceptions.InvalidOperationException;

import com.crio.jukebox.repositories.ISongRepository;
import com.crio.jukebox.repositories.IUserPlayListRepository;
import com.crio.jukebox.repositories.IUserRepository;
import com.crio.jukebox.repositories.UserRepository;

public class UserPlayListService implements IUserPlayListService{
    private final IUserRepository userRepository;
    private final ISongRepository songRepository;
    private final IUserPlayListRepository userPlayListRepository;
    private final UserPlayedSongDto userPlayedSongDto;

    public UserPlayListService(IUserRepository userRepository, ISongRepository songRepository, IUserPlayListRepository userPlayListRepository){
        this.userPlayListRepository = userPlayListRepository;
        this.songRepository = songRepository;
        this.userRepository = userRepository;
        this.userPlayedSongDto = new UserPlayedSongDto();
    }

    public PlayList createPlayList(String userId, String playListName, List<String> csong){
        List<Song> songsOfPlayList = new ArrayList<>();
        for(String songId : csong){
            Song song = songRepository.findById(songId).orElseThrow(() -> new SongNotFoundException("Song not found!"));
            songsOfPlayList.add(song);
        }

        PlayList newPlayList = new PlayList(playListName, songsOfPlayList);

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found!"));

        List<PlayList> newPlayLists = new ArrayList<>();
        newPlayLists.add(newPlayList);
        UserPlayList userPlayList = new UserPlayList(user, newPlayLists);
        userPlayListRepository.save(userPlayList);
        return newPlayList;
    }

    public void deletePlayList(String userId, String playListId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found!"));
        List<UserPlayList> userPlayLists = userPlayListRepository.findAllUserPlayList();
        boolean playListIsPresent = false;
        loop1:
        for(UserPlayList userPlayList : userPlayLists){
            if(user == userPlayList.getUser()){
                List<PlayList> listOfPlaylist = userPlayList.getPlayLists();
                for(int i=0; i<listOfPlaylist.size(); i++){
                    if(listOfPlaylist.get(i).getId() == playListId){
                        listOfPlaylist.remove(i);
                        playListIsPresent = true;
                        break loop1;
                    }
                }
            }
        }

        if(!playListIsPresent) throw new PlayListNotFoundException("PlayList not found!");
    }


    public PlayList addSongToPlayList(String userId, String playListId, List<String> songIds){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found!"));
        List<UserPlayList> userPlayLists = userPlayListRepository.findAllUserPlayList();
        boolean playListIsPresent = false;
        // loop1:
        for(UserPlayList userPlayList : userPlayLists){
            if(user == userPlayList.getUser()){
                List<PlayList> listOfPlaylist = userPlayList.getPlayLists();
                for(int i=0; i<listOfPlaylist.size(); i++){
                    if(listOfPlaylist.get(i).getId() == playListId){
                        PlayList targetedPlayList = listOfPlaylist.get(i);
                        for(String currSongId : songIds){
                            boolean checkIfSongIsPresent = false;
                            List<Song> songs = targetedPlayList.getSongs();
                            Song tempSong = songRepository.findById(currSongId).orElseThrow(() -> new SongNotFoundException("Song not found!"));
                            for(Song song : songs){
                                if(song == tempSong){
                                    checkIfSongIsPresent = true;
                                    break;
                                }
                            }                        
                            if(!checkIfSongIsPresent) songs.add(tempSong);
                        }
                        return targetedPlayList;
                        // playListIsPresent = true;
                        // break loop1;
                    }
                }
            }
        }

        if(!playListIsPresent) throw new PlayListNotFoundException("PlayList not found!"); 
        // return targetedPlayList;
        return null;
    }

  
    public PlayList deleteSongFromPlayList(String userId, String playListId, List<String> songIds){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found!"));
        List<UserPlayList> userPlayLists = userPlayListRepository.findAllUserPlayList();
        PlayList targetedPlayList;
        boolean playListIsPresent = false;
        // loop1:
        for(UserPlayList userPlayList : userPlayLists){
            if(user == userPlayList.getUser()){
                List<PlayList> listOfPlaylist = userPlayList.getPlayLists();
                for(int i=0; i<listOfPlaylist.size(); i++){
                    if(listOfPlaylist.get(i).getId() == playListId){
                        targetedPlayList = listOfPlaylist.get(i);
                        for(String currSongId : songIds){
                            Song tempSong = songRepository.findById(currSongId).orElseThrow(() -> new SongNotFoundException("Song not found!"));
                            targetedPlayList.getSongs().remove(tempSong);
                        }
                        return targetedPlayList;
                        // playListIsPresent = true;
                        // break loop1;
                    }
                }
            }
        }

        if(!playListIsPresent) throw new PlayListNotFoundException("PlayList not found!"); 
        return null;
    }


    public UserPlayedSongDto setCurrentPlayList(String userId, String playListId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found!"));
        List<UserPlayList> userPlayLists = userPlayListRepository.findAllUserPlayList();
        PlayList targetedPlayList;
        boolean playListIsPresent = false;
        for(UserPlayList userPlayList : userPlayLists){
            if(user == userPlayList.getUser()){
                List<PlayList> listOfPlaylist = userPlayList.getPlayLists();
                for(int i=0; i<listOfPlaylist.size(); i++){
                    if(listOfPlaylist.get(i).getId() == playListId){
                        targetedPlayList = listOfPlaylist.get(i);
                        // PlayListSongDto playListSongDto = new PlayListSongDto(playListId, targetedPlayList.getName(), targetedPlayList.getSongs());
                        List<Song> songs = targetedPlayList.getSongs();
                        if(songs.isEmpty()) throw new InvalidOperationException("Invalid Operation exception!");
                        Song currSong = songs.get(0);
                        targetedPlayList.setSongPlayingStatus(SongPlayingStatus.valueOf("PLAYING"));
                        userPlayedSongDto.setData(user.getName(), currSong.getName(), currSong.getAlbumName(), currSong.getArtist().get(0));
                        return userPlayedSongDto;
                    }
                }
            }
        }
        if(!playListIsPresent) throw new PlayListNotFoundException("PlayList not found!"); 
        return null;
    }


    public UserPlayedSongDto playSongById(String userId, String songId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found!"));
        List<UserPlayList> userPlayLists = userPlayListRepository.findAllUserPlayList();
        PlayList targetedPlayList;
        boolean noPlayListIsPlaying = false;
        for(UserPlayList userPlayList : userPlayLists){
            if(user == userPlayList.getUser()){
                List<PlayList> listOfPlaylist = userPlayList.getPlayLists();
                for(int i=0; i<listOfPlaylist.size(); i++){
                    if(listOfPlaylist.get(i).getSongPlayingStatus() == SongPlayingStatus.valueOf("PLAYING")){
                        targetedPlayList = listOfPlaylist.get(i);
                        // PlayListSongDto playListSongDto = new PlayListSongDto(targetedPlayList.getId(), targetedPlayList.getName(), targetedPlayList.getSongs());
                        Song currSong = null;
                        List<Song> songs = targetedPlayList.getSongs();
                        for(Song song : songs){
                            if(song.getId() == songId){
                                currSong = song; 
                                break;
                            }
                        }                   
                        if(currSong == null) throw new SongNotFoundException("Song not found!");
                        userPlayedSongDto.setData(user.getName(), currSong.getName(), currSong.getAlbumName(), currSong.getArtist().get(0));
                        return userPlayedSongDto;
                    }
                }
            }
        }
        if(!noPlayListIsPlaying) throw new InvalidOperationException("Invalid operation!");
        return null;
    }


    public UserPlayedSongDto playSongByOrder(String userId, SongPlayingOrder songPlayingOrder){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found!"));
        List<UserPlayList> userPlayLists = userPlayListRepository.findAllUserPlayList();
        boolean noPlayListIsPlaying = false;
        for(UserPlayList userPlayList : userPlayLists){
            if(user == userPlayList.getUser()){
                List<PlayList> listOfPlaylist = userPlayList.getPlayLists();
                for(int i=0; i<listOfPlaylist.size(); i++){
                    if(listOfPlaylist.get(i).getSongPlayingStatus() == SongPlayingStatus.valueOf("PLAYING")){
                        PlayList targetedPlayList = listOfPlaylist.get(i);
                        // PlayListSongDto playListSongDto = new PlayListSongDto(targetedPlayList.getId(), targetedPlayList.getName(), targetedPlayList.getSongs());
                        Song currSong = null;
                        List<Song> songs = targetedPlayList.getSongs();
                        int j = 0, n = songs.size();
                        for(j=0;j<n;j++){
                            if(songs.get(j).getName() == userPlayedSongDto.getSongName()){
                                currSong = songs.get(j);
                                break;
                            }
                        }

                        if(currSong == null) throw new InvalidOperationException("Invalid Operation Exception");
                        if(SongPlayingOrder.valueOf("Back") == songPlayingOrder){
                            if(j == 0){
                                j = n-1;
                            }else {
                                j--;
                            }
                        }else {
                            if(j == n){
                                j = 0;
                            }else{
                                j++;
                            }
                        }
                        currSong = songs.get(j);                        
                        userPlayedSongDto.setData(user.getName(), currSong.getName(), currSong.getAlbumName(), currSong.getArtist().get(0));
                        return userPlayedSongDto;
                    }
                }
            }
        }

        if(!noPlayListIsPlaying) throw new InvalidOperationException("Invalid operation!");
        return null;
    }
    
}
 
