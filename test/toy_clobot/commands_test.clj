(ns toy-clobot.commands-test
  (:require [clojure.test :refer :all]
            [toy-clobot.commands :refer :all]))

(deftest test-parse-command-type
  (testing "it correctly recognises a place command"
    (let [command "PLACE 1,4,NORTH"]
      (is (= (parse-command-type command)
             :place))))
  (testing "it correctly recognises a turn command"
    (let [command "RIGHT"]
      (is (= (parse-command-type command)
             :turn))))
  (testing "it correctly recognises a move command"
    (let [command "MOVE"]
      (is (= (parse-command-type command)
             :move)))))

(deftest test-coords-after-place
  (testing "If command contains valid coordinates, it returns the new coordinates of the robot"
    (let [command "PLACE 4,2,EAST"
          coordinates { :x 2 :y 3 :f :SOUTH }]
      (is (= (coords-after-place command coordinates)
             { :x 4 :y 2 :f :EAST }))))
  (testing "If command contains invalid coordinates, it returns the original coordinates"
    (let [command "PLACE 2,6,NORTH"
          coordinates { :x 2 :y 3 :f :SOUTH }]
      (is (= (coords-after-place command coordinates)
             coordinates)))))

(deftest test-coords-after-turn
  (testing "it returns the new coordinates after turning"
    (let [command "RIGHT"
          coordinates { :x 2 :y 3 :f :SOUTH }]
      (is (= (coords-after-turn command coordinates)
             { :x 2 :y 3 :f :WEST })))
    (let [command "LEFT"
          coordinates { :x 3 :y 1 :f :EAST }]
      (is (= (coords-after-turn command coordinates)
             { :x 3 :y 1 :f :NORTH })))))

(deftest test-coords-after-move
  (testing "If move is valid, it returns the new coordinates of the robot after moving"
    (let [coordinates { :x 1 :y 1 :f :NORTH }]
      (is (= (coords-after-move coordinates)
             { :x 1 :y 2 :f :NORTH }))))
  (testing "If move is invalid, it returns the original coordinates"
    (let [coordinates { :x 0 :y 3 :f :WEST }]
      (is (= (coords-after-move coordinates)
             coordinates)))))

(deftest test-report-and-return-coords
  (testing "it reports the current position of the robot"
    (let [coordinates { :x 2 :y 4 :f :NORTH }]
      (is (= (with-out-str (report-and-return-coords coordinates))
             "Current position: [ X: 2, Y: 4, F: NORTH ]\n")))))

(deftest test-process-command
  (testing "it returns the new coordinates after processing the command"
    (let [command-type :place
          command "PLACE 3,3,WEST"
          coordinates {}]
      (is (= (process-command command-type command coordinates)
             { :x 3 :y 3 :f :WEST }))))
  (testing "it will not take other actions if the robot is not on the table"
    (let [command-type :move
          command "MOVE"
          coordinates {}]
      (is (= (process-command command-type command coordinates)
             {})))))
