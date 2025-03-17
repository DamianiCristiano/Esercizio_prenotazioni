package classiMie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class MySQL {

	/**
	 * Classe MySQL per la gestione della connessione al database e l'esecuzione di
	 * query.
	 * 
* ## Metodi principali:
 * - **connectDB()**: Stabilisce la connessione al database.
 * - **isConnected()**: Verifica se la connessione è attiva.
 * - **closeDB()**: Chiude la connessione al database.
 * - **selectQuery(String query)**: Esegue una query di selezione e memorizza il ResultSet.*
 * - **clearErrorString()**: Pulisce il messaggio di errore.
   // nella selectProtectedQuery utilizziamo un array di stringhe perche' potremmo avere di bisogno di aggiungere maggiori parametri.
 * - **selectProtectedQuery(String query, String[] values)**: Esegue una query parametrizzata per prevenire SQL Injection.
 * - **first()**: Si posiziona sul primo risultato del ResultSet.
 * - **getString(String field)**: Restituisce il valore di un campo come String.
 * - **getInteger(String field)**: Restituisce il valore di un campo come Integer.
 * - **getBoolean(String field)**: Restituisce il valore di un campo come Boolean.
 * - **getFloat(String field)**: Restituisce il valore di un campo come Float.
 * - **count()**: Restituisce il numero di righe del ResultSet.
 * - **next()**: Sposta il cursore alla riga successiva del ResultSet.
 * - **last()**: Sposta il cursore all'ultima riga del ResultSet.
 * - **previous()**: Sposta il cursore alla riga precedente del ResultSet.
 * - **absolute(int row)**: Sposta il cursore alla riga specificata del ResultSet.
 * - **updateQuery(String query)**: Esegue un'operazione di aggiornamento nel database.
 * - **updateProtectedQuery(String query, String[] values)**: Esegue un'operazione di aggiornamento sicura con parametri.
 * - **InsertQuery(String query)**: Esegue un'operazione di inserimento nel database.
 * - **InsertProtectedQuery(String query, String[] values)**: Esegue un'operazione di inserimento sicura con parametri.
 * - **getLastInsertId()**: Restituisce l'ID dell'ultimo inserimento.
 * - **DeleteQuery(String query)**: Esegue un'operazione di eliminazione nel database.
 * - **DeleteProtectedQuery(String query, String[] values)**: Esegue un'operazione di eliminazione sicura con parametri.
	 */

	private String dbHost;
	private String dbPort;
	private String dbName;
	private String dbUsername;
	private String dbPassword;

	private Connection dbConn;

	private String errorString;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private Integer lastInsertId;

	public MySQL() {
		this.dbHost = null;
		this.dbPort = null;
		this.dbName = null;
		this.dbUsername = null;
		this.dbPassword = null;
	}

	public MySQL(String dbHost, String dbPort, String dbName, String db, String dbPassword) {
		this.dbHost = dbHost;
		this.dbPort = dbPort;
		this.dbName = dbName;
		this.dbUsername = db;
		this.dbPassword = dbPassword;
		this.connectDB();
		;
	}

	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getErrorString() {
		return errorString;
	}

	public void clearErrorString() {
		this.errorString = null;
	}

	public Connection getDbConn() {
		return dbConn;
	}

    public boolean connectDB() {
        try {
            this.dbConn = DriverManager.getConnection(
                    "jdbc:mysql://" + this.dbHost + ":" + this.dbPort + "/" + 
                    this.dbName , 
                    this.dbUsername, 
                    this.dbPassword);
//            System.out.println(this.dbHost);
//            System.out.println(this.dbPort);
//            System.out.println(this.dbName);
//            System.out.println(this.dbUsername);
//            System.out.println(this.dbPassword);
            return true;
        } catch (Exception e) {
            this.errorString = e.getMessage();
            return false;
        }
    }

	public boolean isConnected() {
		this.clearErrorString();
		try {
			if (this.dbConn == null || this.dbConn.isClosed()) {
				return false;
			}
			return this.dbConn.isValid(0);
		} catch (Exception e) {
			this.errorString = e.getMessage();
			return false;
		}
	}

	public void closeDB() {
		this.clearErrorString();
		try {
			if (this.dbConn != null) {
				this.dbConn.close();
			}
		} catch (Exception e) {
			this.errorString = e.getMessage();
		}
	}

	public ResultSet selectQuery(String query) {
		this.clearErrorString();
		try {
			this.stmt = this.dbConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			this.stmt.executeQuery(query);
			this.rs = this.stmt.getResultSet();
			return this.rs;
		} catch (Exception e) {
			this.errorString = e.getMessage();
			return null;
		}
	}

	public boolean selectProtectedQuery(String query, String[] values) {
		this.clearErrorString();
		try {
			this.pstmt = this.dbConn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			for (int i = 0; i < values.length; i++) {
				this.pstmt.setString(i + 1, values[i]);
			}
			this.rs = this.pstmt.executeQuery();
			return true;
		} catch (Exception e) {
			this.errorString = e.getMessage();
			return false;
		}
	}

	public boolean first() {
		this.clearErrorString();
		if (this.rs == null) {
			this.errorString = "ResultSet non inizializzato";
			return false;
		}
		try {
			return this.rs.first();
		} catch (Exception e) {
			this.errorString = e.getMessage();
			return false;
		}
	}

	public String getString(String field) {
		this.clearErrorString();
		if (this.rs == null) {
			this.errorString = "ResultSet non inizializzato";
			return null;
		}
		try {
			return this.rs.getString(field);
		} catch (Exception e) {
			this.errorString = e.getMessage();
			return null;
		}
	}

	public Integer getInteger(String field) {
		this.clearErrorString();
		if (this.rs == null) {
			this.errorString = "ResultSet non inizializzato";
			return null;
		}
		try {
			return this.rs.getInt(field);
		} catch (Exception e) {
			this.errorString = e.getMessage();
			return null;
		}
	}

	public boolean getBoolean(String field) {
		this.clearErrorString();
		if (this.rs == null) {
			this.errorString = "ResultSet non inizializzato";
			return false;
		}
		try {
			return this.rs.getBoolean(field);
		} catch (Exception e) {
			this.errorString = e.getMessage();
			return false;
		}
	}

	public float getFloat(String field) {
		this.clearErrorString();
		if (this.rs == null) {
			this.errorString = "ResultSet non inizializzato";
			return 0.0f;
		}
		try {
			return this.rs.getFloat(field);
		} catch (Exception e) {
			this.errorString = e.getMessage();
			return 0.0f;
		}
	}
	
	public LocalDate getLocalDate(String field) {
			this.clearErrorString();
        if (this.rs == null) {
            this.errorString = "ResultSet non inizializzato";
            return null;
        }
        try {
            return this.rs.getDate(field).toLocalDate();
        } catch (Exception e) {
            this.errorString = e.getMessage();
            return null;}
	}

	public Integer count() {
		this.clearErrorString();
		if (this.rs == null) {
			this.errorString = "ResultSet non inizializzato";
			return 0;
		}
		try {
			Integer actualPos = this.rs.getRow();
			this.rs.last();
			Integer c = this.rs.getRow();
			this.rs.absolute(actualPos);
			return c;
		} catch (Exception e) {
			this.errorString = e.getMessage();
			return 0;
		}
	}

	public boolean next() {
		this.clearErrorString();
		if (this.rs == null) {
			this.errorString = "ResultSet non inizializzato";
			return false;
		}
		try {
			return this.rs.next();
		} catch (Exception e) {
			this.errorString = e.getMessage();
			return false;
		}
	}
	
	public boolean last() {
		this.clearErrorString();
		if (this.rs == null) {
			this.errorString = "ResultSet non inizializzato";
			return false;
		}
 
		try {
			return this.rs.last();
		} catch (SQLException e) {
			this.errorString = e.getMessage();
			return false;
		}
	}
	
	public void previous() {
			this.clearErrorString();
        if (this.rs == null) {
            this.errorString = "ResultSet non inizializzato";
        } else {
            try {
                this.rs.previous();
            } catch (Exception e) {
                this.errorString = e.getMessage();
            }
        }
	}
	
	public boolean absolute(int row) {
			this.clearErrorString();
        if (this.rs == null) {
            this.errorString = "ResultSet non inizializzato";
            return false;
        } else {
            try {
                return this.rs.absolute(row);
            } catch (Exception e) {
                this.errorString = e.getMessage();
                return false;
            }
            }
	}
	
	public boolean updateQuery (String query) {
			this.clearErrorString();
        try {
            this.stmt = this.dbConn.createStatement();
            this.stmt.executeUpdate(query);
            return true;
        } catch (Exception e) {
            this.errorString = e.getMessage();
            return false;
        }
	}
	
	public boolean updateProtectedQuery (String query, String[] values) {
		this.clearErrorString();
        try {
            this.pstmt = this.dbConn.prepareStatement(query, 
            		ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            for (int i = 0; i < values.length; i++) {
                this.pstmt.setString(i + 1, values[i]);
            }
            this.pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            this.errorString = e.getMessage();
            return false;
        }
	}
	
	public boolean InsertProtectedQuery(String Query, String[] values) {
		this.clearErrorString();
		this.resetDataPrequery();
		try {
			this.pstmt = this.dbConn.prepareStatement(Query, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < values.length; i++) {
				this.pstmt.setString(i + 1, values[i]);
			}
			this.pstmt.executeUpdate();
 
			ResultSet rs = this.pstmt.getGeneratedKeys();
			if (rs.next()) {
				this.lastInsertId = rs.getInt(1);
			}
			return true;
		} catch (SQLException e) {
			this.errorString = e.getMessage();
			return false;
		}
 
 
	}
 
	public boolean InsertQuery(String query)
	{
		return this.InsertProtectedQuery(query, new String[] {});
	}
 
	private void resetDataPrequery() {
        this.lastInsertId = null;
    }
 
	public Integer getLastInsertId() {
		return this.lastInsertId;
	}
 
	public boolean DeleteQuery(String query) {
		this.clearErrorString();
		try {
			this.stmt = this.dbConn.createStatement();
			this.stmt.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			this.errorString = e.getMessage();
			return false;
		}
	}
 
	public boolean DeleteProtectedQuery(String query, String[] values) {
		this.clearErrorString();
		try {
			this.pstmt = this.dbConn.prepareStatement(query);
			for (int i = 0; i < values.length; i++) {
				this.pstmt.setString(i + 1, values[i]);
			}
			this.pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			this.errorString = e.getMessage();
			return false;
		}
	}

}
