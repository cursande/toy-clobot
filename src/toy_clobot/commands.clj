(ns toy-clobot.commands
(:require [clojure.string :refer [split]]))

(def table-dimensions
  "Default width and height of table in units, assuming SW corner is (0,0)"
  {:width 4 :height 4})

(def directions
  "Valid directions"
  [:NORTH :EAST :SOUTH :WEST])

(defn parse-command-type [command]
  "returns a keyword representing the type of command that will be attempted"
  (let [command-type (-> command
                         (split #" ")
                         (first))
        command-matches? (fn [command pattern] (boolean (re-find pattern command)))]
    (cond
      (command-matches? command-type #"PLACE")
      :place
      (command-matches? command-type #"(LEFT|RIGHT)")
      :turn
      (command-matches? command-type #"MOVE")
      :move
      (command-matches? command-type #"REPORT")
      :report)))

(defn valid-coords? [coordinates]
  "Ensure coords are still on the table"
  (let [x (:x coordinates)
        y (:y coordinates)]
    (and (<= x (:width table-dimensions))
         (>= x 0)
         (<= y (:height table-dimensions))
         (>= y 0))))

(defn parse-place-command [[command x y f]]
  {:x (read-string x)
   :y (read-string y)
   :f (keyword f)})

(defn new-direction [current turn-dir]
  (let [current-index (.indexOf directions current)] ; TODO: This is pretty gross
    (case turn-dir
      :RIGHT (get directions (+ current-index 1) :NORTH)
      :LEFT (get directions (- current-index 1) :WEST))))

(defn coords-after-place [command coordinates]
  "Returns new coords if valid, returns original coordinates if invalid"
  (let [new-coordinates (-> (re-find #"PLACE (\d+),(\d+),(\w+)" command)
                            (parse-place-command))]
    (if (valid-coords? new-coordinates)
      new-coordinates
      coordinates)))

(defn coords-after-turn [command coordinates]
  "Returns new coordinates after turning"
  (assoc coordinates
         :f
         (new-direction (:f coordinates) (keyword command))))

(defn coords-after-move [coordinates]
  "Returns new coordinates if move is valid, returns original coordinates if invalid"
  (let [x (:x coordinates)
        y (:y coordinates)
        new-coordinates (case (:f coordinates)
                          :NORTH (assoc coordinates :y (inc y))
                          :EAST  (assoc coordinates :x (inc x))
                          :SOUTH (assoc coordinates :y (dec y))
                          :WEST  (assoc coordinates :x (dec x)))]
    (if (valid-coords? new-coordinates)
      new-coordinates
      coordinates)))

(defn report-and-return-coords [coordinates]
  "returns the coordinates after printing to output stream"
  (let [report (format "Current position: [ X: %s, Y: %s, F: %s ]"
                       (:x coordinates)
                       (:y coordinates)
                       (name (:f coordinates)))]
    (println report)
    coordinates))

(defn process-command [command-type command coordinates]
  "Processes valid actions for the robot, and returns its new coordinates"
  (if (= command-type :place)
    (coords-after-place command coordinates)
    (if (empty? coordinates) ; don't bother processing commands if the robot is not on the table
      coordinates
      (case command-type
        :turn (coords-after-turn command coordinates)
        :move (coords-after-move coordinates)
        :report (report-and-return-coords coordinates)))))

