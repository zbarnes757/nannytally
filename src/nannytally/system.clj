(ns nannytally.system
  (:require
   [clojure.tools.logging :as logging]
   [integrant.core :as ig]
   [nannytally.config :as config]
   [nannytally.handlers :as handlers]
   [ring.adapter.jetty :as jetty]
   [next.jdbc :as jdbc]))

(def system-map
  "This file defines the core system setup for the NannyTally application.
  It is responsible for initializing, configuring, and managing the lifecycle
  of the application's main components and services."
  {::config {}
   ::db {::config (ig/ref ::config)}
   ::handler {::config (ig/ref ::config)
              ::db (ig/ref ::db)}
   ::server
   {::config (ig/ref ::config)
    ::handler (ig/ref ::handler)}})

;; Config init
(defmethod ig/init-key ::config
  [_ _opts]
  (config/read-config))

;; Handler init
(defmethod ig/init-key ::handler
  [_ opts]
  (handlers/make-handler opts))

;; Server init/halt
(defmethod ig/init-key ::server
  [_ {::keys [config handler] :as _opts}]
  (let [port (get-in config [:server :port])]
    (logging/info (str "Starting server on port: " port))
    (jetty/run-jetty handler (:server config))))

(defmethod ig/halt-key! ::server [_ server]
  (.stop server))

;; DB init
(defmethod ig/init-key ::db
  [_ {::keys [config]}]
  (let [db-spec (:db-spec config)]
    (jdbc/get-datasource db-spec)))

(comment
  (def system
    (ig/init system-map))
  (ig/halt! system))
