(ns toy-clobot.core-test
  (:require [clojure.test :refer :all]
            [toy-clobot.core :refer [parse-command-type
                                     new-direction]]))

(deftest test-parse-command-type
  (testing "it correctly recognises a place command"
    (let [command "PLACE 1,4,NORTH"]
      (is (= (parse-command-type command)
             :place))))
  (testing "it correctly recognises a direction command"
    (let [command "EAST"]
      (is (= (parse-command-type command)
             :direction))))
  (testing "it correctly recognises a MOVE-type command"
    (let [command "MOVE"]
      (is (= (parse-command-type command)
             :move)))))

(deftest test-new-direction
  (testing "it returns the new direction after turning"
    (let [current :EAST
          turn-dir :RIGHT]
      (is (= (new-direction current turn-dir)
             :SOUTH))))
  (testing "it can handle out-of-bounds values"
    (let [current :NORTH
          turn-dir :LEFT]
      (is (= (new-direction current turn-dir)
             :WEST)))))
