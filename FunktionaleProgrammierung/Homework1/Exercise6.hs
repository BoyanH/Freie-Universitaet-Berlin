--Colors don't need to be precise or at least not for this example, so I choose Float and not Double
rgb2cmyk :: (Float, Float, Float) -> (Float, Float, Float, Float)
rgb2cmyk (r, g, b) = if (r, g, b) == (0, 0, 0)  -- default case
					then (0, 0, 0, 1)
					else (c, m, y, k) --using where for cleaner code
					where
					 	w = maximum [r / 255, b / 255, g / 255] --calculating c, m, y and k using the given formula
					 	c = (w - (r / 255)) / w
					 	m = (w - (g / 255)) / w
					  	y = (w - (b / 255)) / w
					 	k = 1 - w