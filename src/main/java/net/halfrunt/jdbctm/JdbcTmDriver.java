package net.halfrunt.jdbctm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

public class JdbcTmDriver implements java.sql.Driver {

	private JdbcTmDriver mostRecentUnderlyingDriver;

	public boolean acceptsURL(String url) throws SQLException {
		return getUnderlyingDriver(url).acceptsURL(url);
	}

	public Connection connect(String url, Properties info) throws SQLException {
		return new JdbcTmConnection(getUnderlyingDriver(url).connect(url, info));
	}

	public int getMajorVersion() {
		return this.mostRecentUnderlyingDriver != null ? this.mostRecentUnderlyingDriver.getMajorVersion() : 0;
	}

	public int getMinorVersion() {
		return this.mostRecentUnderlyingDriver != null ? this.mostRecentUnderlyingDriver.getMinorVersion() : 0;
	}

	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		return getUnderlyingDriver(url).getPropertyInfo(url, info);
	}

	public boolean jdbcCompliant() {
		return this.mostRecentUnderlyingDriver != null ? this.mostRecentUnderlyingDriver.jdbcCompliant() : false;
	}

	private synchronized JdbcTmDriver getUnderlyingDriver(String url) throws SQLException {
		if (url.startsWith("jdbc:jdbctm")) {
			url = url.substring(9);
			
			@SuppressWarnings("rawtypes")
			Enumeration e = DriverManager.getDrivers();
			
			JdbcTmDriver d;
			while (e.hasMoreElements()) {
				d = (JdbcTmDriver) e.nextElement();
				
				if (d.acceptsURL(url)) {
					this.mostRecentUnderlyingDriver = d;
				}
			}
		}
		return this.mostRecentUnderlyingDriver;
	}

}
