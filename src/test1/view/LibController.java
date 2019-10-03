// Ting Yao and Kshitij Bafna
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
		listview.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> songDetail(newVal));
		mainStage.setOnCloseRequest(event -> 
		{
			try {
				FileWriter intoTxt = new FileWriter("list.txt");
				int i;
				for(i = 0; i < songlist.size(); i++)
				{
					if(songlist.get(i).getAlbum().contentEquals(""))
						songlist.get(i).setAlbum(" ");
					if(songlist.get(i).getYear().contentEquals(""))
						songlist.get(i).setYear(" ");
					intoTxt.write(songlist.get(i).writeTxt() + "\n");
				}
				intoTxt.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		});
		
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
		if(s == null)
			return;
		artist.setText(s.getArtist());
		album.setText(s.getAlbum());
		year.setText(s.getYear());
	}
	
	public void addSong(ActionEvent e)
	{
		int size = songlist.size();
		String songname = editsong.getText();
		if(songname == null || songname.equals(""))
		{
			Alert serr = new Alert(AlertType.INFORMATION);
			serr.setTitle("Error");
			serr.setHeaderText("need a song name");
			Optional<ButtonType> ser = serr.showAndWait();
			return;
		}
		String artist = editartist.getText();
		if(artist == null || artist.equals(""))
		{
			Alert arterr = new Alert(AlertType.INFORMATION);
			arterr.setTitle("Error");
			arterr.setHeaderText("need an artist name");
			Optional<ButtonType> aer = arterr.showAndWait();
			return;
		}
		String album = editalbum.getText();
		String year = edityear.getText();
		if(!year.contentEquals(""))
		{
			try
			{
				Integer.parseInt(year);
			}catch(NumberFormatException ex)
			{
				Alert numerr = new Alert(AlertType.INFORMATION);
				numerr.setTitle("Error");
				numerr.setHeaderText("year must be a number");
				Optional<ButtonType> ner = numerr.showAndWait();
				return;
			}
		}
		int i;
		for(i = 0; i < size; i++)
		{
			Song temp = songlist.get(i);
			if(songname.equals(temp.getSongname()) && artist.equals(temp.getArtist()))
			{
				Alert alerror = new Alert(AlertType.INFORMATION);
				alerror.setTitle("Error");
				alerror.setHeaderText("song already exists in the library");
				Optional<ButtonType> aerr = alerror.showAndWait();
					return;
			}
		}
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
			listview.getSelectionModel().select(findSong(songname));
			songDetail(tempsong);
			editsong.setText("");
			editartist.setText("");
			editalbum.setText("");
			edityear.setText("");
		}
	}
	
	public void deleteSong(ActionEvent e)
	{
		Song currentSong = listview.getSelectionModel().getSelectedItem();
		int size = songlist.size();
		if(size == 0)
		{
			Alert err = new Alert(AlertType.INFORMATION);
			err.setTitle("Error");
			err.setHeaderText("there is no song in the libaray");
			Optional<ButtonType> er = err.showAndWait();
			return;
		}
		Alert al = new Alert(AlertType.CONFIRMATION);
		al.setTitle("Alert");
		al.setHeaderText("you are going to delete the current song in the list");
		Optional<ButtonType> res = al.showAndWait();
		
		if(res.get() == ButtonType.OK)
		{
			for(int i = 0; i < size; i++)
			{
				Song temp = songlist.get(i);

				if(currentSong.getSongname().equals(temp.getSongname()) && currentSong.getArtist().equals(temp.getArtist()))
				{
						songlist.remove(i,i+1);
						listview.setItems(songlist);
						if(songlist.size() == 0)
						{
							artist.setText("");
							album.setText("");
							year.setText("");
						}
						break;
				}
			}
			
		}
	}
	
	public void editSong(ActionEvent e)
	{
		Song currentSong = listview.getSelectionModel().getSelectedItem();
		int size = songlist.size();
		if(size == 0)
		{
			Alert err = new Alert(AlertType.INFORMATION);
			err.setTitle("Error");
			err.setHeaderText("there is no song in the libaray");
			Optional<ButtonType> er = err.showAndWait();
			return;
		}
		String new_songname = editsong.getText();
		String new_artist = editartist.getText();
		String new_year = edityear.getText();
		String new_album = editalbum.getText();
		if(!new_year.contentEquals(""))
		{
			try
			{
				Integer.parseInt(new_year);
			}catch(NumberFormatException ex)
			{
				Alert numerr = new Alert(AlertType.INFORMATION);
				numerr.setTitle("Error");
				numerr.setHeaderText("year must be a number");
				Optional<ButtonType> ner = numerr.showAndWait();
				return;
			}
		}
		
		for(int i = 0; i < size; i++)
		{
			Song temp = songlist.get(i);

			if(new_songname.equals(temp.getSongname()) && new_artist.equals(temp.getArtist()))
			{
				Alert alerror = new Alert(AlertType.INFORMATION);
				alerror.setTitle("Error");
				alerror.setHeaderText("song already exists in the library");
				Optional<ButtonType> aerr = alerror.showAndWait();
					return;
			}
		}
		Alert al = new Alert(AlertType.CONFIRMATION);
		al.setTitle("Alert");
		al.setHeaderText("you are going to edit the current song in the list");
		Optional<ButtonType> res = al.showAndWait();
		if(res.get() == ButtonType.OK)
		{
			if(new_songname != null && !new_songname.equals(""))
				currentSong.setSongname(new_songname);
			if(new_artist != null && !new_artist.equals(""))
				currentSong.setArtist(new_artist);
			if(new_album != null && !new_album.equals(""))
				currentSong.setAlbum(new_album);
			if(new_year != null && !new_year.equals(""))
				currentSong.setYear(new_year);
			FXCollections.sort(songlist, new compareSong());
			listview.setItems(songlist);
			listview.getSelectionModel().select(findSong(currentSong.getSongname()));
			songDetail(currentSong);
			editsong.setText("");
			editartist.setText("");
			editalbum.setText("");
			edityear.setText("");
		}
	}
	
	public int findSong(String name)
	{
		int index;
		for(index = 0; index < songlist.size(); index++)
		{
			if(name.equalsIgnoreCase(songlist.get(index).getSongname()))
			{
				break;
			}
		}
		return index;
	}
	
	class compareSong implements Comparator<Song>{
		public int compare(Song s1, Song s2)
		{
			int result = s1.getSongname().compareToIgnoreCase(s2.getSongname());
			return result;
		}
	}
}