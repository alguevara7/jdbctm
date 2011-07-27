package net.halfrunt.jdbctm;

import java.sql.SQLException;

public interface JdbcTmSession {
	
	public void release() throws SQLException;

}
