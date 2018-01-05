package pl.poznan.put.cs.si.puttalky;

import java.util.ArrayList;

/** Author: agalawrynowicz<br>
 * Date: 19-Dec-2016 */

public class Fakt {
	
	private String nazwa;
	private ArrayList<String> wartosc;
	
	public Fakt() {
		this.wartosc = new ArrayList<String>();
	}
	
	public Fakt(String nazwa, String wartosc) {
		this.nazwa = nazwa;
		this.wartosc = new ArrayList<String>();
		this.wartosc.add(wartosc);
	}

	public String getNazwa() {
        return this.nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public ArrayList<String> getWartosc() {
        return this.wartosc;
    }

    public void setWartosc(String wartosc) {
    	this.wartosc.add(wartosc);
    }
}
