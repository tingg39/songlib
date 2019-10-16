// Ting Yao and Kshitij Bafna
package test1.view;

public class Song
{
	private String songname;
	private String artist;
	private String album;
	private String year;
	
	public Song(String s1, String a1, String a2, String y1)
	{
		songname = s1;
		artist = a1;
		album = a2;
		year = y1;
	}
	
	public void setSongname(String s1)
	{
		songname = s1;
	}
	
	public void setArtist(String a1)
	{
		artist = a1;
	}
	
	public void setAlbum(String a2)
	{
		album = a2;
	}
	
	public void setYear(String y1)
	{
		year = y1;
	}
	
	public String getSongname()
	{
		return songname;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getAlbum()
	{
		return album;
	}
	
	public String getYear() 
	{
		return year;
	}
	
	public String writeTxt()
	{
		return (songname + ";" + artist + ";" + album + ";" + year);
	}
	
	public String toString()
	{
		return songname + " - " + artist;
	}
}