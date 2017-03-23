{-
	I am using Double, because money and interest rate are usually not whole numbers and
	not Float as Double has bigger capacity => better precision
-}
getTotalCapital :: Double -> Double -> Double -> Double             --Using the given formula, incomeInPercents used for a more readable code
getTotalCapital baseInvestment annualInterestRate durationInYears = baseInvestment * incomeInPercents
																	where incomeInPercents = exp(annualInterestRateConverted*durationInYears)
																		  annualInterestRateConverted = if (annualInterestRate) <= 1
											  								--if the input is more than 1, the user really meant X%
																		  								then annualInterestRate
																		  								else annualInterestRate / 100