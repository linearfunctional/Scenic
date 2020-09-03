# from scenic.domains.driving.network import loadNetwork
# loadNetwork('/home/carla_challenge/Desktop/Carla/Dynamic-Scenic/Scenic-devel-099/examples/carla/OpenDrive/Town01.xodr')

# param map = localPath('../OpenDrive/Town01.xodr')
# param carla_map = 'Town01'

# from scenic.domains.driving.behaviors import *
# # from scenic.simulators.carla.model import *

# model scenic.domains.driving.model

param map = localPath('../../lgsvl/maps/cubetown.xodr')
param lgsvl_map = 'CubeTown'

model scenic.domains.driving.model

MAX_BREAK_THRESHOLD = 1
SAFETY_DISTANCE = 10
INITIAL_DISTANCE_APART = -1 * Uniform(5, 10)
STEPS_PER_SEC = 10

behavior LeadCarBehavior():
	try:
		FollowLaneBehavior()
	interrupt when 5 * STEPS_PER_SEC < simulation().currentTime and simulation().currentTime < 6 * STEPS_PER_SEC:
		take SetBrakeAction(MAX_BREAK_THRESHOLD)

behavior CollisionAvoidance():
	while distanceToAnyObjs(self, SAFETY_DISTANCE):
		take SetBrakeAction(MAX_BREAK_THRESHOLD)


behavior FollowLeadCarBehavior():

	try: 
		FollowLaneBehavior()

	interrupt when distanceToAnyObjs(self, SAFETY_DISTANCE):
		CollisionAvoidance()


<<<<<<< HEAD
=======
behavior LeadCarSuddenlyStopsAndGo():

	sudden_stop_time = Range(3, 6)
	start_time = time.time()

	try:
		FollowLane(25)

	interrupt when (time.time()-start_time > sudden_stop_time) and (time.time()-start_time < sudden_stop_time+1): 
		# Unnatural to enforce time constraint
		take actions.SetBrakeAction(MAX_BREAK_THRESHOLD)


>>>>>>> dynamics2
roads = network.roads
select_road = Uniform(*roads)
select_lane = Uniform(*select_road.lanes)

other = Car on select_lane.centerline,
		with behavior LeadCarBehavior()

ego = Car following roadDirection from other by INITIAL_DISTANCE_APART,
		with behavior FollowLeadCarBehavior()