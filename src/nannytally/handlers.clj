(ns nannytally.handlers
  (:require
   [clojure.tools.logging :as logging]
   [compojure.core :refer [defroutes GET POST DELETE]]
   [compojure.route :as route]
   [hiccup2.core :as h]
   [nannytally.config :as config]
   [nannytally.models.entries :as entries]
   [nannytally.views :as views]
   [ring.logger :refer [wrap-with-logger]]
   [ring.middleware.keyword-params :refer [wrap-keyword-params]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.refresh :refer [wrap-refresh]]
   [ring.util.response :refer [content-type response]]))

(defn get-db-conn [request]
  (get-in request [:system :nannytally.system/db]))

(defn html-response [body]
  (-> body
      response
      (content-type "text/html")))

(defn index [request]
  (let [db (get-db-conn request)
        entries (entries/list-entries db)
        entries (map entries/add-total-hours-for-entry entries)
        _ (logging/info (str "Loaded entries: " entries))]
    (-> entries
        views/index-page
        html-response)))

(defn create-entries [request]
  (let [db (get-db-conn request)
        params (:params request)
        _ (logging/info (str "Got request with params: " params ". Inserting..."))
        entry (entries/save-entry db params)
        _ (logging/info (str "Saved entry: " entry))
        entries (entries/list-entries db)
        entries (map entries/add-total-hours-for-entry entries)]
    (-> entries
        views/entries-table
        h/html
        str
        html-response)))

(defn delete-entry [request]
  (let [db (get-db-conn request)
        id (get-in request [:params :id])
        _ (logging/info (str "Deleting entry: " id))
        _ (entries/delete-entry db id)]
    (html-response "")))

(defroutes routes
  (GET "/" request (index request))
  (POST "/entries" request (create-entries request))
  (DELETE "/entries/:id" request (delete-entry request))
  (route/not-found "<h1>Page not found</h1>"))

(defn wrap-system
  [handler system]
  (fn [request]
    (handler (assoc request :system system))))

(defn make-handler [system]
  ;; Determine the base handler: either the refreshable routes or the static routes.
  (let [base-handler (if (contains? #{:default :dev} (config/get-profile))
                       (wrap-refresh #'routes) ;; If dev, apply refresh to the var
                       #'routes)]              ;; Otherwise, just use the var

    ;; Apply the rest of the middleware to the base handler
    (-> base-handler
        (wrap-system system)
        wrap-with-logger
        wrap-keyword-params
        wrap-params)))
