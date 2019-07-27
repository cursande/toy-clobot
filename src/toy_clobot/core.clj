(ns toy-clobot.core
  (:require [clojure.string :refer [split split-lines]]))

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

(defn -main [arg]
  (let [commands (-> arg
                     (slurp)
                     (split-lines))]))
