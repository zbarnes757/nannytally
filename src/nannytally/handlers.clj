(ns nannytally.handlers
  (:require
   [compojure.core :refer [defroutes GET]]
   [compojure.route :as route]
   [ring.middleware.keyword-params :refer [wrap-keyword-params]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.logger :refer [wrap-with-logger]]))


(defroutes routes
  (GET "/" [] "<h1>Hello World</h1>")
  (route/not-found "<h1>Page not found</h1>"))

(defn wrap-system
  [handler system]
  (fn [request]
    (handler (assoc request :system system))))

(defn make-handler [system]
  (-> #'routes
      (wrap-system system)
      wrap-with-logger
      wrap-keyword-params
      wrap-params))
