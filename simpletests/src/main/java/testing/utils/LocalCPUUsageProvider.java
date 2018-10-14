package testing.utils;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.testng.annotations.Test;

import com.loadcoder.result.TransactionExecutionResult;
import com.loadcoder.result.clients.GrafanaClient;
import com.loadcoder.result.clients.InfluxDBClient;
import com.loadcoder.result.clients.InfluxDBClient.InfluxDBTestExecution;

public class LocalCPUUsageProvider {
	
	MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
	ObjectName name;
	
	public LocalCPUUsageProvider(){

	    try {
			name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Thread getInfluxDBUpdatingThread(InfluxDBTestExecution influxDBTestExecution) {
		Thread t = new Thread(()-> {
			

			Map<String, List<TransactionExecutionResult>> map = new HashMap<String, List<TransactionExecutionResult>>();
			List<TransactionExecutionResult> l = new ArrayList<TransactionExecutionResult>();
			map.put("CPU", l);
			
			while(true) {
				 double cpuLoad = getProcessCpuLoad();
				 int cpuLoadPercent = (int)(cpuLoad *100);
					TransactionExecutionResult cpuTransaction = new TransactionExecutionResult(
							System.currentTimeMillis(), cpuLoadPercent, true, null);
					l.add(cpuTransaction);
			
				influxDBTestExecution.writeTransactions(map);
				l.clear();
				sleep(1000);
			}
		});
		return t;
	}
	
	public double getProcessCpuLoad() {

		AttributeList list;
		try {
			list = mbs.getAttributes(name, new String[]{ "SystemCpuLoad" });
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 

	    if (list.isEmpty())     return Double.NaN;

	    Attribute att = (Attribute)list.get(0);
	    Double value  = (Double)att.getValue();
	    return value;
	}
	
	@Test
	void getCPULoadTest(Method method){
		double load = getProcessCpuLoad();
		System.out.println("load" + load);
		sleep(5000);
		load = getProcessCpuLoad();
		System.out.println("load" + load);
		
	}
	
	@Test
	void testGrafanaInfluxDB(Method method){
		String db = "stefansDB";
		InfluxDBClient client = new InfluxDBClient("localhost", 8086, false, db);
		GrafanaClient grafana = new GrafanaClient("localhost", 3000, false, "Basic YWRtaW46YWRtaW4=");

		String executionId=  method.getName()+System.currentTimeMillis();
		InfluxDBTestExecution influxDBTestExecution = client.createTestExecution(executionId);

		grafana.createNewDashboard( method.getName(), method.getName() + System.currentTimeMillis(), Arrays.asList("CPU"));
		Thread t = getInfluxDBUpdatingThread(influxDBTestExecution);
		t.start();
		sleep(60_000);

	}
	
	void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
