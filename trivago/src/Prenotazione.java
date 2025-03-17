import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Prenotazione {

	private int id;
	private String nomeCliente;
	private LocalDate dataPrenotazione;
	private String numeroCliente;
	private Connection conn;
	

	
	
	public Prenotazione() {

	}

	public Prenotazione(int id, String nomeCliente,LocalDate dataInizio, String numeroCliente) {
		this.id = id;
		this.nomeCliente = nomeCliente;
		this.dataPrenotazione = dataInizio;
		this.numeroCliente = numeroCliente;
	}
	
	public Prenotazione(String nomeCliente, LocalDate dataInizio, String numeroCliente) {
		this.nomeCliente = nomeCliente;
		this.dataPrenotazione = dataInizio;
		this.numeroCliente = numeroCliente;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public LocalDate getDataInizio() {
		return dataPrenotazione;
	}

	public void setDataInizio(LocalDate dataInizio) {
		this.dataPrenotazione = dataInizio;
	}

	public String getNumeroCliente() {
		return numeroCliente;
	}

	public void setNumeroCliente(String numeroCliente) {
		this.numeroCliente = numeroCliente;
	}
	
	public Connection getConnection() {
			try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
	}
	

	public void insertPrenotazione(String nome, String telefono, LocalDate dataPrenotazione) {
	    String query = "INSERT INTO prenotazione (nome, telefono, data) VALUES (?,?,?)";
	    getConnection();
	    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	        pstmt.setString(1, nome);
	        pstmt.setString(2, telefono);
	        pstmt.setDate(3, java.sql.Date.valueOf(dataPrenotazione));
	        pstmt.executeUpdate();
	        System.out.println("Prenotazione inserita con successo!");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void deletePrenotazione(int id) {
		String query = "DELETE FROM prenotazione WHERE id =?";
        getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Prenotazione eliminata con successo!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
            
        
}

