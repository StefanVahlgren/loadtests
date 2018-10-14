package dto;

import java.util.Random;

public class Odds {

		public String getHemma() {
		return hemma;
	}

	public void setHemma(String hemma) {
		this.hemma = hemma;
	}

	public String getBorta() {
		return borta;
	}

	public void setBorta(String borta) {
		this.borta = borta;
	}

	public double getEtt() {
		return ett;
	}

	public void setEtt(double ett) {
		this.ett = ett;
	}

	public double getKryss() {
		return kryss;
	}

	public void setKryss(double kryss) {
		this.kryss = kryss;
	}

	public double getTva() {
		return tva;
	}

	public void setTva(double tva) {
		this.tva = tva;
	}

		String hemma;
		String borta;
		double ett;
		double kryss;
		double tva;
		
		public Odds() {
		
		}		
		
		public Odds(String hemma, String borta) {
			this.hemma = hemma;
			this.borta = borta;
			
			ett = new Double("" + Utils.randInt(1, 15) + "." + Utils.randInt(0,9)+ Utils.randInt(0, 9));
			kryss = new Double("" + Utils.randInt(1, 15) + "." + Utils.randInt(0,9)+ Utils.randInt(0, 9));
			tva = new Double("" + Utils.randInt(1, 15) + "." + Utils.randInt(0,9)+ Utils.randInt(0, 9));

		}
		
}
