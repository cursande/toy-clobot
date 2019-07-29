(ns toy-clobot.core-test
  (:require [clojure.test :refer :all]
            [toy-clobot.core :refer :all]))

(deftest test-run-robot-commands
  (testing "runs a list of commands"
    (let [commands ["PLACE 1,2,NORTH"
                    "MOVE"
                    "RIGHT"
                    "RIGHT"
                    "MOVE"
                    "LEFT"
                    "MOVE"
                    "REPORT"]]
      (is (= (with-out-str (run-robot-commands commands))
             (str "Current position: [ X: 2, Y: 2, F: EAST ]\n"
                  "All commands complete\n")))))
  (testing "it only reports coordinates for the robot if it is on the table"
    (let [commands ["PLACE 7,6,NORTH"
                    "MOVE"
                    "REPORT"
                    "PLACE 4,4,NORTH"
                    "REPORT"]]
      (is (= (with-out-str (run-robot-commands commands))
             (str "Current position: [ X: 4, Y: 4, F: NORTH ]\n"
                  "All commands complete\n"))))))
