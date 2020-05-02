(ns ring-app.core
  (:gen-class)
  (:require
   [ring-app.server :as settings :only [start-server stop-server]]))

   ;;[ring-app.routes :as routes]
   ;;[reitit.ring :as reitit]
   ;;[ring.adapter.jetty :as jetty]
   ;;[muuntaja.middleware :as muuntaja]
   ;;[ring.util.http-response :as response]
   ;;[ring.middleware.reload :refer [wrap-reload]]
   ;;[muuntaja.middleware :refer [wrap-format]]
   ;[ring-app.db.core :as db]

(defn -main [& args]
  (case (first args)
    "-start" (settings/start-server)
    (println "No arguments were provided")))
