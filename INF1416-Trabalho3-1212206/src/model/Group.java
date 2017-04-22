package model;

public class Group {

	private int idGroup;
	private String groupname;
	public Group() {
	}

	public Group(int idGroup, String groupname) {
		this.idGroup = idGroup;
		this.groupname = groupname;
	}
	
	public int getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(int idGroup) {
		this.idGroup = idGroup;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
}
