# I-270 Commuter Transit Simulation

A discrete-event simulation in Java that models a public transit alternative for Frederick County commuters traveling down the I-270 corridor to Washington, DC.

Built as a capstone project at Frederick Community College (CMSC 285).

## The Problem

I-270 is one of the most congested commuter routes in Maryland. Thousands of Frederick County residents drive it every weekday morning to reach jobs in Montgomery County and DC. This project asks: **could a robotaxi feeder network plus a commuter train move those people more efficiently?**

## How It Works

The simulation models the morning rush from **7:00 AM to 10:00 AM** (180 simulated minutes):

1. **Commuters arrive** at 27 neighborhood pickup points across Frederick County (Walkersville, Urbana, Middletown, Ballenger Creek, Fort Detrick, and more). Arrival rates are randomized and weighted by each area's population density.
2. **Robotaxis pick them up.** A dispatcher assigns taxis across 8 routes (A–H), picks up waiting commuters, and drops them off at the central train station.
3. **Trains carry them to DC.** Two trains run on staggered schedules, boarding commuters from the station queue for the ~20-minute trip.
4. **Metrics are collected** for every completed trip.

## Output Data

At the end of each run, the simulation reports:

- Average and median **wait time** for commuters
- Average and median **total trip time** (door to DC)
- Wait and trip time **percentiles** (e.g., 90th percentile)
- **Throughput** (commuters delivered per minute)
- **Utilization** of taxis and trains
- Average passengers per train trip

## Project Structure

```
src/main/
  Main.java                 Entry point: sets parameters and runs the simulation
  Simulation.java           Core event loop
  Dispatcher.java           Assigns robotaxis to pickup requests
  RoboTaxi.java             Taxi pickup, drop-off, and seat tracking
  Train.java                Train schedules, boarding, and trip stats
  Commuter.java             A single commuter and their trip timestamps
  ArrivalProcess.java       Randomized commuter arrivals per location
  Metrics.java              Statistics collected from a run
  Route.java / RouteBuilder.java / Location.java   Route network loaded from CSV
  ExponentialDistribution.java / NormalDistribution.java   Random distributions
  Queue.java / Stack.java / List.java / SingleServerQueue.java   Data structures
test/main/                  JUnit tests for routes and data structures
locations.csv               Pickup point IDs and map coordinates
routes.csv                  Which locations belong to each route
SYSTEM_MAP.txt              Human-readable list of routes and stops
```

## Running It

Requires Java 17+.

```bash
javac -d out src/main/*.java
java -cp out main.Main
```

Run it from the project root so it can find `locations.csv` and `routes.csv`.

Simulation settings (run length, arrival rate multiplier, train travel time, train offsets) can be changed at the top of `Main.java`.

## My Contributions

*Sai Sirigiri, co-developer*

- Built the **two-train system** and multi-density commuter arrivals
- Integrated the **robotaxi dispatcher** into the simulation and fixed completed-trip tracking
- Implemented **taxi drop-off** into the station queue
- Added new **taxi metrics**
- Set the 7–10 AM simulation window, the train schedule, and new locations and routes
- Updated the `Commuter` and `ArrivalProcess` classes

## Team

- **Sai Sirigiri**, co-developer
- **Brad Pyne** ([@bradpyne](https://github.com/bradpyne)), co-developer

Developed collaboratively using feature branches and 17 pull requests on the original team repository, with regular team meetings to divide tasks and merge work.

Course instructor: J. Southard, Frederick Community College
