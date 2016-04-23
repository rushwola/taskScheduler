package  com.ilin.task;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ilin.localization.StringManager;


/**
 *
 * @author johnnyKing.39
 *
 */
public class Host {
	private static final String HOST_IP_PATTERN = "[0-9\\\\.]+";
	private static final Logger logger = Logger.getLogger(Host.class);
	private static final StringManager sm = StringManager.getManager(Constance.PACKAGE);
	public static List<String> getAllIp() {
		Enumeration<NetworkInterface> interfaces = null;
		ArrayList<String> ipList = new ArrayList<String>();
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
			if (null == interfaces) {
				return ipList;
			}
			while (interfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) interfaces.nextElement();
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					InetAddress ip = (InetAddress) ips.nextElement();
					if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
						Pattern p = Pattern.compile(HOST_IP_PATTERN);
						Matcher m = p.matcher(ip.toString());
						while(m.find()) {
							ipList.add(m.group());
						}
					}
				}
			}
		}
		catch (Exception e) {
			logger.fatal(sm.getString("hostIpException"),e);
		}
		return ipList;
	}
}