package fulltest.utils;

import java.lang.management.ManagementFactory;

import javax.management.Attribute;
import javax.management.AttributeChangeNotification;
import javax.management.AttributeList;
import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class ResourceUtils {

	
	public static void main(String[] args) {
		try {
			getClient();
//			System.out.println("cpuload:" + cpuLoad);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	public static void echo(String msg) {
		System.out.println(msg);
	} 
	
	public static void getClient() {
		try {
			// create jmx connection with mules jmx agent
			JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi");
			JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
			jmxc.connect();
			 
			//create object instances that will be used to get memory and operating system Mbean objects exposed by JMX; create variables for cpu time and system time before
			Object memoryMbean = null;
			Object processCPU = null;
			Object systemCPU = null;
			long cpuBefore = 0;
			long tempMemory = 0;
			CompositeData cd = null;
			 
//			cpuBefore = Long.parseLong(a.toString());
			 while(true) {
				// call the garbage collector before the test using the Memory Mbean
//				jmxc.getMBeanServerConnection().invoke(new ObjectName("java.lang:type=Memory"), "gc", null, null);
				int samplesCount =100;
				//create a loop to get values every second (optional)
				for (int i = 0; i < samplesCount; i++) {
				 
				//get an instance of the HeapMemoryUsage Mbean
				memoryMbean = jmxc.getMBeanServerConnection().getAttribute(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage");
				cd = (CompositeData) memoryMbean;
				//get an instance of the OperatingSystem Mbean
				processCPU = jmxc.getMBeanServerConnection().getAttribute(new ObjectName("java.lang:type=OperatingSystem"),"ProcessCpuLoad");
				systemCPU = jmxc.getMBeanServerConnection().getAttribute(new ObjectName("java.lang:type=OperatingSystem"),"SystemCpuLoad");
				System.out.println("Used memory: " + " " + cd.get("used") + " Used cpu: " + (double)processCPU * 100 + " OS cpu: " + (double)systemCPU * 100); //print memory usage
				tempMemory = tempMemory + Long.parseLong(cd.get("used").toString());
				 
				
				Thread.sleep(2000);
			}
			}
		}catch(Exception e) {
			System.out.println(e);
		}
	}
}
