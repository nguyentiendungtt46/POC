package cic.h2h.form;

import org.springframework.web.multipart.MultipartFile;

import frwk.form.ModelForm;

public class TestForm extends ModelForm{ 
	private static final long serialVersionUID = 1L;
	private MultipartFile inputFile;
	public MultipartFile getInputFile() {
		return inputFile;
	}
	public void setInputFile(MultipartFile inputFile) {
		this.inputFile = inputFile;
	}
	@Override
	public Object getModel() {
		return null;
	}
}
