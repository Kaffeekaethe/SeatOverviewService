package com.db.systel.bachelorproject2016.seatoverviewservice;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.db.systel.bachelorproject2016.seatoverviewservice.api.SeatOverviewController;
import com.db.systel.bachelorproject2016.seatoverviewservice.domainmodel.logic.SeatOverviewDAO;

public class SeatOverviewService {

	public static SeatOverviewDAO seatOverviewDAO;

	public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	public static void main(String[] args) {

		dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");

		seatOverviewDAO = (SeatOverviewDAO) context.getBean("seatOverviewDAO");

		SpringApplication.run(SeatOverviewController.class, args);
	}

}
