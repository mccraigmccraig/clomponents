(ns clomponents.control
  (:use clojure.core.strint
        clojure.core.incubator
        midje.open-protocols)
  (:require [clojure.tools.logging :as log]))

(defprotocol Clomponent
  (create [this] [this additional-config])
  (destroy [this])
  (object [this])
  (perform [this action]))

(defn resolve-fn
  [config fn-type & {:keys [required?] :or {required? true}}]
  (let [fn-or-sym (fn-type config)]
    (cond (fn? fn-or-sym)
          fn-or-sym

          (nil? fn-or-sym)
          (let [use-ns (:ns config)
                use-n (-> fn-type name symbol)
                v (if (and use-ns use-n)
                    (do
                      (require use-ns)
                      (ns-resolve use-ns use-n)))]
            (cond v v
                  required? (throw (RuntimeException. (<< "could not resolve for fn-type: ~{fn-type} in ~{config}")))
                  true nil))


          (symbol? fn-or-sym)
          (let [[ns n] [(-?> fn-or-sym namespace symbol) (-> fn-or-sym name symbol)]
                use-ns (or ns (:ns config))
                use-n n
                v (if (and use-ns use-n)
                    (do
                      (require use-ns)
                      (ns-resolve use-ns use-n)))]
            (cond v v
                  required? (throw (RuntimeException. (<< "could not resolve for fn-type: ~{fn-type} in ~{config}")))
                  true nil))

          true
          (throw (RuntimeException. (<< "unrecognised config for fn-type: ~{fn-type} in ~{config}"))))))

(defrecord-openly namespace-clomponent [config obj]
  Clomponent

  (create [this]
    (create this nil))

  (create [this additional-config]
    (dosync
     (if-not (ensure obj)
       (ref-set obj
                ((resolve-fn config :create) (merge config additional-config))))))

  (destroy [this]
    (dosync
     (if (ensure obj)
       (let [destroy-fn (resolve-fn config :destroy :required? false)
             result (if destroy-fn (try (destroy-fn config @obj)
                                        (catch Exception e (log/warn e (<< "error closing clomponent: ~{config}")))))]
         (ref-set obj nil)
         result))))

  (object [this]
   (dosync
    (if (ensure obj)
      @obj
      (create this))))

  (perform [this action]
    (dosync
     (if-not (ensure obj)
       (throw (RuntimeException. "no object!"))
       ((resolve-fn config action) config @obj)))))

(defn create-namespace-clomponent
  [config]
  (->namespace-clomponent config (ref nil)))
