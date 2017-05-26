{-# LANGUAGE OverloadedStrings #-}

import Control.Monad
import Control.Applicative
import Database.PostgreSQL.Simple
import qualified Data.Text as Text


main = do
	conn <- connectPostgreSQL "dbname='dbs' user='testuser' host='localhost' password='testpass'"
	putStrLn "Connected to DB! Querying all students"
	xs <- (query_ conn "SELECT vorname, nachname, matrikelnummer FROM Student")
	forM_ xs $ \(fname, lname, matrikelnummer) ->
  		putStrLn $ Text.unpack "Matrikelnummer: " ++ show (matrikelnummer :: Int) ++ "; Vorname: " ++ fname ++ "; Nachname: " ++ lname