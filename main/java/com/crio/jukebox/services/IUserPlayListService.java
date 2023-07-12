package com.crio.jukebox.services;

import java.util.List;

import com.crio.jukebox.dtos.UserPlayedSongDto;
import com.crio.jukebox.entities.PlayList;
import com.crio.jukebox.entities.SongPlayingOrder;

import com.crio.jukebox.exceptions.InvalidOperationException;
import com.crio.jukebox.exceptions.UserNotFoundException;
import com.crio.jukebox.exceptions.PlayListNotFoundException;
import com.crio.jukebox.exceptions.SongNotFoundException;


public interface IUserPlayListService {
    public PlayList createPlayList(String userId, String playListName, List<String> csong) throws UserNotFoundException, SongNotFoundException;
    public void deletePlayList(String userId, String playListId) throws UserNotFoundException, PlayListNotFoundException;
    public PlayList addSongToPlayList(String userId, String playListId, List<String> songIds) throws UserNotFoundException, SongNotFoundException, PlayListNotFoundException;
    public PlayList deleteSongFromPlayList(String userId, String playListId, List<String> songIds) throws UserNotFoundException, SongNotFoundException, PlayListNotFoundException;
    public UserPlayedSongDto setCurrentPlayList(String userId, String playListId) throws UserNotFoundException, InvalidOperationException, PlayListNotFoundException;
    public UserPlayedSongDto playSongById(String userId, String songId) throws UserNotFoundException, InvalidOperationException, SongNotFoundException;
    public UserPlayedSongDto playSongByOrder(String userId, SongPlayingOrder songPlayingOrder) throws InvalidOperationException, UserNotFoundException;
}