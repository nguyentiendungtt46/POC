package cic.h2h.form;

import entity.Index;
import frwk.form.ModelForm;

public class IndexForm extends ModelForm<Index> {
	Index idx = new Index();
	
	public Index getIdx() {
		return idx;
	}

	public void setIdx(Index idx) {
		this.idx = idx;
	}

	@Override
	public Index getModel() {
		// TODO Auto-generated method stub
		return idx;
	}

}
