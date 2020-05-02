(ns ring-app.server
  (:require
   [ring-app.routes :refer [routes]]
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
     (constantly (response/not-found "404 - page not found"))
     :method-not-allowed
     (constantly (response/method-not-allowed "405 - not allowed"))
     :not-acceptable
     (constantly (response/not-acceptable "406 - not acceptable"))})))

(defn wrap-nocache [handler]
  (fn [request]
    (-> request
        handler
        (assoc-in [:headers "pragma"] "no-cache"))))

(defn wrap-formats [handler]
  (-> handler
      (muuntaja/wrap-format)))

;; define server instance

(defonce ^:private server-api (atom nil))

(defn start-server []
  "Start the server as a background process"
  (println "Starting server...")
  (reset! server-api
   (server/run-server
    (-> #'handler
        wrap-nocache
         wrap-reload)
    {:port 3000}))
  (println "Success."))

(defn stop-server []
  "Shutdown the server after waining for 100ms"
  (when-not (nil? @server-api)
    (@server-api :timeout 100)
    (reset! server-api nil))
  (println "Server shutdown complete."))
