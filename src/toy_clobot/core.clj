(ns toy-clobot.core
  (:require [clojure.string :refer [split split-lines]]))

;; - Handle the next command [ ]
;;   - placement [ ]
;;   - direction change [x]
;;   - a forward move [ ]
;;     - CHECK if next move is valid, skip if not valid
;;   - reporting [ ]
;;     - print (x: x, y: y, dir: f) to stdout
;;   - Dockerise it [  ]

(def board-dimensions
  "Default width and height of board in units"
  {:width 5 :height 5})

(def directions
  "Valid directions"
  [:NORTH :EAST :SOUTH :WEST])

(defn parse-command-type
  "returns a keyword representing the type of command that will be attempted"
  [command]
  (let [command-type (-> command
                         (split #" ")
                         (first))
        command-matches? (fn [command pattern] (boolean (re-find pattern command)))]
    (cond
      (command-matches? command-type #"PLACE")
      :place
      (command-matches? command-type #"(NORTH|EAST|SOUTH|WEST)")
      :direction
      (command-matches? command-type #"MOVE")
      :move
      (command-matches? command-type #"REPORT")
      :report)))

(defn new-direction [current turn-dir]
 "Will just throw for now if the direction is not valid"
  (let [current-index (.indexOf directions current)] ; TODO: This is pretty gross
  (case turn-dir
    :RIGHT (get directions (+ current-index 1) :NORTH)
    :LEFT (get directions (- current-index 1) :WEST))))

(defn -main [arg]
  (let [commands (-> arg
                     (slurp)
                     (split-lines))]))
