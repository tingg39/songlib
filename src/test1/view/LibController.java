package test1.view;

import java.util.*;

import java.io.*;
import javafx.event.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LibController
{
	@FXML ListView<Song> listview;
	@FXML Label artist;
	@FXML Label album;
	@FXML Label year;
	@FXML TextField editsong;
	@FXML TextField editartist;
	@FXML TextField editalbum;
	@FXML TextField edityear;
	@FXML Button addsong;
	@FXML Button deletesong;
	@FXML Button editinfo;
	
	private ObservableList<Song> songlist = null;
	private Stage mainStage;
	
	public void start(Stage mStage) throws Exception
	{
		mainStage = mStage;
		songlist = FXCollections.observableArrayList(readTxt("list.txt"));
		FXCollections.sort(songlist, new compareSong());
		listview.setItems(songlist);
		listview.getSelectionModel().select(0);
		Song currentSong =listview.getSelectionModel().getSelectedItem();
		if(currentSong == null)
		{
			artist.setText("");
			album.setText("");
			year.setText("");
		}else
		{
			songDetail(currentSong);
		}
		editsong.setText("");
		editartist.setText("");
		editalbum.setText("");
		edityear.setText("");
		listview.getSelectionModel().selectedItemProperty().addListener((slist, oldVal, newVal) -> songDetail(newVal));
		
	}
	
	public List<Song> readTxt(String fname) throws Exception
	{
		String token;
		String listofsong[];
		List<Song> filelist = new ArrayList<Song>();
		BufferedReader br = new BufferedReader(new FileReader(fname));
		while((token = br.readLine()) != null)
		{
			listofsong = token.split(";");
			Song temp = new Song(listofsong[0], listofsong[1], listofsong[2], listofsong[3]);
			filelist.add(temp);
			listofsong = null;
		}
		br.close();
		return filelist;
	}
	
	
	public void songDetail(Song s)
	{
		artist.setText(s.getArtist());
		album.setText(s.getAlbum());
		year.setText(s.getYear());
	}
	
	public void addSong(ActionEvent e)
	{
		int size = songlist.size();
		String songname = editsong.getText();
		String artist = editartist.getText();
		String album = editalbum.getText();
		String year = edityear.getText();
		int i;
		for(i = 0; i < size; i++)
		{
			Song temp = songlist.get(i);
			if(songname.equals(temp.getSongname()))
			{
				Alert al = new Alert(AlertType.CONFIRMATION);
				al.setTitle("Alert");
				al.setHeaderText("you are going to add a song to the list");
				Optional<ButtonType> res = al.showAndWait();
				if(res.get() == ButtonType.OK)
				{
					Song tempsong = new Song(songname, artist, album, year);
					songlist.add(tempsong);
					FXCollections.sort(songlist, new compareSong());
					listview.setItems(songlist);
				}
			}
		}
	}
	
	public void deleteSong()
	{
		
	}
	
	public void editSong()
	{
		
	}
	
	class compareSong implements Comparator<Song>{
		public int compare(Song s1, Song s2)
		{
			int result = s1.getSongname().compareToIgnoreCase(s2.getSongname());
			return result;
		}
	}
}