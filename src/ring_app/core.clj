(ns ring-app.core
  (:gen-class)
  (:require
   [ring-app.settings :as settings :only [server]]))

(defn -main [& args]
  (case (first args)
    "-start" (do (.start @settings/server))
    (println "No arguments were provided")))
