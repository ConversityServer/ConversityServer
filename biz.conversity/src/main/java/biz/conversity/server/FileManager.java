package biz.conversity.server;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileManager {
	private static FileManager instance;
	
	private FileManager() {}
	
	public static FileManager getInstance() {
		if (instance == null) {
			instance = new FileManager();
		}
		return instance;
	}
	
	public void saveDataXML(Object object, String filename){
        XMLEncoder encoder;
        try{
        	System.out.println("Try to Encode..");
            encoder = new XMLEncoder(new FileOutputStream(filename));
            System.out.println("Try to Write Objects..");
            encoder.writeObject(object);
            System.out.println("Close..");
            encoder.close();
        }
        catch(FileNotFoundException e){
            System.err.println("File '" + filename + "' not found.");
        }
    }
	
	public Object loadDataXML(String filename){
        XMLDecoder decoder;
        try{
            decoder = new XMLDecoder(new FileInputStream(filename));
            Object object = decoder.readObject();
            decoder.close();
            return object;
        }
        catch(FileNotFoundException e){
            System.err.println("File '" + filename + "' not found.");
            return null;
        }
    }
	
	public void saveData(Object object, String filename) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.close();
			fos.close();
			System.out.println("Saving " + filename);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Object loadData(String filename) {
		Object object = null;
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			object = ois.readObject();
			ois.close();
			fis.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return object;
		}
		return object;
	}
}
