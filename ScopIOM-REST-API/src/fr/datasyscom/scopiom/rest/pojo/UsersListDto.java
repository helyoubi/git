package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UsersListDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> usersList;

	public List<String> getUsersList() {
		return usersList;
	}
	
	

}
