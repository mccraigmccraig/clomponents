(ns clomponents.core
  (:use clojure.core.strint
        clojure.core.incubator)
  (:require [clojure.tools.logging :as log]
            [clomponents.control :as control]))

(defn create-registry
  "create a registry of clomponents from a hash of configuration. clomponent
   objects will be registered against the same keys in the registry that their
   config is registered against in the config"
  [registry-config]
  (reduce (fn [r [id clomponent-config]]
            (assoc r id (control/create-namespace-clomponent clomponent-config)))
          {}
          registry-config))

(defn swap-registry
  "given an atom containing a current registry, and config for a new registry, first
   destroy all clomponents in the current registry, then swap the atom for a new
   registry initialised from the config"
  [registry-atom registry-config]
  (swap! registry-atom
         (fn [old-registry]
           (-?>> old-registry
                 reverse
                 (map (fn [[key clomponent]]
                        (control/destroy clomponent)))
                 dorun)
           (create-registry registry-config))))
