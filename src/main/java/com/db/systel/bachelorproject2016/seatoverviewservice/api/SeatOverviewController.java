package com.db.systel.bachelorproject2016.seatoverviewservice.api;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.db.systel.bachelorproject2016.seatoverviewservice.SeatOverviewService;
import com.db.systel.bachelorproject2016.seatoverviewservice.clients.PricingServiceClient;
import com.db.systel.bachelorproject2016.seatoverviewservice.domainmodel.data.Seat;
import com.db.systel.bachelorproject2016.seatoverviewservice.domainmodel.data.TrainConnection;

@EnableAutoConfiguration
@Controller
public class SeatOverviewController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<String> home() {

		return new ResponseEntity<String>("I am a SeatOverviewService.", HttpStatus.OK);
	}

	/*
	 * Die ersten drei Requests leiten einfach nur die Parameter an die
	 * Datenbank weiter
	 */
	@RequestMapping(value = "/get-train-connections", method = RequestMethod.GET)
	public ResponseEntity<?> trainConnections(@RequestParam(value = "start") String start,
			@RequestParam(value = "destination") String destination, @RequestParam(value = "time") String time,
			@RequestParam(value = "day") String day, @RequestParam(value = "departure") boolean departure) {

		try {

			Date date = SeatOverviewService.dateTimeFormat.parse(day + " 00:00:00");

			Calendar c = Calendar.getInstance();
			c.setTime(date);

			Date datetime = SeatOverviewService.dateTimeFormat.parse("01-01-1970 " + time + ":00");

			int weekDay = c.get(Calendar.DAY_OF_WEEK) - 1;

			if (weekDay == 0) {
				weekDay = 7;
			}

			List<TrainConnection> trainConnections = SeatOverviewService.seatOverviewDAO.getTrainConnections(start,
					destination, datetime.getTime(), weekDay, departure, date.getTime());

			if (trainConnections.size() == 0){
				return new ResponseEntity<String>("There aren't any connections available", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<TrainConnection>>(trainConnections, HttpStatus.OK);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<List<TrainConnection>>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/get-seats-recommendation", method = RequestMethod.GET)
	public ResponseEntity<?> seatsRecommendation(@RequestParam(value = "trainConnectionID") int trainConnectionID,
			@RequestParam(value = "seatClass") int seatClass, @RequestParam(value = "area", required=false) String area,
			@RequestParam(value = "location", required=false) String location,
			@RequestParam(value = "compartmentType", required=false) String compartmentType,
			@RequestParam(value = "upperLevel", required=false) boolean upperLevel,
			@RequestParam(value = "numberOfPersons") int numberOfPersons, @RequestParam(value = "day") String day,
			@RequestParam(value = "arrival") String arrival, @RequestParam(value = "departure") String departure) {

		try {

			Date arrivalDate = SeatOverviewService.dateTimeFormat.parse(day + " " + arrival + ":00");
			Date departureDate = SeatOverviewService.dateTimeFormat.parse(day + " " + departure + ":00");

			List<Seat> seats = SeatOverviewService.seatOverviewDAO.getSeat(trainConnectionID, seatClass, area, location,
					compartmentType, upperLevel, numberOfPersons, departureDate.getTime(), arrivalDate.getTime());

			// FÃ¼r jeden Platz den Preis ermitteln und hinzufÃ¼gen

			if (seats == null) {

				return new ResponseEntity<String>("There isn't a sufficient number of seats", HttpStatus.CONFLICT);
			}

			for (Seat seat : seats) {
				seat.setPrice(PricingServiceClient.getPrice(seat));
			}

			return new ResponseEntity<List<Seat>>(seats, HttpStatus.OK);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/get-seats", method = RequestMethod.GET)
	public ResponseEntity<?> allSeats(@RequestParam(value = "trainConnectionID") int trainConnectionID,
			@RequestParam(value = "day") String day, @RequestParam(value = "arrival") String arrival,
			@RequestParam(value = "departure") String departure) {
		try {

			SeatOverviewService.dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

			Date arrivalDate = SeatOverviewService.dateTimeFormat.parse(day + " " + arrival + ":00");
			Date departureDate = SeatOverviewService.dateTimeFormat.parse(day + " " + departure + ":00");

			List<Seat> seats = SeatOverviewService.seatOverviewDAO.getAllSeats(trainConnectionID,
					departureDate.getTime(), arrivalDate.getTime());

			return new ResponseEntity<List<Seat>>(seats, HttpStatus.OK);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * Haben wir noch nichts zu
	 */
	@RequestMapping(value = "/get-timestamp", method = RequestMethod.GET)
	public ResponseEntity<String> timestamp() {
		return new ResponseEntity<String>("Timestamps are currently not available", HttpStatus.OK);
	}

	/*
	 * Lock und unlock mÃ¼ssten auf dem Event-Store arbeiten, das haben wir noch
	 * nicht (also hinter den Funktionen liegt noch nichts, und das dÃ¼fte
	 * eigentlich auch nicht in dieser DAO sein)
	 */
	@RequestMapping(value = "/unlock-seat", method = RequestMethod.POST)
	public ResponseEntity<String> unlockedSeat(@RequestBody List<Integer> seatIDs) {

		for (int seatID : seatIDs) {
			SeatOverviewService.seatOverviewDAO.unlockSeat(seatID);
		}

		return new ResponseEntity<String>("Unlocked Seats", HttpStatus.OK);
	}

	@RequestMapping(value = "/lock-seat", method = RequestMethod.POST)
	public ResponseEntity<String> lockedSeat(@RequestBody List<Integer> seatIDs) {

		for (int seatID : seatIDs) {
			SeatOverviewService.seatOverviewDAO.lockSeat(seatID);
		}

		return new ResponseEntity<String>("Unlocked Seats", HttpStatus.OK);
	}

	@RequestMapping(value = "/fetch-soft-copy", method = RequestMethod.GET)
	public ResponseEntity<String> fetchedSoftCopy() {

		// TODO: Update Database
		return new ResponseEntity<String>("I am a SeatOverviewService.", HttpStatus.OK);
	}

}
