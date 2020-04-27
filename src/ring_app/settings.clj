(ns ring-app.settings
  (:require
   [ring-app.routes :as routes]
   [ring.util.http-response :as response]
   [reitit.ring :as reitit]
   [ring.adapter.jetty :as jetty]
   [muuntaja.middleware :as muuntaja]
   [ring.middleware.reload :refer [wrap-reload]]
   [muuntaja.middleware :refer [wrap-format]]))

(def handler
  (reitit/routes
   (reitit/ring-handler
    (reitit/router routes/routes))
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

(defonce server
  (delay
    (jetty/run-jetty
     (-> #'handler
         wrap-nocache
         wrap-reload)
     {:port 3000
      :join? false}))) ;;TRUE blocks repl prompt
