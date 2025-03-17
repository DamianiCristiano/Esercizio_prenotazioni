import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;

import classiMie.MySQL;

public class Main {

	public static void main(String[] args) {
		
		MySQL db = new MySQL("localhost", "3306", "hotel", "root", "");
		db.connectDB();
    	Prenotazione p = new Prenotazione();
		Scanner scanner = new Scanner(System.in);
		
		int scelta = 0;
		
		
		
		do {
			System.out.println("1. Visualizza prenotazioni");
            System.out.println("2. Aggiungi prenotazione");
            System.out.println("3. Elimina prenotazione");
            System.out.println("4. Esci");
            
            scelta = scanner.nextInt();
            		
            if (scelta < 1 || scelta > 4) {
            	System.out.println("Scelta non valida. Riprova.");
                continue;
            } 
            
            if (scelta == 1) {
            	ResultSet rs = db.selectQuery("Select * from prenotazione");
            	System.out.println("---------------------------");
            	try {
					while(rs.next()) {
						int id = db.getInteger("id");
						String nome = db.getString("nome");
						String telefono = db.getString("numero_di_telefono");
						LocalDate data = db.getLocalDate("data");
						System.out.println(id + "- " + nome + " " + telefono + " " + data);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
            	System.out.println("---------------------------");
            }
            
            if (scelta == 2) {
            	System.out.println("Inserisci nome:");
                String nome = scanner.next();
                scanner.nextLine();
                
                System.out.println("Inserisci numero di telefono:");
                String telefono = scanner.nextLine();
                
                System.out.println("Inserisci data (yyyy-mm-gg):");
                String data = scanner.nextLine();
                
                LocalDate dataPrenotazione = LocalDate.parse(data);
                
                if (LocalDate.now().isBefore(dataPrenotazione)) {
                	p.insertPrenotazione(nome, telefono, dataPrenotazione);;                	
                } else {
                	System.out.println("Data non valida. Deve essere una data successiva alla data odierna.");
                }

                
            }
            
            if (scelta == 3) {
            	p = new Prenotazione();
            	System.out.println("Inserisci id prenotazione da eliminare:");
                int id = scanner.nextInt();
                
                p.deletePrenotazione(id);
                System.out.println("Prenotazione eliminata con successo.");
            }
            		;
		} while (scelta != 4);

	}

}
