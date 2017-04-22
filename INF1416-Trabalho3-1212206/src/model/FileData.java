package model;

import utils.Util;

public class FileData {

	private FileStatus fileStatus;
	private byte[] seed;
	private byte[] content;
	private String status;
	private String signature;
	
	public FileData() {
		super();
		this.fileStatus = FileStatus.FILEENVNOTFOUND;
		this.seed = null;
		this.content = null;
		this.status = "NOT OK";
		this.signature = "";
	}
		
	public FileStatus getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(FileStatus fileStatus) {
		this.fileStatus = fileStatus;
	}

	public byte[] getSeed() {
		return seed;
	}
	
	public String getSeedUTF8() {
		return (seed!=null?Util.convertToUTF8(seed):"");
	}
	
	public String getSeedHEX() {
		return (seed!=null?Util.convertToHex(seed):"");
	}

	public void setSeed(byte[] seed) {
		this.seed = seed;
	}

	public byte[] getContent() {
		return content;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
}
