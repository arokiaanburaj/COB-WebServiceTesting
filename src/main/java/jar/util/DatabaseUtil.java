/*******************************************************************************

 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven approach
 *                

 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE
 *
 * You should have received a copy of the GNU General Public License along with this program in the name of LICENSE.txt in the root folder of the distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 *
 * See the NOTICE.TXT file in root folder of this source files distribution 
 * for additional information regarding copyright ownership and licenses

 *

 *******************************************************************************/

package jar.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import jar.core.ConfigurationManager;

/**
 * Database class for DatabaseUtil Created on Jan 29, 2010
 * 
 * @author Chirag Jayswal
 */

public class DatabaseUtil {
	private static Logger log = Logger.getLogger(DatabaseUtil.class);

	public static void close(PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.clearWarnings();
				ps.clearParameters();
				ps.close();
			}
		} catch (SQLException sqle) {
			log.error("An error occurred while attempting to close a PreparedStatement", sqle);
		}
	}

	public static void close(Statement s) {
		try {
			if (s != null) {
				s.close();
			}
		} catch (SQLException sqle) {
			log.error("An error occurred while attempting to close a SQLStatement", sqle);
		}
	}

	public static void close(PreparedStatement ps, ResultSet rs) {
		close(rs);
		close(ps);
	}

	public static void close(Statement st, ResultSet rs) {
		close(rs);
		close(st);
	}

	public static void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException sqle) {
			log.error("An error occurred while attempting to close a ResultSet", sqle);
		}
	}

	public static void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException se) {
			log.error("An error occurred while attempting to close a database connection", se);
		}
	}

	/**
	 * For default data base configuration, Creates data base connection using following properties:
	 * <ol>
	 * <li>db.driver.class</li>
	 * <li>db.connection.url</li>
	 * <li>db.user</li>
	 * <li>db.pwd</li>
	 * </ol>
	 * 
	 * @return {@link Connection} object
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		PropertyUtil props = ConfigurationManager.getBundle();
		String url = props.getString("db.connection.url");
		String driverclass = props.getString("db.driver.class");
		String user = props.getString("db.user");
		String pwd = props.getString("db.pwd");
		return getConnection(driverclass, url, user, pwd);
	}

	/**
	 * If you want to use multiple data bases in the project. provide different configuration with different prefix. For example
	 * <p>defualt</p>
	 * <ol>
	 * <li>db.driver.class</li>
	 * <li>db.connection.url</li>
	 * <li>db.user</li>
	 * <li>db.pwd</li>
	 * </ol>
	 * <p>another database configuration</p>
	 * <ol>
	 * <li>con1.db.driver.class</li>
	 * <li>con1.db.connection.url</li>
	 * <li>con1.db.user</li>
	 * <li>con1.db.pwd</li>
	 * </ol>
	 * to use another configuration: <code>{@link #getConnection(String) getConnection("con1")}
	 * @param prefix - prefix of the database configuration to be used
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection(String prefix) throws Exception {
		Configuration props = StringUtil.isBlank(prefix) ? ConfigurationManager.getBundle()
				: ConfigurationManager.getBundle().subset(prefix);
		String url = props.getString("db.connection.url");
		String driverclass = props.getString("db.driver.class");
		String user = props.getString("db.user");
		String pwd = props.getString("db.pwd");
		return getConnection(driverclass, url, user, pwd);
	}

	/**
	 * @param driverCls
	 * @param url
	 * @param user
	 * @param pwd
	 * @return {@link Connection} object
	 * @throws Exception
	 */
	public static Connection getConnection(String driverCls, String url, String user, String pwd) throws Exception {
		Connection con = null;
		Class.forName(driverCls);// loads the driver
		con = DriverManager.getConnection(url, user, pwd);
		return con;
	}

	public static Object[][] getData(String query) {
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DatabaseUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				int colsCnt = rs.getMetaData().getColumnCount();
				Object[] cols = new Object[colsCnt];
				for (int indx = 0; indx < colsCnt; indx++) {
					cols[indx] = rs.getObject(indx + 1);
				}
				rows.add(cols);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(stmt, rs);
			DatabaseUtil.close(con);
		}

		return rows.toArray(new Object[][] {});
	}

	public static Object[][] getRecordDataAsMap(String query) {
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DatabaseUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				HashMap<String, String> map = new HashMap<String, String>();

				int colsCnt = rs.getMetaData().getColumnCount();
				for (int indx = 1; indx <= colsCnt; indx++) {
					map.put(rs.getMetaData().getColumnLabel(indx), String.valueOf(rs.getObject(indx)));
				}
				rows.add(new Object[] { map });
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(stmt, rs);
			DatabaseUtil.close(con);
		}
		return rows.toArray(new Object[][] {});
	}
	
	/**
	 * use this method when you want to run query using default database configuration
	 * @param query
	 * @return
	 */
	public static List<Map<String, Object>> getRecordAsMap(String query) {
		return getRecordAsMap("", query);
	}

	/**
	 * Use this method if you have multiple database configuration. Provide prefix of the configuration to be used. 
	 * @see #getConnection(String)
	 * @param connectionPrefix
	 * @param query
	 * @return
	 */
	public static List<Map<String, Object>> getRecordAsMap(String connectionPrefix,String query) {
		ArrayList<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DatabaseUtil.getConnection(connectionPrefix);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();

				int colsCnt = rs.getMetaData().getColumnCount();
				for (int indx = 1; indx <= colsCnt; indx++) {
					map.put(rs.getMetaData().getColumnLabel(indx), rs.getObject(indx));
				}
				rows.add(map);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(stmt, rs);
			DatabaseUtil.close(con);
		}
		return rows;
	}
}
