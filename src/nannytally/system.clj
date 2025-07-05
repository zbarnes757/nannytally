(ns nannytally.system
  (:require
   [aero.core :refer [read-config]]
   [clojure.java.io :as io]
   [integrant.core :as ig]
   [nannytally.handlers :as handlers]
   [ring.adapter.jetty :as jetty]
   [clojure.tools.logging :as logging]))

(def system-map
  "This file defines the core system setup for the NannyTally application.
  It is responsible for initializing, configuring, and managing the lifecycle
  of the application's main components and services."
  {::config {}
   ::handler {::config (ig/ref ::config)}
   ::server
   {::config (ig/ref ::config)
    ::handler (ig/ref ::handler)}})

;; Config init
(defmethod ig/init-key ::config
  [_ _opts]
  (read-config (io/resource "config.edn") {:profile (keyword (System/getenv "PROFILE"))}))

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

(comment
  (def system
    (ig/init system-map))
  (io/resource "config.edn")
  (ig/halt! system))
