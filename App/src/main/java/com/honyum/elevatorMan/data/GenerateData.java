package com.honyum.elevatorMan.data;

import java.util.ArrayList;
import java.util.List;

public class GenerateData {
	
	public static List<Project> createProject() {
		List<Project> projectList = new ArrayList<Project>();
		
		for (int i = 0; i < 10; i++) {
			Project project = new Project();

			List<Building> buildingList = new ArrayList<Building>();
						
			for (int j = 0; j < 20; j++) {
				Building building = new Building();
				List<Elevator> elevatorList = new ArrayList<Elevator>();
				
				for (int k = 0; k < 40; k++) {
					Elevator elevator = new Elevator();
					elevator.setUnitCode("" + (k % 3));
					elevatorList.add(elevator);
				}

				building.setElevatorList(elevatorList);
				buildingList.add(building);
			}
			project.setBuildingList(buildingList);
			projectList.add(project);
		}
		return projectList;
	}
}