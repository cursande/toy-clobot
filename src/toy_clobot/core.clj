(ns toy-clobot.core
  (:require [clojure.string :refer [split-lines]]
            [toy-clobot.commands :refer [parse-command-type process-command]]))

(defn no-command-remaining? [command]
  (false? (boolean (seq command))))

(defn run-robot-commands [commands]
  (loop [[command & remaining] commands coordinates {}]
    (if (no-command-remaining? command)
      (println "All commands complete")
      (let [command-type (parse-command-type command)
            new-coordinates (process-command command-type
                                             command
                                             coordinates)]
        (recur remaining new-coordinates)))))

(defn -main [path]
  (let [commands (-> path
                     (slurp)
                     (split-lines))]
    (run-robot-commands commands)))

