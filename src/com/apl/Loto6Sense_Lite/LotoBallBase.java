package com.apl.Loto6Sense_Lite;

public class LotoBallBase {

    /** �ԍ� */
    private int number;
    
    /** �L��/���� */
    private boolean enabled;

    /** Status�R�[�h */
    private int status;
    
    /** ���o����Ă����� */
    private boolean selected;
    
	public LotoBallBase(int inNumber){
		number = inNumber;
		enabled = true;
		selected = false;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
