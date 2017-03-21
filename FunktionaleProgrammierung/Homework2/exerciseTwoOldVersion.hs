{-
overlaps :: Rectangle -> Rectangle -> Bool -- Tests, whether one rectangle overlaps the other or not
overlaps rectA rectB = if (isRectCorrect rectA) && (isRectCorrect rectB)
						--I check if a side of snd rect is within 1st rect
						--If not, then only chance to overlap is if 2nd rect contains fst
						--case contains (rectA rectB) is already checked in the xOverlps && yOverlaps
						then (xOverlaps && yOverlaps) || (contains rectB rectA)
						else
							reportRectError
							where 			--1st case, left side of 2nd rect within 1st rect
								xOverlaps = leftSideWithin || righSideWithin
											--2nd case, right side of 2nd rect within 1st rect

											--1st case, top side of 2nd rect within 1st rect 
								yOverlaps = topSideWithin || bottomSideWithin
											--2nd case, bottom side of 2nd rect within 1st rect
											

								leftSideWithin = ( (fst (fst rectA)) < (fst (fst rectB)) ) &&
												( (fst (snd rectA)) > (fst (fst rectB)) )

								righSideWithin = ( (fst (fst rectA)) < (fst (snd rectB)) ) &&
												( (fst (snd rectA)) > (fst (snd rectB)) )
											

								topSideWithin = ( (snd (fst rectA)) > (snd (fst rectB)) ) &&
												( (snd (snd rectA)) < (snd (fst rectB)) )

								bottomSideWithin = ( (snd (fst rectA)) > (snd (snd rectB)) ) &&
													( (snd (snd rectA)) < (snd (snd rectB)) )
-}