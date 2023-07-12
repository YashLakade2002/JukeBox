package com.crio.jukebox.services;

import java.io.IOException;

public interface ISongService {
    public void loadSong(String fileName) throws IOException;
    
}
