package com.example.api.gamedemo;

import com.example.api.constants.Constants;
import com.example.api.models.InnerBody;
import com.example.api.models.Line;
import com.example.api.models.Point;
import com.example.api.service.GetAdjuscentNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootApplication(scanBasePackages = {"com.example.api"})
@EnableAutoConfiguration
@RestController
@CrossOrigin({"*"})
public class GameDemoApplication {


	private static Map<String, Object> state;
	private static Map<String, Object> pointsAlreadyMet;
	private static Map<Point, List<Point>> adjuscentPoints;

	@Autowired
	private GetAdjuscentNodeService getAdjuscentNodeService;

	static {
		state = new HashMap<>();
		pointsAlreadyMet = new HashMap<>();
	}

	@RequestMapping("/initialize")
	public com.example.api.models.ResponseBody initialize() {
		//state = new HashMap<>();
		com.example.api.models.ResponseBody responseBody = new com.example.api.models.ResponseBody();

		adjuscentPoints = getAdjuscentNodeService.getPoints();
		resetMap();
		state.put("playerKey", Constants.PLAYER_1);

		responseBody.setMsg(Constants.INITIALIZE);
		responseBody.setBody(new InnerBody(null, "Player 1", null));
		System.out.println(responseBody);
		return responseBody;
	}

	private void resetMap() {
		state.clear();
		pointsAlreadyMet.clear();
	}

	@PostMapping("/node-clicked")
	public com.example.api.models.ResponseBody nodeClicked(@RequestBody Point point) {

		com.example.api.models.ResponseBody responseBody = new com.example.api.models.ResponseBody();
		System.out.println("NODE_CLICKED : " + point);

		//if start node is already there in the state map then add point as end node
		if(state.containsKey(Constants.STATE_START) && state.get(Constants.STATE_START) != null) {
			state.put(Constants.STATE_END, point);
		}

		// If node-clicked is not started then let's start it
		if (!state.containsKey(Constants.STATE_START)) {
			state.put(Constants.STATE_START, point);
			responseBody.setMsg(Constants.VALID_START_NODE);
			responseBody.setBody(new InnerBody(null, (String) state.get("playerKey"), "Select a second node to complete the line."));
			System.out.println(responseBody);
			return responseBody;
		}
		//if user selected the start point but it's not an adjuscent then throw error
		// if user selected node is not adjuscent then should be INVALID_NODE
		if (!adjuscentPoints.containsKey(point) || (state.get(Constants.STATE_START) != null) && state.get(Constants.STATE_END) != null) {
			state.remove(Constants.STATE_START);
			responseBody.setMsg(Constants.INVALID_END_NODE);
			responseBody.setBody(new InnerBody(null, (String) state.get(Constants.PLAYER_1), "Invalid move! Try again."));
			System.out.println(responseBody);
			return responseBody;
		}


		// if user is selected proper adjuscent node
		if (adjuscentPoints.containsKey(point)) {
			Point start = (Point) state.get(Constants.STATE_START);
			//end = (Point) state.get(Constants.STATE_END);
			Line line = new Line(start, point);
			responseBody.setMsg(Constants.VALID_START_NODE);
			responseBody.setBody(new InnerBody(line, "Player 2", "Select a second node to complete the line."));
			System.out.println(responseBody);
			return responseBody;
		}

		// if node-clicked event is already started but not the end point
		if (state.containsKey(Constants.STATE_START) && !state.containsKey(Constants.STATE_END)) {
			state.put(Constants.STATE_END, point);
			Point start = (Point) state.get(Constants.STATE_START);
			Point end = (Point) state.get(Constants.STATE_END);
			Line line = new Line(start, point);
			responseBody.setMsg(Constants.VALID_START_NODE);
			responseBody.setBody(new InnerBody(line, "Player 2", "Select a second node to complete the line."));
			System.out.println(responseBody);
			return responseBody;
		}

		Point start = (Point) state.get(Constants.STATE_START);
		Point end = (Point) state.get(Constants.STATE_END);

		// if user already selected 1 line and tries to select another point then it should be either START or END should be clicked first
		if (!start.equals(point) || !end.equals(point)) {
			// invalid move!!
			responseBody.setMsg(Constants.INVALID_START_NODE);
			responseBody.setBody(new InnerBody(null, "Player 2", "Not a valid starting position."));
			System.out.println(responseBody);
			return responseBody;
		}
		// user selected already from the start or end node
		// then we need to update start and end in the current state.
		return null;

	}

	@RequestMapping("/error1")
	String gameError() {
		return "{\n" +
				"    \"error\": \"Invalid type for `id`: Expected INT but got a STRING\"\n" +
				"}";
	}

	public static void main(String[] args) {
		SpringApplication.run(GameDemoApplication.class, args);
	}

}
