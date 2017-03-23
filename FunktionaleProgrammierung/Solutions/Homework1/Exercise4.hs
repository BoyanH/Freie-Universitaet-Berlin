{-
	In this exercise I am using Doubles and not Floats for more precision
	Latitude and Longitude are almost never whole numbers
-}

rad2grad :: Double -> Double 																	
rad2grad rad = rad * 180 / pi {-one radiant is the angle that subtends one radius from given circle
								lengthOfCircle = 2*pi*radius and radiant of 1 radius = 1 => 360 degrees = 2*pi radiants
									=> deg/rad = 180/pi => deg = 180*rad / pi
							-}

grad2rad :: Double -> Double
grad2rad grad = grad * pi / 180 -- same as above, from deg/rad = 180/pi => rad = grad*pi / 180

distanceOnCircle :: Double -> Double -> Double -> Double -> Double
distanceOnCircle x1 y1 x2 y2 = let c = 111.2225685 in c * (grad2rad longitudeDegree) -- The given distance constant is per degree, so I convert radiants in degrees
							   where 					--Haskell works wit radiants, so I am converting each given degree in radiants
							       longitudeDegree = acos (sin (rad2grad x1) * sin (rad2grad x2) + cos (rad2grad x1) * cos (rad2grad x2) * cos (rad2grad (y1 - y2) ))