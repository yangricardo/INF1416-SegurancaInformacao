package model;

public class User {
	private int idUser;
	private String loginname;
	private String username;
	private String password;
	private String salt;
	private int idGroup;
	private String digitalcertificate;
	private boolean isBlocked;
	private String blockedSince;
	private int contLoginError;
	private int numberOfAccess;
	private int numberOfSearchesKeys;
	private int numberOfSearchesFiles;
	
	public User(int idUser, String loginname, String username, String password, String salt, int idGroup,
			String digitalcertificate, boolean isBlocked, String blockedSince, int contLoginError, int numberOfAccess,
			int numberOfSearchesKeys, int numberOfSearchesFiles) {
		super();
		this.idUser = idUser;
		this.loginname = loginname;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.idGroup = idGroup;
		this.digitalcertificate = digitalcertificate;
		this.isBlocked = isBlocked;
		this.blockedSince = blockedSince;
		this.contLoginError = contLoginError;
		this.numberOfAccess = numberOfAccess;
		this.numberOfSearchesKeys = numberOfSearchesKeys;
		this.numberOfSearchesFiles = numberOfSearchesFiles;
	}

	public int getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(int idGroup) {
		this.idGroup = idGroup;
	}

	public int getNumberOfSearchesKeys() {
		return numberOfSearchesKeys;
	}

	public void setNumberOfSearchesKeys(int numberOfSearchesKeys) {
		this.numberOfSearchesKeys = numberOfSearchesKeys;
	}

	public int getNumberOfSearchesFiles() {
		return numberOfSearchesFiles;
	}

	public void setNumberOfSearchesFiles(int numberOfSearchesFiles) {
		this.numberOfSearchesFiles = numberOfSearchesFiles;
	}

	public User(String loginname, String username, String password, String salt, int idGroup,
			String digitalcertificate) {
		super();
		this.loginname = loginname;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.idGroup = idGroup;
		this.digitalcertificate = digitalcertificate;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public int getIdUserGroup() {
		return idGroup;
	}

	public void setIdUserGroup(int idUserGroup) {
		this.idGroup = idUserGroup;
	}

	public String getDigitalcertificate() {
		return digitalcertificate;
	}

	public void setDigitalcertificate(String digitalcertificate) {
		this.digitalcertificate = digitalcertificate;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public String getBlockedSince() {
		return blockedSince;
	}

	public void setBlockedSince(String blockedSince) {
		this.blockedSince = blockedSince;
	}

	public int getContLoginError() {
		return contLoginError;
	}

	public void setContLoginError(int contLoginError) {
		this.contLoginError = contLoginError;
	}

	public int getNumberOfAccess() {
		return numberOfAccess;
	}

	public void setNumberOfAccess(int numberOfAccess) {
		this.numberOfAccess = numberOfAccess;
	}	
}
