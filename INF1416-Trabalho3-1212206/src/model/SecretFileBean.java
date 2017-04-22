package model;

import javafx.beans.property.SimpleStringProperty;

public class SecretFileBean {

	private final SimpleStringProperty secretName;
	private final SimpleStringProperty codeName;
	private final SimpleStringProperty digitalSignature;
	private final SimpleStringProperty digitalEnvelope;
	private final SimpleStringProperty status;
	
	public SecretFileBean(String secretName, String codeName, String digitalSignature,
			String digitalEnvelope,String status) {
		super();
		this.secretName =  new SimpleStringProperty(secretName);
		this.codeName =  new SimpleStringProperty(codeName);
		this.digitalSignature =  new SimpleStringProperty(digitalSignature);
		this.digitalEnvelope =  new SimpleStringProperty(digitalEnvelope);
		this.status = new SimpleStringProperty(status);
	}

	public String getSecretName() {
		return secretName.get();
	}

	public String getCodeName() {
		return codeName.get();
	}

	public String getDigitalSignature() {
		return digitalSignature.get();
	}

	public String getDigitalEnvelope() {
		return digitalEnvelope.get();
	}

	public String getStatus() {
		return status.get();
	}
}
