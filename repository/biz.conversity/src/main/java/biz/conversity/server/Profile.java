package biz.conversity.server;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Profile implements Serializable{
	static final long serialVersionUID = 0;
	private final String email;
	private String firstName;
	private String lastName;
	private String major;
	private String gradClass;
	private BufferedImage profilePicture;
	private BufferedImage backdropPicture;
	private final List<BufferedImage> pictures;
	
	public Profile(String email) {
		this.email = email;
		this.pictures = new ArrayList<BufferedImage>();
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public String getMajor() {
		return this.major;
	}
	
	public String getGradClass() {
		return this.gradClass;
	}
	
	public BufferedImage getProfilePicture() {
		return this.profilePicture;
	}
	
	public BufferedImage getBackdropPicture() {
		return this.backdropPicture;
	}
	
	public List<BufferedImage> getPictures() {
		return this.pictures;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setMajor(String major) {
		this.major = major;
	}
	
	public void setGradClass(String gradClass) {
		this.gradClass = gradClass;
	}
	
	public void setProfilePicture(BufferedImage profilePicture) {
		this.profilePicture = profilePicture;
	}
	
	public void setBackdropPicture(BufferedImage backdropPicture) {
		this.backdropPicture = backdropPicture;
	}
	
	public void addPicture(BufferedImage picture) {
		pictures.add(picture);
	}
	
	public void removePicture(BufferedImage picture) {
		if (pictures.contains(picture)) {
			pictures.remove(picture);
		}
	}
	
	@Override
	public String toString() {
		String string = this.email;
		return string;
	}
}
