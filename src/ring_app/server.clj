(ns ring-app.server
  (:require
   [ring-app.routes :refer [routes]]
   [ring-app.db.core :refer [create-tables-debug]]
   [ring.util.http-response :as response]
   [reitit.ring :as reitit]
   ;;   [ring.adapter.jetty :as jetty]
   [org.httpkit.server :as server]
   [muuntaja.middleware :as muuntaja]
   [ring.middleware.reload :refer [wrap-reload]]
   [muuntaja.middleware :refer [wrap-format]]))

(def handler
  (reitit/routes
   (reitit/ring-handler
    (reitit/router routes)) ;; add custom routes
   (reitit/create-default-handler
    {:not-found
     (constantly (response/not-found "404 - Page not found"))
     :method-not-allowed
     (constantly (response/method-not-allowed "405 - Not allowed"))
     :not-acceptable
     (constantly (response/not-acceptable "406 - Not acceptable"))})))

(defn wrap-nocache [handler]
  (fn [request]
    (-> request
        handler
        (assoc-in [:headers "Pragma"] "no-cache"))))

(defn wrap-formats [handler]
  (-> handler
      (muuntaja/wrap-format)))

;; define server instance

(defonce ^:private server-api (atom nil))

(defn start-server []
  "Starts the server"
  (println "Starting server...")
  (reset! server-api
          (server/run-server
           (-> #'handler
               wrap-nocache
               wrap-reload)
           {:port 3000}))
  (create-tables-debug)
  (println "Success. Server launch complete."))

(defn stop-server []
  "Shutdown the server after waining for 100ms"
  (when-not (nil? @server-api)
    (@server-api :timeout 100)
    (reset! server-api nil))
  (println "Server shutdown complete."))
