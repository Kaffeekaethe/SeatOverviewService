package com.db.systel.bachelorproject2016.seatoverviewservice.clients;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.db.systel.bachelorproject2016.seatoverviewservice.domainmodel.data.Seat;

public class PricingServiceClient {
	private static RestTemplate restTemplate;

	// TODO: Service Discovery f�r BuchungsDienste --> URL und Port
	/*
	 * Hier wird die Adresse spezifiziert, damit der Dienst weiß, wo er fragen
	 * musss
	 */
	private static String hostAddress = "http://localhost";
	private static int port = 8084;

	public static Double getPrice(Seat seat) {

		restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		/*
		 * Es gibt verschiedene Header, die gesetzt werden müssen. Hier brauchen
		 * wir keinen, weil wir nur ein Double wollen. Würden wir aber z.B.
		 * JSON-Erwarten, müssten die Header "application/json" heißen.
		 * 
		 * Ansonsten kann er die Antwort nicht verarbeiten
		 * 
		 * z.B.: headers.add("Accept", "text/plain");
		 * headers.add("Content-Type", "text/plain");
		 * 
		 * 
		 * ? gibt den Typ des RequestBody an, wenn es einen gäbe (hier haben wir
		 * nur ein GET-Statement, da gibt es keine Request-Bodys. Gäbe es einen
		 * body, müsste das der erste Parameter sein.
		 * 
		 * Referenz, wenn du dir das mal anschauen willst, sind die
		 * NotificationService-Clients
		 */
		HttpEntity<?> entity = new HttpEntity<>(headers);

		String URL = String.format("%s:%s/%s", hostAddress, port, "get-price?seatClass=" + seat.getSeatClass());
		try {

			/*
			 * <T> gibt an, was wir zurück erwarten. Bei einem Array z.B.
			 * dataType[].class
			 */
			ResponseEntity<Double> response = restTemplate.exchange(URL, HttpMethod.GET, entity, Double.class);

			return response.getBody();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

	}

}
