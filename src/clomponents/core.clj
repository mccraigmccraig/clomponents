(ns clomponents.core
  (:use clojure.core.strint
        clojure.core.incubator)
  (:require [clojure.tools.logging :as log]
            [clomponents.control :as control]))

(defn create-registry
  "create a registry of clomponents from a hash of configuration. clomponent
   objects will be registered against the same keys in the registry as their
   config is registered against in the config"
  [registry-config]
  (reduce (fn [r [id clomponent-config]]
            (assoc r id (control/create-namespace-clomponent clomponent-config)))
          {}
          registry-config))

(defn create
  [registry id & params]
  (control/create (registry id)))

(defn object
  [registry id]
  (control/object (registry id)))

(defn perform
  [registry id action]
  (control/perform (registry id) action))

(defn destroy
  [registry id]
  (control/destroy (registry id)))
