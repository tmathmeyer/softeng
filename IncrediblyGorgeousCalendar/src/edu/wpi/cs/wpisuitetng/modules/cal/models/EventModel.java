package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Semaphore;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

public class EventModel {

	private DateTimeFormatter serializer = ISODateTimeFormat.basicDateTime();
	public static final String separator = "%2C";

	public Event[] getEvents(DateTime from, DateTime to) {
		return get("filtered-events", from.toString(serializer),
				to.toString(serializer));
	}

	private Event[] get(String... args) {
		final Semaphore sem = new Semaphore(1);
		try {
			sem.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final ArrayList<Event> events = new ArrayList<Event>();
		
		// Send a request to the core to save this message
		final Request request = Network.getInstance().makeRequest("cal/events/" + glue(args),
				HttpMethod.GET);
		request.addObserver(new RequestObserver() {

			@Override
			public void responseSuccess(IRequest iReq) {
				final Gson parser = new Gson();
				events.addAll(Arrays.asList(parser.fromJson(iReq.getBody(), Event[].class)));
				sem.release();
			}

			@Override
			public void responseError(IRequest iReq) {
				System.err.println("The request to add an event failed.");
				sem.release();

			}

			@Override
			public void fail(IRequest iReq, Exception exception) {
				System.err.println("The request to add an event failed.");
				sem.release();
			}
		}); // add an observer to process the response
		request.send(); // send the request

		boolean acquired = false;
		while (!acquired) {
			try {
				sem.acquire();
				acquired = true;
			} catch (InterruptedException e) {
			}
		}
		return events.toArray(new Event[0]);
	}

	private String glue(String[] args) {
		StringBuilder sb = new StringBuilder();
		for (String string : args) {
			sb.append(string);
			if (args[args.length - 1] != string) // yes, this is reference equals to check for the last item
				sb.append(separator);
		}
		return sb.toString();
	}
}
