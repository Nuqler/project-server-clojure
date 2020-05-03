(ns ring-app.core
  (:gen-class)
  (:require
   [ring-app.server :as settings :only [start-server stop-server]]))

(defn -main [& args]
  (case (first args)
    "-start" (settings/start-server)
    (println "No arguments were provided")))
