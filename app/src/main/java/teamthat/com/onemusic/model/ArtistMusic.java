package teamthat.com.onemusic.model;

/**
 * Created by thietit on 11/1/2016.
 */

public class ArtistMusic {

    String nameMusic;
    String musicPath;
    String id;
    String artistId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArtistMusic() {

    }

    public ArtistMusic(String nameMusic, String musicPath) {
        this.nameMusic = nameMusic;
        this.musicPath = musicPath;
    }
    public ArtistMusic(String nameMusic, String musicPath,String id) {
        this.nameMusic = nameMusic;
        this.musicPath = musicPath;
        this.id =id;
    }

    public ArtistMusic(String nameMusic, String musicPath, String id, String artistId) {
        this.nameMusic = nameMusic;
        this.musicPath = musicPath;
        this.id = id;
        this.artistId = artistId;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getNameMusic() {
        return nameMusic;
    }

    public void setNameMusic(String nameMusic) {
        this.nameMusic = nameMusic;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    @Override
    public String toString() {
        return this.nameMusic;
    }
}
