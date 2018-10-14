package fulltest.utils;

import java.util.ArrayList;
import java.util.List;

public class TestJMXApp {

	public static String getMega() {
		String s = "1234567890";
		String kb = "";
		String mb = "";
		for(int i =0; i<1024; i++)
			kb = kb + s;
		for(int i =0; i<1024; i++)
			mb = mb + kb;
		return mb;
	}
	
	static String mega = getMega();
	public static void main(String[] args) {
		
		List<String> a = new ArrayList<String>();
		while (true) {
			System.out.println("sleeping...");
			try {
				
//				String result = s;
				for(int i =0; i<60; i++) {
//					result = result +s;
					a.add(mega);
					Thread.sleep(1000);	
				}
				a.clear();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
