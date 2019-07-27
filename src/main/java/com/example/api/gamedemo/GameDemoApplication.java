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
		responseBody.setBody(new InnerBody(null, "Player 1", "Awaiting Player 1's Move!"));
		System.out.println(responseBody);
		return  responseBody;
	}

	private void resetMap() {
		state.clear();
		pointsAlreadyMet.clear();
	}

	@PostMapping("/node-clicked")
	public com.example.api.models.ResponseBody nodeClicked(@RequestBody Point point) {

		com.example.api.models.ResponseBody responseBody = new com.example.api.models.ResponseBody();

		System.out.println("NODE_CLICKED : " + point);

		// If node-clicked is not started then let's start it
		if(!state.containsKey(Constants.STATE_START)) {
			state.put(Constants.STATE_START, point);
			responseBody.setMsg(Constants.VALID_START_NODE);
			responseBody.setBody(new InnerBody(null, "Player 2", "Select a second node to complete the line."));
			System.out.println(responseBody);
			return  responseBody;
		}

		// if node-clicked event is already started but not the end point
		if(state.containsKey(Constants.STATE_START) && !state.containsKey(Constants.STATE_END)) {
			state.put(Constants.STATE_END, point);

			Point start = (Point) state.get(Constants.STATE_START);
			Point end = (Point) state.get(Constants.STATE_END);

			Line line = new Line(start, point);

			responseBody.setMsg(Constants.VALID_START_NODE);
			responseBody.setBody(new InnerBody(line, "Player 2", "Select a second node to complete the line."));

			System.out.println(responseBody);
			return  responseBody;
		}

		// if user selected node is not adjuscent then should be INVALID_NODE


		if(!adjuscentPoints.containsKey(point)) {

			responseBody.setMsg(Constants.INVALID_END_NODE);
			responseBody.setBody(new InnerBody(null, "Player 2", "Invalid move!."));
			System.out.println(responseBody);
			return  responseBody;
		} else {

			Point start = (Point) state.get(Constants.STATE_START);
			Point end = (Point) state.get(Constants.STATE_END);

			Line line = new Line(start, point);
			responseBody.setMsg(Constants.VALID_START_NODE);
			responseBody.setBody(new InnerBody(line, "Player 2", "Select a second node to complete the line."));
			System.out.println(responseBody);
			return responseBody;
		}

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
